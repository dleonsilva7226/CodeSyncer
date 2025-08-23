import { ENDPOINT_PREFIX } from ".";

export const mergeApi = () => {
    
    const fetchMergeApi = async (formData: FormData) => {
        try {
            const response = await fetch(`${ENDPOINT_PREFIX}/merge/suggestion`, {
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
            console.log('Data fetched from merge API:', data);
            return data;
        } catch (error) {
            console.error('Error fetching merge API:', error);
            throw error;
        }
    }

    const acceptMergeApi = async (mergeID: number) => {
        try {
            const response = await fetch(`${ENDPOINT_PREFIX}/merge/accept/${mergeID}`, {
                method: 'POST'
            });
            console.log('Response status for accept:', response.status);
            if (!response.ok) {
                window.alert("Merge Data not added in DB");
                return "Failed to accept merge";
            }
            const data = await response.json();
            console.log('Data after accepting merge:', data);
            window.alert("Merge Data added to DB");
            return data;
        } catch (error) {
            console.error('Error accepting merge API:', error);
            throw error;
        }
    }

    const rejectMergeApi = async (mergeID: number) => {
        try {
            const response = await fetch(`${ENDPOINT_PREFIX}/merge/reject/${mergeID}`, {
                method: 'DELETE'
            });
            console.log('Response status for reject:', response.status);
            if (!response.ok) {
                window.alert("Merge Data not found in DB");
                return "Failed to reject merge";
            }
            const data = await response.json();
            console.log('Data after rejecting merge:', data);
            window.alert("Merge Data deleted from DB");
            return data;
        } catch (error) {
            console.error('Error rejecting merge API:', error);
            throw error;
        }
    }

    return {
        fetchMergeApi,
        acceptMergeApi,
        rejectMergeApi
    }
    
}