// src/app/components/CollaborativeCodeBlock.tsx
import React, { useRef, useEffect } from 'react';
import { CodeBlock, dracula } from 'react-code-blocks';
import { WebSocketService } from '../../services/WebSocketService';

interface CollaborativeCodeBlockProps {
  text: string;
  language: string;
  fileName: string;
  mergeId: number;
  userId: number;
  webSocketService: WebSocketService;
  title?: string;
}

export const CollaborativeCodeBlock: React.FC<CollaborativeCodeBlockProps> = ({
  text,
  language,
  fileName,
  mergeId,
  userId,
  webSocketService,
  title
}) => {
  const containerRef = useRef<HTMLDivElement>(null);

  const handleClick = (event: React.MouseEvent<HTMLDivElement>) => {
    if (!containerRef.current) return;

    const rect = containerRef.current.getBoundingClientRect();
    const clickY = event.clientY - rect.top;
    
    // Estimate line number based on click position
    // CodeBlock typically has ~20px line height
    const estimatedLineHeight = 20;
    const lineNumber = Math.max(1, Math.ceil(clickY / estimatedLineHeight));
    
    // Don't send updates for clicks beyond the actual code
    const totalLines = text.split('\n').length;
    if (lineNumber <= totalLines) {
      console.log(`Clicked line ${lineNumber} in ${fileName}`);
      webSocketService.sendLocationUpdate(fileName, lineNumber, mergeId, userId);
    }
  };

  return (
    <div className="mb-4">
      {title && <h2 className="font-semibold text-lg mb-2">{title}</h2>}
      
      <div
        ref={containerRef}
        className="relative cursor-pointer border border-gray-200 rounded-lg overflow-hidden"
        onClick={handleClick}
        title="Click on any line to indicate your focus"
      >
        <CodeBlock 
          text={text} 
          language={language} 
          theme={dracula}
          showLineNumbers={true}
        />
        
        {/* Connection indicator */}
        <div className="absolute top-2 right-2 flex items-center space-x-1">
          <div className={`w-2 h-2 rounded-full ${
            webSocketService.isConnected() ? 'bg-green-500 animate-pulse' : 'bg-red-500'
          }`} />
          <span className="text-xs text-gray-500">
            {webSocketService.isConnected() ? 'Live' : 'Offline'}
          </span>
        </div>
      </div>
    </div>
  );
};