export interface DiffEntry {
  id: number;
  type: string;
  oldStartLine: number;
  oldEndLine: number;
  oldLines: string[];
  newStartLine: number;
  newEndLine: number;
  newLines: string[];
  oldContextBeforeStartLines: string[];
  oldContextAfterEndLines: string[];
  newContextBeforeStartLines: string[];
  newContextAfterEndLines: string[];
}

export interface MergeData {
  id: number;
  author: string;
  timestamp: string;
  description: string;
  oldCode: string;
  newCode: string;
  mergedCode: string;
  mergeReason: string;
  isAccepted: boolean;
  diff: DiffEntry[];
}


// collaboration.ts

export type WsMessage =
  | { type: "join"; id: string }
  | { type: "quit"; id: string }
  | { type: "move"; id: string; x: number; y: number }
  | { type: "get-cursors" }
  | { type: "get-cursors-response"; sessions: Session[] };

export type Session = { id: string; x: number; y: number };