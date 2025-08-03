"use client";
import Form from 'next/form';
import { syncApi } from "../api/syncApi";


const FormPage = () => {
  const { fetchSyncApi } = syncApi();

  const onSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    const formData = new FormData(e.currentTarget);
    const old_data = {
      content: formData.get('content') as string,
      author: formData.get('author') as string,
      timeStamp: formData.get('timestamp') as string,
    };
    const result = await fetchSyncApi(old_data);
    console.log('Result from sync API:', result);
    if (result === "Not cool") {
      alert("There was an issue with the sync API.");
    }
  }

  return (
    <div className="font-sans grid grid-rows-[20px_1fr_20px] items-center justify-items-center min-h-screen p-8 pb-20 gap-16 sm:p-20">
      <form onSubmit={onSubmit}>
        <input name="content" />
        <input name="author" />
        <input name="timestamp" />
        <button type="submit">Submit</button>
      </form>
    </div>
  );
}

export default FormPage;