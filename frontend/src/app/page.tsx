import { syncApi } from "./api/syncApi";
import Link from 'next/link'

export default function Home() {
  return (
    <div className="flex items-center justify-center min-h-screen bg-gradient-to-br from-blue-100 via-white to-blue-200">
      <div className="bg-white rounded-xl shadow-lg p-12 flex flex-col items-center max-w-lg w-full">
        <h1 className="text-4xl font-bold text-gray-800 mb-4">Welcome to CodeSyncer</h1>
        <p className="text-lg text-gray-600 mb-8 text-center">
          Effortlessly sync and compare your frontend and backend code. Get started by submitting your code!
        </p>
        <Link
          href="/form"
          className="bg-blue-500 hover:bg-blue-700 text-white font-semibold py-3 px-8 rounded-lg transition duration-200"
        >
          Go to Form Page
        </Link>
      </div>
    </div>
  );
}