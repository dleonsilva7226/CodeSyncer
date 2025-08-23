import {flatMap} from "lodash";
import { JSX } from "react";
import {Diff, DiffProps, Decoration, DiffType, Hunk, HunkData, HunkProps} from 'react-diff-view';

interface RenderFileProps {
    oldRevision: string;
    newRevision: string;
    type: DiffType;
    hunks: HunkData[];
}

interface DiffViewProps {
    diffText: string;
    diffType: DiffType;
    hunks:  JSX.Element[];
}


const renderHunk = ({ hunk }: HunkProps) => [
    <Decoration key={'decoration-' + hunk.content}>
        {hunk.content}
    </Decoration>,
    <Hunk key={'hunk-' + hunk.content} hunk={hunk} />
];

export const DiffView = ({diffType, hunks}: DiffProps) => {
    <Diff viewType="split" diffType={"add"} hunks={hunks}>
        {flatMap(hunks, renderHunk)}
    </Diff>
};