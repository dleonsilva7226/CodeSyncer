// src/app/services/WebSocketService.ts
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export interface LocationMessage {
  userId: number;
  fileName: string;
  lineNumber: number;
  mergeId: number;
}

export interface ConflictWarning {
  type: 'CONFLICT_WARNING';
  fileName: string;
  conflictingUsers: string[];
  lineNumber: number;
  proximity: number;
  mergeId: number;
}

type ConflictHandler = (warning: ConflictWarning) => void;

export class WebSocketService {
  private client: Client | null = null;
  private connected: boolean = false;
  private conflictHandlers: ConflictHandler[] = [];

  constructor() {
    this.connect();
  }

  connect(): Promise<void> {
    return new Promise((resolve, reject) => {
      try {
        const socket = new SockJS('http://localhost:8080/ws');
        
        this.client = new Client({
          webSocketFactory: () => socket,
          debug: (str) => console.log('WebSocket:', str),
          reconnectDelay: 5000,
          heartbeatIncoming: 4000,
          heartbeatOutgoing: 4000,
        });

        this.client.onConnect = () => {
          console.log('Connected to WebSocket');
          this.connected = true;
          this.subscribeToConflicts();
          resolve();
        };

        this.client.onStompError = (frame) => {
          console.error('WebSocket error:', frame);
          reject(new Error(frame.headers['message']));
        };

        this.client.activate();
      } catch (error) {
        reject(error);
      }
    });
  }

  private subscribeToConflicts(): void {
    if (!this.client) return;

    this.client.subscribe('/topic/conflicts', (message) => {
      const warning: ConflictWarning = JSON.parse(message.body);
      this.conflictHandlers.forEach(handler => handler(warning));
    });
  }

  sendLocationUpdate(fileName: string, lineNumber: number, mergeId: number, userId: number): void {
    if (!this.connected || !this.client) return;

    const locationMessage: LocationMessage = {
      userId,
      fileName,
      lineNumber,
      mergeId
    };

    this.client.publish({
      destination: '/app/location/update',
      body: JSON.stringify(locationMessage)
    });
  }

  onConflictWarning(handler: ConflictHandler): () => void {
    this.conflictHandlers.push(handler);
    return () => {
      const index = this.conflictHandlers.indexOf(handler);
      if (index > -1) this.conflictHandlers.splice(index, 1);
    };
  }

  disconnect(): void {
    if (this.client) {
      this.client.deactivate();
      this.connected = false;
    }
  }

  isConnected(): boolean {
    return this.connected;
  }
}