interface RefactorResultsCardProps {
    response: string[];
}

export const RefactorResultsCard = ({ response }: RefactorResultsCardProps) => {
    return (
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
    );
}