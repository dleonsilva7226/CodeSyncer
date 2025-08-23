import React from 'react';
import {Card, CardContent} from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Check, X } from 'lucide-react';
import { CodeBlock, dracula } from 'react-code-blocks';
import type { DiffEntry, MergeData } from "../api/.";
import { mergeApi } from '../api/mergeApi';

interface MergeViewProps {
  data: MergeData;
}

export const MergeView: React.FC<MergeViewProps> = ({ data }) => {
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
  const { acceptMergeApi, rejectMergeApi } = mergeApi();

  console.log("Merge ID: " + id);
  const handleAccept = async () => {
    // Call acceptMergeApi with the merge ID
    const response = await acceptMergeApi(id);
    console.log("Accepted merge for ID:", data.id);
  };

  const handleReject = async () => {
    // Call rejectMergeApi with the merge ID
    const response = await rejectMergeApi(id);
    console.log("Rejected merge for ID:", data.id);
  };

  return (
    <div className="p-6 space-y-6">
      <h1 className="text-2xl font-bold">Merge Suggestion</h1>
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
            <h2 className="font-semibold text-lg mb-2">Old Code</h2>
            <CodeBlock text={oldCode} language="java" theme={dracula} />
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-4">
            <h2 className="font-semibold text-lg mb-2">New Code</h2>
            <CodeBlock text={newCode} language="java" theme={dracula} />
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardContent className="p-4 space-y-2">
          <h2 className="font-semibold text-lg">Merged Code</h2>
          <CodeBlock text={mergedCode} language="java" theme={dracula} />
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
    </div>
  );
};
