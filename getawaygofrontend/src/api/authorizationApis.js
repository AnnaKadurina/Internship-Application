import axios from 'axios';

const API_URL = 'http://localhost:8090';

const getToken = () => {
    return localStorage.getItem('encodedAccessToken');
}

export const getUser = async (id) => {
    const response = await axios.get(`${API_URL}/users/${id}`, {
        headers: { Authorization: `Bearer ${getToken()}` }
    });
    return response;
};