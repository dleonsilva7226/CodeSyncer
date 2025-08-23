import { ENDPOINT_PREFIX } from ".";

export const diffApi = () => {
    
    const fetchDiffApi = async (formData: FormData) => {
        try {
            const response = await fetch(`${ENDPOINT_PREFIX}/diff/hunks`, {
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
            console.log('Data fetched from diff API:', data);
            return data;
        } catch (error) {
            console.error('Error fetching diff API:', error);
            throw error;
        }
    }

    return {
        fetchDiffApi
    }
    
}