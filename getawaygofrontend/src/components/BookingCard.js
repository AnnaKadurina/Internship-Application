import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import GOOGLE_API_KEY from '../api/googleApi';
import { deleteBooking, generateFile } from '../api/bookingApis';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import MuiAlert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import { Carousel } from 'react-responsive-carousel';
import 'react-responsive-carousel/lib/styles/carousel.min.css';
import { styled } from '@mui/material/styles';

const StyledCarousel = styled(Carousel)({
    minHeight: '15rem',
    width: '50rem',
    height: '20rem'
});

const Alert = React.forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const fetchWeatherData = async (town, country) => {
    /*in this part of the code, we get the lat and long based on the town of the property of the booking with google api.
    Then we get the weather from open-mateo. */
    try {
        const geocodingResponse = await fetch(`https://maps.googleapis.com/maps/api/geocode/json?address=${town},${country}&key=${GOOGLE_API_KEY}`);
        const geocodingData = await geocodingResponse.json();

        const latitude = geocodingData.results[0].geometry.location.lat
        const longitude = geocodingData.results[0].geometry.location.lng

        const weatherResponse = await fetch(`https://api.open-meteo.com/v1/forecast?latitude=${latitude}&longitude=${longitude}&current_weather=true&hourly=temperature_2m`);
        const weatherData = await weatherResponse.json();

        return weatherData;
    } catch (error) {
        console.error('Error fetching weather data:', error);
        return null;
    }
};



