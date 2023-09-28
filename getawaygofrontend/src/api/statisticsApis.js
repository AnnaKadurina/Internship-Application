import axios from 'axios';

const STATISTICS_API_URL = 'http://localhost:8090/statistics';

const getToken = () => {
    return localStorage.getItem('encodedAccessToken');
}

export const getUserStatisticsDto = async () => {
    const response = await axios.get(`${STATISTICS_API_URL}/users`, {
        headers: { Authorization: `Bearer ${getToken()}` }
    });
    return response;
};

export const getTop10Countries = async () => {
    const response = await axios.get(`${STATISTICS_API_URL}/properties`, {
        headers: { Authorization: `Bearer ${getToken()}` }
    });
    return response;
};

export const getReviewStatisticsDTO = async (id) => {
    const response = await axios.get(`${STATISTICS_API_URL}/reviews/${id}`, {
        headers: { Authorization: `Bearer ${getToken()}` }
    });
    return response;
};

export const getTop10Properties = async (id) => {
    const response = await axios.get(`${STATISTICS_API_URL}/booked/properties/${id}`, {
        headers: { Authorization: `Bearer ${getToken()}` }
    });
    return response;
};