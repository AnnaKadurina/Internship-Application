import axios from 'axios';

const REVIEW_API_URL = 'http://localhost:8090/reviews';

const getToken = () => {
    return localStorage.getItem('encodedAccessToken');
}

export const getReviews = async () => {
    const response = await axios.get(`${REVIEW_API_URL}`);
    return response;
};

export const getAverageReview = async (id) => {
    const response = await axios.get(`${REVIEW_API_URL}/average/${id}`);
    return response;
};

export const getReviewsForProperty = async (propertyId) => {
    const response = await axios.get(`${REVIEW_API_URL}/${propertyId}`);
    return response;
};

export const deleteReview = async (id) => {
    const response = await axios.delete(`${REVIEW_API_URL}/${id}`, {
        headers: { Authorization: `Bearer ${getToken()}` }
    });
    return response;
};

export const createReview = (reviewData) => {
    return axios.post(`${REVIEW_API_URL}`, reviewData, {
        headers: { Authorization: `Bearer ${getToken()}` }
    });
};