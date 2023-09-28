import * as React from 'react';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import '../styles/textfields.css';
import AutoComplete from "react-google-autocomplete";
import { useState } from 'react';
import { createProperty } from '../api/propertyApis';
import GOOGLE_API_KEY from '../api/googleApi';


const theme = createTheme();

export default function Create() {
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
            name: data.get('name'),
            address: data.get('address'),
            town: town,
            country: country,
            nrOfRooms: data.get('nrOfRooms'),
            description: data.get('description'),
            price: data.get('price')
        };
        console.log(propertyData)
        try {
            const response = await createProperty(propertyData, localStorage.getItem('id'));
            console.log(response)
            window.location.href = "/home";
        } catch (error) {
            console.log(error);
        }
    };

    return (
        <ThemeProvider theme={theme}>
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                <Box
                    sx={{
                        marginTop: 8,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <Typography component="h1" variant="h5" style={{ color: '#4B8D97' }}>
                        Where is your property?
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
                </Box>
            </Container>
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                <Box
                    sx={{
                        marginTop: 8,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <Typography component="h1" variant="h5" style={{ color: '#4B8D97' }}>
                        Fill in the details of your property
                    </Typography>
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
                                    inputProps={{
                                        maxLength: 50
                                    }}
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
                                    inputProps={{
                                        maxLength: 50
                                    }}
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
                                    inputProps={{
                                        maxLength: 50
                                    }}
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
                                    inputProps={{
                                        maxLength: 50
                                    }}
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
                                    inputProps={{
                                        min: 1
                                    }}
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
                                    multiline
                                    rows={10}
                                    inputProps={{
                                        maxLength: 500
                                    }}
                                />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    fullWidth
                                    type="number"
                                    name="price"
                                    label="Price (€)"
                                    id="price"
                                    autoComplete="price"
                                    className='textFieldStyle'
                                    inputProps={{
                                        min: 10
                                    }}
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
                            Create
                        </Button>
                    </Box>
                </Box>
            </Container>
        </ThemeProvider>
    );
}