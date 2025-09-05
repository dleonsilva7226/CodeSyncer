"use client";
import { useState } from 'react';
import { mergeApi } from '../api/mergeApi';
import { LoadingPage } from '../components/LoadingPage';
import { FileUploadCard } from '../components/FileUploadCard';
import { MergeData } from '../api';
import { MergeView } from './MergeCodeCard';
import { EnhancedMergeView } from './EnhancedMergeCodeCard';

export const MergeCodeForm = () => {
  const {fetchMergeApi, acceptMergeApi, rejectMergeApi} = mergeApi(); 
  const [response, setResponse] = useState<MergeData | undefined>(undefined);
  const [isLoading, setIsLoading] = useState(false);
  const [oldFile, setOldFile] = useState<File | null>(null);
  const [newFile, setNewFile] = useState<File | null>(null);
  const acceptedOldFileTypes = ".java,.py,.rb,.go,.cs,.php,.js,.ts,.jsx,.tsx,.html,.css";
  const acceptedNewFileTypes = ".java,.py,.rb,.go,.cs,.php,.js,.ts,.jsx,.tsx,.html,.css";

  const handleOldFileChange = (file: File | null) => {
    setOldFile(file);
  };

  const handleNewFileChange = (file: File | null) => {
    setNewFile(file);
  };

  const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    setIsLoading(true);
    setResponse(undefined);
    e.preventDefault();
    const formData: FormData = new FormData();
    if (oldFile) formData.append("oldFile", oldFile);
    if (newFile) formData.append("newFile", newFile);
    formData.append("author", (e.currentTarget as HTMLFormElement).author.value);
    
    const result = await fetchMergeApi(formData as any);
    setResponse(result as any);
    console.log('Result from sync API:', result);
    if (result === "Not cool") {
      alert("There was an issue with the sync API.");
    }
  }

    return (
      <>
    <div className="flex flex-col items-center justify-center min-h-screen from-blue-100 via-white to-blue-200 p-8">
      <form onSubmit={onSubmit} className="flex flex-col bg-white shadow-2xl rounded-2xl px-6 pt-4 pb-6 w-full gap-[15px]">

        <h1 className="text-blue-700 text-center text-5xl font-extrabold mb-6 tracking-tight">Code Merge Displayer</h1>
          <div className="flex gap-[40px] justify-center items-center">
            <FileUploadCard onFileChange={handleOldFileChange} file={oldFile} codeType={"Old"} acceptedFileTypes={acceptedOldFileTypes}/>
            <FileUploadCard onFileChange={handleNewFileChange} file={newFile} codeType={"New"} acceptedFileTypes={acceptedNewFileTypes}/>
          </div>
          <div>
            <label className="block text-gray-700 text-lg font-semibold mb-2" htmlFor="author">
              Author
            </label>
            <input
              name="author"
              id="author"
              className="shadow border border-blue-200 rounded-lg w-full py-3 px-4 text-gray-700 leading-tight focus:outline-none focus:ring-2 focus:ring-blue-400 mb-8"
              placeholder="Enter author"
              required
            />
          </div>
        <button
          type="submit"
          className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-8 rounded-lg w-full text-lg transition duration-200"
        >
          Merge Code
        </button>
      </form>
    </div>
    <div className="w-full h-full flex justify-center">
      {response !== undefined ? (
          <EnhancedMergeView data={response}/>
        ) : (
          <LoadingPage isLoading={isLoading}/>
        )}
    </div>
  </>
);
}
