// src/app/components/ConflictWarningToast.tsx
import React, { useEffect } from 'react';
import { ConflictWarning } from '../services/WebSocketService';

interface ConflictWarningToastProps {
  warning: ConflictWarning | null;
  onDismiss: () => void;
}

export const ConflictWarningToast: React.FC<ConflictWarningToastProps> = ({
  warning,
  onDismiss
}) => {
  // Auto-dismiss after 5 seconds
  useEffect(() => {
    if (warning) {
      const timer = setTimeout(() => {
        onDismiss();
      }, 5000);

      return () => clearTimeout(timer);
    }
  }, [warning, onDismiss]);

  if (!warning) return null;

  return (
    <div className="fixed top-4 right-4 bg-yellow-100 border-l-4 border-yellow-500 p-4 rounded-lg shadow-lg z-50 max-w-sm animate-slide-in-from-right">
      <div className="flex items-start justify-between">
        <div className="flex items-start">
          <div className="flex-shrink-0">
            <svg className="w-5 h-5 text-yellow-600 mt-0.5" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
            </svg>
          </div>
          <div className="ml-3">
            <h3 className="text-sm font-medium text-yellow-800">
              Editing Conflict Detected
            </h3>
            <div className="mt-1 text-sm text-yellow-700">
              <p>
                {warning.conflictingUsers.length === 1 
                  ? `User ${warning.conflictingUsers[0]} is also` 
                  : `Users ${warning.conflictingUsers.join(', ')} are also`
                } viewing near line {warning.lineNumber} in <span className="font-medium">{warning.fileName}</span>
              </p>
            </div>
          </div>
        </div>
        
        <button
          onClick={onDismiss}
          className="ml-4 text-yellow-400 hover:text-yellow-600 focus:outline-none"
        >
          <span className="sr-only">Dismiss</span>
          <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
            <path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd" />
          </svg>
        </button>
      </div>
    </div>
  );
};