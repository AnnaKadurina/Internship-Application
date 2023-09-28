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
import { createReview } from '../api/reviewApis';
import Rating from '@mui/material/Rating';
import { useLocation } from "react-router-dom";

const theme = createTheme();

export default function Create() {

    const location = useLocation();
    const propertyId = location.state?.id || ""; // Access the id from location.state

    const handleSubmit = async (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        const reviewData = {
            text: data.get('text'),
            rating: parseInt(data.get('customized-10')),
            userId: localStorage.getItem('id'),
            propertyId: propertyId
        };
        try {
            console.log(reviewData)
            const response = await createReview(reviewData);
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
                        Share Your Experience: <br />
                        Write a Review for the Property
                    </Typography>
                    <Box component="form" sx={{ mt: 3 }} onSubmit={handleSubmit}>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>

                                <Rating name="customized-10" defaultValue={1} max={10} min={1} />
                            </Grid>
                            <Grid item xs={12}>
                                <TextField
                                    required
                                    fullWidth
                                    id="text"
                                    label="Text of review"
                                    name="text"
                                    autoComplete="text"
                                    className='textFieldStyle'
                                    multiline
                                    rows={10}
                                    inputProps={{
                                        maxLength: 500
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
                            Post review
                        </Button>
                    </Box>
                </Box>
            </Container>
        </ThemeProvider >
    );
}