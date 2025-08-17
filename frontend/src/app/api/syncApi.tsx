export const syncApi = () => {
    const ENDPOINT_PREFIX = 'http://localhost:8080';

    const fetchSyncApi = async (formData: FormData) => {
        try {
            const response = await fetch(`${ENDPOINT_PREFIX}/api/suggest`, {
                method: 'POST',
                body: formData
            });
            console.log('Response status:', response.status);
            console.log('Response headers:', response.headers);
            console.log('Response URL:', response.url);
            if (!response.ok) {
                return "Not cool";
            }
            const data = await response.json();
            console.log('Data fetched from sync API:', data);
            return data;
        } catch (error) {
            console.error('Error fetching sync API:', error);
            throw error;
        }
    }

    return {
        fetchSyncApi
    }
    
}