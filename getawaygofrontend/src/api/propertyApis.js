import axios from 'axios';

const PROPERTY_API_URL = 'http://localhost:8090/properties';

const getToken = () => {
  return localStorage.getItem('encodedAccessToken');
}

export const getAllProperties = async () => {
  const response = await axios.get(`${PROPERTY_API_URL}`);
  return response;
};

export const getHighestPrice = async () => {
  const response = await axios.get(`${PROPERTY_API_URL}/max`);
  return response;
};

export const getAllPropertiesByCountry = async (country) => {
  const response = await axios.get(`${PROPERTY_API_URL}/country/${country}`);
  return response;
};

export const getFilteredProperties = async (town, max) => {
  const response = await axios.get(`${PROPERTY_API_URL}/${town}/${max}`);
  return response;
};

export const getAllPropertiesForHost = async (id) => {
  const response = await axios.get(`${PROPERTY_API_URL}/host/${id}`, {
    headers: { Authorization: `Bearer ${getToken()}` }
  });
  return response;
};

export const getProperty = async (id) => {
  const response = await axios.get(`${PROPERTY_API_URL}/${id}`);
  return response;
};

export const createProperty = (propertyData, id) => {
  return axios.post(`${PROPERTY_API_URL}/${id}`, propertyData, {
    headers: { Authorization: `Bearer ${getToken()}` }
  });
};

export const updateProperty = (propertyData) => {
  return axios.put(`${PROPERTY_API_URL}`, propertyData, {
    headers: { Authorization: `Bearer ${getToken()}` }
  });
};

export const uploadPictures = async (photos, id) => {
  const response = await axios.put(`${PROPERTY_API_URL}/photos/${id}`, photos, {
    headers: { Authorization: `Bearer ${getToken()}` }
  });
  return response;
};

export const deleteProperty = async (id) => {
  const response = await axios.delete(`${PROPERTY_API_URL}/${id}`, {
    headers: { Authorization: `Bearer ${getToken()}` }
  });
  return response;
};