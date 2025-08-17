"use client";
import { useEffect, useState } from 'react';
import { syncApi } from "../api/syncApi";
import { LoadingPage } from '../components/LoadingPage';
import { FileUploadCard } from '../components/FileUploadCard';

const FormPage = () => {
  const { fetchSyncApi } = syncApi();
  const [response, setResponse] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [frontendFile, setFrontendFile] = useState<File | null>(null);
  const [backendFile, setBackendFile] = useState<File | null>(null);
  const acceptedFrontendFileTypes = ".js,.ts,.jsx,.tsx,.html,.css";
  const acceptedBackendFileTypes = ".java,.py,.rb,.go,.cs,.php,.js,.ts";

  const handleFrontendFileChange = (file: File | null) => {
    setFrontendFile(file);
  };

  const handleBackendFileChange = (file: File | null) => {
    setBackendFile(file);
  };

  const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    setIsLoading(true);
    setResponse([]);
    e.preventDefault();
    const formData: FormData = new FormData();
    if (frontendFile) formData.append("frontendCode", frontendFile);
    if (backendFile) formData.append("backendCode", backendFile);
    formData.append("author", (e.currentTarget as HTMLFormElement).author.value);
    
    const result = await fetchSyncApi(formData);
    setResponse(result as any);
    console.log('Result from sync API:', result);
    if (result === "Not cool") {
      alert("There was an issue with the sync API.");
    }
  }

    return (
  <div className="flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-blue-100 via-white to-blue-200 p-4 gap-[40px]">
    <form onSubmit={onSubmit} className="bg-white shadow-2xl rounded-2xl px-10 pt-8 pb-10 w-full max-w-2xl">
      <h1 className="text-blue-700 text-center text-5xl font-extrabold mb-6 tracking-tight">CodeSyncer</h1>
        <div className="flex flex-col gap-[40px] justify-center items-center">
          <FileUploadCard onFileChange={handleFrontendFileChange} file={frontendFile} codeType={"Frontend"} acceptedFileTypes={acceptedFrontendFileTypes}/>
          <FileUploadCard onFileChange={handleBackendFileChange} file={backendFile} codeType={"Backend"} acceptedFileTypes={acceptedBackendFileTypes}/>
        </div>
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
      <button
        type="submit"
        className="bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-8 rounded-lg w-full text-lg transition duration-200"
      >
        Submit
      </button>
    </form>
    {response.length > 0 ? (
      <div className="mt-10 w-full max-w-2xl">
        <div className="bg-white rounded-2xl shadow-xl p-8 border border-blue-100">
          <h2 className="text-2xl font-bold mb-4 text-blue-700">Suggestions</h2>
          <ul className="list-disc pl-6 text-gray-800 space-y-2">
            {response.map((suggestion, idx) => (
              <li key={idx} className="bg-blue-50 rounded px-3 py-2">{suggestion}</li>
            ))}
          </ul>
        </div>
      </div>
    ) : (
      <LoadingPage isLoading={isLoading}/>
    )}
  </div>
);
}

export default FormPage;