export default function BookingCard({ id, property, images, startDate, endDate, price, username, photo }) {
    const [weather, setWeather] = useState(null);
    const [open, setOpen] = useState(false);
    const [openDialog, setOpenDialog] = useState(false);
    const [message, setMessage] = useState("");
    const [receiverUsername, setReceiverUsername] = useState('');
    const [receiverPhoto, setReceiverPhoto] = useState('');
    const navigate = useNavigate();

    const handleChatOpen = (username, photo) => {
        setReceiverUsername(username);
        setReceiverPhoto(photo);
        console.log(receiverUsername, receiverPhoto)
        navigate("/chatroom", { state: { receiverUsername: username, receiverPhoto: photo } });
    };


    const handleDownload = (id) => {
        generateFile(id)
            .then((response) => {
                const filePath = response.data;

                const downloadUrl = `${filePath}`;

                const link = document.createElement('a');
                link.href = downloadUrl;
                link.setAttribute('download', 'booking_confirmation.pdf');
                link.setAttribute('target', '_blank');
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            })
            .catch((error) => console.error(error));
    };

    const handleReviewOpen = (propertyId) => {
        navigate(`/guest/create/review/${propertyId}`, { state: { id: propertyId } });
    }

    const handleOpenDialog = () => {
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
    };

    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpen(false);
        setMessage("");
    }

    useEffect(() => {
        fetchWeatherData(property.town, property.country)
            .then((weatherData) => setWeather(weatherData))
            .catch((error) => console.error('Error fetching weather data:', error));
    }, [property]);

    const handleDelete = (id) => {
        deleteBooking(id).then((response) => {
            setOpen(true);
            setMessage(`Successfully canceled booking in ${property.name}!`)
            setOpenDialog(false)
            console.log(response)
            window.location.reload()
        })
            .catch((error) => console.error(error));
    }

    const isPastBooking = new Date(endDate) < new Date();

    const handleUpdateBookingOpen = () => {
        navigate(`/guest/update/booking/${id}`, { state: { property: property, isCreation: false, bookingId: id } });
    }

    return (
        <div>
            <Card sx={{ width: '50rem', mx: 'auto', my: 2 }}>
                <StyledCarousel>
                    <Carousel showArrows={true} showThumbs={false}>
                        {images.map((imageUrl, index) => (
                            <div key={index}>
                                <img src={imageUrl} alt={property.name} style={{ height: '20rem' }} />
                            </div>
                        ))}
                    </Carousel>
                </StyledCarousel>
                <CardContent>
                    <Typography gutterBottom variant="h5" component="div" sx={{ color: '#4B8D97' }}>
                        Booking ID: {id} <br />
                        {property.name}
                    </Typography>
                    <Typography variant="body2" color="text.secondary" sx={{ fontSize: '1rem' }}>
                        <LocationOnIcon sx={{ color: '#8E3B46', fontSize: 25 }} />
                        {property.address}, {property.town}, {property.country} <br />
                        Check-in: {new Date(startDate).toLocaleDateString('en-GB')} <br />
                        Checkout: {new Date(endDate).toLocaleDateString('en-GB')} <br />
                        Total price: {price}€
                    </Typography>
                    {weather && weather.current_weather && (
                        <div>
                            <Typography variant="body2" color="text.secondary" sx={{ fontSize: '1rem' }}>
                                Weather at the moment in {property.town}: {weather.current_weather.temperature}°C
                            </Typography>
                        </div>
                    )}

                </CardContent>
                <CardActions>
                    {!isPastBooking && (
                        <Button variant="contained"
                            sx={{
                                backgroundColor: '#4B8D97',
                                '&:hover': {
                                    backgroundColor: '#C3ACBA',
                                },
                            }}
                            onClick={() => handleOpenDialog(id)}
                        >Cancel booking</Button>
                    )}
                    {!isPastBooking && (
                        <Button variant="contained"
                            sx={{
                                backgroundColor: '#4B8D97',
                                '&:hover': {
                                    backgroundColor: '#C3ACBA',
                                },
                            }}
                            onClick={handleUpdateBookingOpen}
                        >Update booking</Button>
                    )}
                    {!isPastBooking && (
                        <Button variant="contained"
                            sx={{
                                backgroundColor: '#4B8D97',
                                '&:hover': {
                                    backgroundColor: '#C3ACBA',
                                },
                            }}
                            onClick={() => handleChatOpen(username, photo)}
                        >Chat with owner</Button>
                    )}
                    <Button variant="contained"
                        sx={{
                            backgroundColor: '#4B8D97',
                            '&:hover': {
                                backgroundColor: '#C3ACBA',
                            },
                        }}
                        onClick={() => handleDownload(id)}
                    >Download PDF overview</Button>
                    {isPastBooking && (
                        <Button
                            variant="contained"
                            sx={{
                                backgroundColor: '#4B8D97',
                                '&:hover': {
                                    backgroundColor: '#C3ACBA',
                                },
                            }}
                            onClick={() => handleReviewOpen(property.propertyId)}
                        >
                            Write review
                        </Button>
                    )}
                </CardActions>
                <Dialog open={openDialog} onClose={handleClose}>
                    <DialogTitle sx={{ backgroundColor: '#D4DCDE' }}>Cancel booking in {property.name}</DialogTitle>
                    <DialogContent sx={{ backgroundColor: '#D4DCDE' }}>
                        <DialogContentText>
                            By clicking the button confirm, this booking will be canceled.
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions sx={{ backgroundColor: '#D4DCDE' }}>
                        <Button onClick={handleCloseDialog} color="primary" sx={{ color: 'white', backgroundColor: '#4B8D97', '&:hover': { backgroundColor: '#C3ACBA', }, }}>
                            Back
                        </Button>
                        <Button onClick={() => handleDelete(id)} color="primary" sx={{ color: 'white', backgroundColor: '#4B8D97', '&:hover': { backgroundColor: '#C3ACBA', }, }}>
                            Cancel booking
                        </Button>
                    </DialogActions>
                </Dialog>
            </Card>
            <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                <Alert
                    onClose={handleClose}
                    severity="success"
                    sx={{ width: "100%", backgroundColor: "#4B8D97" }}
                >
                    {message}
                </Alert>
            </Snackbar>
        </div>
    );
}