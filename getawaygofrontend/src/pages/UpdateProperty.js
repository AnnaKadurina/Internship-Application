import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import { createTheme, responsiveFontSizes, ThemeProvider } from '@mui/material/styles';
import Typography from '@mui/material/Typography';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import AutoComplete from "react-google-autocomplete";
import TextField from '@mui/material/TextField';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import { updateProperty, uploadPictures } from '../api/propertyApis';
import GOOGLE_API_KEY from '../api/googleApi';
import { Carousel } from 'react-responsive-carousel';
import 'react-responsive-carousel/lib/styles/carousel.min.css';
import { styled } from '@mui/material/styles';


let theme = createTheme();
theme = responsiveFontSizes(theme);

const StyledCarousel = styled(Carousel)({
    width: '100%',
    minHeight: '20rem',
    maxHeight: '40rem'
});
const CustomMainImage = styled('img')({
    height: '22rem',
    objectFit: 'cover',
});

const CustomThumbImage = styled('img')({
    height: '3rem',
    objectFit: 'cover',
});

const PropertyCard = ({ property }) => {
    return (
        <div>
            <ThemeProvider theme={theme}>
                <Typography variant="h3" style={{ color: '#4B8D97' }}>
                    {property.name}
                </Typography>
            </ThemeProvider>
            <StyledCarousel
                renderThumbs={() =>
                    property.photosUrls.map((url, index) => (
                        <div key={index}>
                            <CustomThumbImage src={url} alt={property.name} />
                        </div>
                    ))
                }
            >
                {property.photosUrls.map((url, index) => (
                    <div key={index}>
                        <CustomMainImage src={url} alt={property.name} />
                    </div>
                ))}
            </StyledCarousel>
            <Typography variant="subtitle1" gutterBottom style={{ justifyContent: 'center' }}>
                {property.description}
            </Typography>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <LocationOnIcon sx={{ color: '#8E3B46', fontSize: 50 }} />
                <Typography variant="subtitle1" gutterBottom>
                    {property.address}, {property.town}, {property.country}
                </Typography>
            </div>
            <Typography variant="subtitle1" gutterBottom style={{ justifyContent: 'center' }}>
                Number of rooms: {property.nrOfRooms} <br />
                Price per night: {property.price}€
            </Typography>
        </div>
    );
};

export default function StandardSlider() {
    const location = useLocation();
    const property = location.state;
    const [town, setTown] = useState('');
    const [country, setCountry] = useState('');

    const handlePlaceSelected = (place) => {
        const addressComponents = place.address_components;
        const townComponent = addressComponents.find((component) =>
            component.types.includes('locality')
        );
        const countryComponent = addressComponents.find((component) =>
            component.types.includes('country')
        );

        if (townComponent) {
            setTown(townComponent.long_name);
        }
        if (countryComponent) {
            setCountry(countryComponent.long_name);
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        const propertyData = {
            id: property.propertyId,
            name: data.get('name'),
            address: data.get('address'),
            town: town,
            country: country,
            nrOfRooms: data.get('nrOfRooms'),
            description: data.get('description'),
            price: data.get('price')
        };
        try {
            const response = await updateProperty(propertyData);
            console.log(response)
            window.location.href = "/host/my/properties";
        } catch (error) {
            console.log(error.data);
        }
    };

    const handlePictureUpload = (event) => {
        event.preventDefault();
        const photoInput = document.querySelector('#photo');
        const formData = new FormData();
        for (let i = 0; i < photoInput.files.length; i++) {
            formData.append("photos", photoInput.files[i]);
        }

        try {
            console.log(formData)
            const response = uploadPictures(formData, property.propertyId);
            window.location.href = "/host/my/properties";
            console.log(response.data);
        } catch (error) {
            console.log(error.data);
        }
    };

    return (
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flexDirection: 'column' }}>

            <div style={{ width: 700, marginTop: 20 }}>
                <Card sx={{ minWidth: 275 }}>
                    <CardContent>
                        <Typography variant="h5" component="div">
                            Current Property Details
                        </Typography>
                        <PropertyCard property={property} />
                    </CardContent>
                </Card>
            </div>
            <div style={{ width: 700, marginTop: 20 }}>

                <Card sx={{ minWidth: 275 }}>
                    <CardContent>
                        <Typography variant="h5" component="div">
                            Upload photos for your property
                        </Typography>
                        <Box component="form" sx={{ mt: 3 }} onSubmit={handlePictureUpload}>
                            <input
                                id="photo"
                                type="file"
                                multiple
                                name="photo"
                                required
                                autoFocus
                                className="textFieldStyle"
                            />
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{
                                    mt: 3,
                                    mb: 2,
                                    backgroundColor: '#4B8D97',
                                    '&:hover': {
                                        backgroundColor: '#C3ACBA',
                                    },
                                }}
                            >
                                Upload photos
                            </Button>
                        </Box>

                        <Typography variant="h5" component="div">
                            Update form
                        </Typography>
                        <Box component="form" sx={{ mt: 3 }}>
                            <Grid container spacing={2}>
                                <Grid item xs={12}>
                                    <AutoComplete
                                        required
                                        id="address"
                                        label="Address"
                                        name="address"
                                        apiKey={GOOGLE_API_KEY}
                                        onPlaceSelected={handlePlaceSelected}
                                        className='autoCompleteStyle'
                                        style={{ border: '2px solid #4B8D97', borderRadius: '4px' }}
                                    />
                                </Grid>
                            </Grid>
                        </Box>
                        <Box component="form" sx={{ mt: 3 }} onSubmit={handleSubmit}>
                            <Grid container spacing={2}>
                                <Grid item xs={12}>
                                    <TextField
                                        autoComplete="name"
                                        name="name"
                                        required
                                        fullWidth
                                        id="name"
                                        label="Name of the property"
                                        autoFocus
                                        className='textFieldStyle'
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        autoComplete="address"
                                        name="address"
                                        required
                                        fullWidth
                                        id="address"
                                        label="Street address"
                                        autoFocus
                                        className='textFieldStyle'
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        required
                                        disabled
                                        fullWidth
                                        id="town"
                                        label="Town"
                                        name="town"
                                        autoComplete="town"
                                        className='textFieldStyle'
                                        value={town}
                                        onChange={(event) => setTown(event.target.value)}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        required
                                        disabled
                                        fullWidth
                                        id="country"
                                        label="Country"
                                        name="country"
                                        autoComplete="country"
                                        className='textFieldStyle'
                                        value={country}
                                        onChange={(event) => setCountry(event.target.value)}
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        required
                                        fullWidth
                                        type="number"
                                        id="nrOfRooms"
                                        label="Number of rooms"
                                        name="nrOfRooms"
                                        autoComplete="nrOfRooms"
                                        className='textFieldStyle'
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        required
                                        fullWidth
                                        id="description"
                                        label="Description"
                                        name="description"
                                        autoComplete="description"
                                        className='textFieldStyle'
                                    />
                                </Grid>
                                <Grid item xs={12}>
                                    <TextField
                                        required
                                        fullWidth
                                        type="number"
                                        name="price"
                                        label="Price"
                                        id="price"
                                        autoComplete="price"
                                        className='textFieldStyle'
                                    />
                                </Grid>

                            </Grid>
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{
                                    mt: 3,
                                    mb: 2,
                                    backgroundColor: '#4B8D97',
                                    '&:hover': {
                                        backgroundColor: '#C3ACBA',
                                    },
                                }}
                            >
                                Update
                            </Button>
                        </Box>
                    </CardContent>
                </Card>
            </div>
        </div >


    );
}
