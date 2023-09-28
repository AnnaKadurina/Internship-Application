import axios from 'axios';

const USER_API_URL = 'http://localhost:8090/users';

const getToken = () => {
  return localStorage.getItem('encodedAccessToken');
}

export const getUserDto = async (id) => {
  const response = await axios.get(`${USER_API_URL}/details/${id}`, {
    headers: { Authorization: `Bearer ${getToken()}` }
  });
  return response;
};

export const registerUser = (userData) => {
  return axios.post(`${USER_API_URL}`, userData);
};

export const login = (userData) => {
  return axios.post(`${USER_API_URL}/login`, userData);
};

export const uploadPicture = async (photo, id) => {
  const response = await axios.put(`${USER_API_URL}/photo/${id}`, photo, {
    headers: { Authorization: `Bearer ${getToken()}` }
  });
  return response;
};

export const updateRole = async (id) => {
  return axios({
    method: 'PUT',
    url: `${USER_API_URL}/host/${id}`,
    headers: {
      'Authorization': 'Bearer ' + getToken()
    }
  })
};

export const updateData = async (userData, id) => {
  try {
    const response = await axios.put(`${USER_API_URL}/${id}`, userData, {
      headers: { Authorization: `Bearer ${getToken()}` }
    });
    return response.data;
  } catch (error) {
    throw new Error(error.response.data.message || 'Failed to update data');
  }
};

export const getUsers = async () => {
  const response = await axios.get(`${USER_API_URL}`, {
    headers: { Authorization: `Bearer ${getToken()}` }
  });
  return response;
};

export const deactivateUser = async (id) => {
  return axios({
    method: 'PUT',
    url: `${USER_API_URL}/deactivate/${id}`,
    headers: {
      'Authorization': 'Bearer ' + getToken()
    }
  })
};

export const activateUser = async (id) => {
  return axios({
    method: 'PUT',
    url: `${USER_API_URL}/activate/${id}`,
    headers: {
      'Authorization': 'Bearer ' + getToken()
    }
  })
};

export const deleteUser = async (id) => {
  const response = await axios.delete(`${USER_API_URL}/${id}`, {
    headers: { Authorization: `Bearer ${getToken()}` }
  });
  return response;
};

