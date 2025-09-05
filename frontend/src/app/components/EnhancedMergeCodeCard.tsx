import React, { useState, useEffect } from 'react';
import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Check, X } from 'lucide-react';
import type { MergeData } from "../api/.";
import { mergeApi } from '../api/mergeApi';
import { WebSocketService, ConflictWarning } from '../services/WebSocketService';
import { CollaborativeCodeBlock } from './collaboration/CollaborativeCodeBlock';
import { ConflictWarningToast } from './ConflictWarningToast';

interface EnhancedMergeViewProps {
  data: MergeData;
}

export const EnhancedMergeView: React.FC<EnhancedMergeViewProps> = ({ data }) => {
  const {
    id,
    author,
    timestamp,
    description,
    oldCode,
    newCode,
    mergedCode,
    mergeReason,
    isAccepted,
  } = data;

  // WebSocket state
  const [webSocketService, setWebSocketService] = useState<WebSocketService | null>(null);
  const [conflictWarning, setConflictWarning] = useState<ConflictWarning | null>(null);
  const [isConnected, setIsConnected] = useState(false);

  // Mock user ID - in real app, get from authentication
  const userId = 123; // Replace with actual user ID from auth context

  const { acceptMergeApi, rejectMergeApi } = mergeApi();

  // Initialize WebSocket connection
  useEffect(() => {
    const wsService = new WebSocketService();
    
    wsService.connect()
      .then(() => {
        console.log('WebSocket connected for merge conflict detection');
        setIsConnected(true);
        setWebSocketService(wsService);

        // Subscribe to conflict warnings
        wsService.onConflictWarning((warning) => {
          // Only show warnings for this specific merge
          if (warning.mergeId === id) {
            setConflictWarning(warning);
          }
        });
      })
      .catch((error) => {
        console.error('Failed to connect WebSocket:', error);
      });

    // Cleanup on unmount
    return () => {
      wsService.disconnect();
    };
  }, [id]);

  const handleAccept = async () => {
    const response = await acceptMergeApi(id);
    console.log("Accepted merge for ID:", id);
  };

  const handleReject = async () => {
    const response = await rejectMergeApi(id);
    console.log("Rejected merge for ID:", id);
  };

  const dismissConflictWarning = () => {
    setConflictWarning(null);
  };

  if (!webSocketService) {
    return (
      <div className="p-6 text-center">
        <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
        <p className="mt-2 text-gray-600">Connecting to collaboration service...</p>
      </div>
    );
  }

  return (
    <div className="p-6 space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Merge Suggestion</h1>
        <div className="flex items-center space-x-2 text-sm">
          <div className={`w-2 h-2 rounded-full ${isConnected ? 'bg-green-500 animate-pulse' : 'bg-red-500'}`} />
          <span className="text-gray-600">
            {isConnected ? 'Live Collaboration' : 'Offline'}
          </span>
        </div>
      </div>
      
      <p className="text-sm text-gray-500">By {author} on {new Date(timestamp).toLocaleString()}</p>

      <Card>
        <CardContent className="p-4 space-y-2">
          <h2 className="font-semibold text-lg">Description</h2>
          <p>{description.trim()}</p>
        </CardContent>
      </Card>

      <div className="grid md:grid-cols-2 gap-6">
        <Card>
          <CardContent className="p-4">
            <CollaborativeCodeBlock
              text={oldCode}
              language="java"
              fileName="old-version.java"
              mergeId={id}
              userId={userId}
              webSocketService={webSocketService}
              title="Old Code"
            />
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-4">
            <CollaborativeCodeBlock
              text={newCode}
              language="java"
              fileName="new-version.java"
              mergeId={id}
              userId={userId}
              webSocketService={webSocketService}
              title="New Code"
            />
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardContent className="p-4 space-y-2">
          <CollaborativeCodeBlock
            text={mergedCode}
            language="java"
            fileName="merged-version.java"
            mergeId={id}
            userId={userId}
            webSocketService={webSocketService}
            title="Merged Code"
          />
        </CardContent>
      </Card>

      <Card>
        <CardContent className="p-4 space-y-2">
          <h2 className="font-semibold text-lg">Merge Reason</h2>
          <p>{mergeReason.trim()}</p>
        </CardContent>
      </Card>

      <div className="flex items-center gap-4">
        <Button variant="outline" className="flex items-center gap-2" onClick={handleAccept}>
          <Check className="w-4 h-4" /> Accept
        </Button>
        <Button variant="destructive" className="flex items-center gap-2" onClick={handleReject}>
          <X className="w-4 h-4" /> Reject
        </Button>
        <span className="text-sm text-gray-500 ml-4">
          Status: {isAccepted ? 'Accepted' : 'Pending'}
        </span>
      </div>

      {/* Conflict Warning Toast */}
      <ConflictWarningToast
        warning={conflictWarning}
        onDismiss={dismissConflictWarning}
      />
    </div>
  );
};