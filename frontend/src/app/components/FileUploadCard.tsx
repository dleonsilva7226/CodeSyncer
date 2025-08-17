import React from 'react';

interface FileUploadCardProps {
    onFileChange: (file: File | null) => void;
    file: File | null;
    codeType: string;
    acceptedFileTypes: string;
}

export const FileUploadCard: React.FC<FileUploadCardProps> = ({ onFileChange, file, codeType, acceptedFileTypes }) => {
  return (
    <div className="bg-white shadow-lg rounded-xl p-6 w-full max-w-md flex flex-col items-center">
        <h2 className="text-xl font-bold mb-4">{codeType} Code</h2>
        <label className="cursor-pointer bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded mb-2">
            Upload File
            <input
            type="file"
            accept={acceptedFileTypes}
            onChange={e => onFileChange(e.target.files && e.target.files[0] ? e.target.files[0] : null)}
            className="hidden"
            />
        </label>
        {file && <p className="mt-2 text-sm text-gray-600">{file.name}</p>}
    </div>
  );
};
