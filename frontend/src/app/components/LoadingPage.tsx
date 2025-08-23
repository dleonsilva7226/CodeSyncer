interface LoadingProps {
    isLoading?: boolean;
}

export const LoadingPage = ({ isLoading }: LoadingProps) => (
  isLoading ? (
    <div className="flex flex-col items-center justify-center rounded-lg bg-gradient-to-br from-blue-200 via-white to-blue-400 w-full h-[15rem]">
      <div className="relative flex items-center justify-center mb-6 w-full max-w-md">
        <div className="absolute w-16 h-16 border-4 border-blue-300 border-t-blue-600 rounded-full animate-spin"></div>
        <div className="w-8 h-8 bg-blue-500 rounded-full"></div>
      </div>
      <p className="text-blue-700 text-xl font-bold tracking-wide w-full text-center">Loading...</p>
    </div>
  ) : (
    <div>
    </div>
  )
);