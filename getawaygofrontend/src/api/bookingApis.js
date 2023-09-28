import axios from 'axios';

const BOOKING_API_URL = 'http://localhost:8090/bookings';

const getToken = () => {
    return localStorage.getItem('encodedAccessToken');
}

export const createBooking = async (bookingData) => {
    try {
        const response = await axios.post(`${BOOKING_API_URL}`, bookingData, {
            headers: { Authorization: `Bearer ${getToken()}` }
        });
        return response.data;
    } catch (error) {
        throw new Error(error.response.data.message || 'Failed to create booking');
    }
};

export const updateBooking = async (bookingData) => {
    try {
        const response = await axios.put(`${BOOKING_API_URL}`, bookingData, {
            headers: { Authorization: `Bearer ${getToken()}` }
        });
        return response.data;
    } catch (error) {
        throw new Error(error.response.data.message || 'Failed to update booking');
    }
};

export const getAllBookingsForUser = async (id) => {
    const response = await axios.get(`${BOOKING_API_URL}/user/${id}`, {
        headers: { Authorization: `Bearer ${getToken()}` }
    });
    return response;
};

export const getAllBookingsForProperty = async (id) => {
    const response = await axios.get(`${BOOKING_API_URL}/property/${id}`, {
        headers: { Authorization: `Bearer ${getToken()}` }
    });
    return response;
};

export const getBookedDatesForProperty = async (id) => {
    const response = await axios.get(`${BOOKING_API_URL}/booked/${id}`, {
        headers: { Authorization: `Bearer ${getToken()}` }
    });
    return response;
};

export const generateFile = async (id) => {
    const response = await axios.post(`${BOOKING_API_URL}/confirmation/${id}`, {
        headers: { Authorization: `Bearer ${getToken()}` }
    });
    return response;
};

export const deleteBooking = async (id) => {
    const response = await axios.delete(`${BOOKING_API_URL}/${id}`, {
        headers: { Authorization: `Bearer ${getToken()}` }
    });
    return response;
};
