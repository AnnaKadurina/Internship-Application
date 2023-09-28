import axios from 'axios';

const CHAT_API_URL = 'http://localhost:8090/{username}/messages';

const getToken = () => {
    return localStorage.getItem('encodedAccessToken');
}

export const loadMessages = async (username) => {
    const response = await axios.get(`${CHAT_API_URL.replace('{username}', username)}`, {
        headers: { Authorization: `Bearer ${getToken()}` }
    });
    return response;
};
