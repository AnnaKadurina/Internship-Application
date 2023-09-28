import React, { useState, useEffect } from 'react';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import '../styles/textfields.css';
import { createBooking, updateBooking, getBookedDatesForProperty } from '../api/bookingApis';
import { getUser } from '../api/authorizationApis';
import { useLocation } from "react-router-dom";
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import MuiAlert from '@mui/material/Alert';
import Snackbar from '@mui/material/Snackbar';
import emailjs from 'emailjs-com';
import { useNavigate } from "react-router-dom";


const theme = createTheme();

const Alert = React.forwardRef(function Alert(props, ref) {
    return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

export default function Create() {

    const location = useLocation();
    const property = location.state?.property || "";
    const isCreation = location.state?.isCreation;
    const bookingId = location.state?.bookingId || "";
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [price, setPrice] = useState(0);
    const [open, setOpen] = useState(false);
    const [message, setMessage] = useState("");
    const [bookedDates, setBookedDates] = useState([]);
    const [userData, setUserData] = useState([]);
    const navigate = useNavigate();


    useEffect(() => {
        getBookedDatesForProperty(property.propertyId)
            .then((response) => {
                setBookedDates(response.data);
            })
            .catch((error) => console.error(error));
    }, [property]);

    useEffect(() => {
        const id = localStorage.getItem('id');
        getUser(id)
            .then((userData) => {
                setUserData(userData);
            })
            .catch((error) => {
                console.log(error);
            });
    }, []);

    const sendEmailToConfirm = () => {
        const serviceID = 'service_ldvd77l';
        const templateID = 'template_km6owwb';
        const userID = 'aIi4ug20GkFerv4ur';

        const emailParams = {
            to_email: userData.data.email,
            to_name: userData.data.firstName,
            to_property: property.name,
        };

        emailjs.send(serviceID, templateID, emailParams, userID)
            .then((response) => {
                console.log('Email sent!', response.status, response.text);
            })
            .catch((error) => {
                console.error('Email sending failed:', error);
            });
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        const createBookingData = {
            startDate: startDate.toISOString(),
            endDate: endDate.toISOString(),
            userId: localStorage.getItem('id'),
            propertyId: property.propertyId,
        };
        const updateBookingData = {
            startDate: startDate.toISOString(),
            endDate: endDate.toISOString(),
            bookingId: bookingId,
        };
        try {
            if (isCreation) {
                const response = await createBooking(createBookingData);
                console.log(response);
                sendEmailToConfirm()
            } else {
                console.log(updateBookingData)
                const response = await updateBooking(updateBookingData);
                console.log(response);
            }
            navigate("/guest/bookings");
        } catch (error) {
            console.log(error);
            setOpen(true);
            setMessage(`Error: ${error.message}`)
        }
    };

    const handleClose = (event, reason) => {
        if (reason === 'clickaway') {
            return;
        }
        setOpen(false);
        setMessage("");
    }

    useEffect(() => {
        if (startDate && endDate) {
            const nights = endDate.diff(startDate, 'day');
            const totalPrice = property.price * nights;
            setPrice(totalPrice);
        }
    }, [startDate, endDate, property.price]);

    const currentDate = new Date();

    function isBeforeToday(date) {
        return date < currentDate;
    }
    function isBeforeStartDay(date) {
        return date < startDate;
    }

    const shouldDisableDate = (date) => {
        if (isBeforeToday(date)) {
            return true;
        }

        if (isBeforeStartDay(date)) {
            return true;
        }

        return bookedDates.some((bookedDate) => {
            const bookedDateCheck = new Date(bookedDate);
            return date >= bookedDateCheck && date <= bookedDateCheck;
        });
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
                        Please choose a check-in and checkout date.
                    </Typography>
                    <Box component="form" sx={{ mt: 3 }} onSubmit={handleSubmit} id='createUpdateForm'>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                                <Typography variant="subtitle1" gutterBottom>
                                    Start date:
                                </Typography>
                                <LocalizationProvider dateAdapter={AdapterDayjs} id="startDate">
                                    <DatePicker value={startDate} onChange={setStartDate} shouldDisableDate={shouldDisableDate} id="startDateInput" />
                                </LocalizationProvider>
                            </Grid>
                            <Grid item xs={12}>
                                <Typography variant="subtitle1" gutterBottom>
                                    End date:
                                </Typography>
                                <LocalizationProvider dateAdapter={AdapterDayjs} id="endDate">
                                    <DatePicker value={endDate} onChange={setEndDate} shouldDisableDate={shouldDisableDate} id="endDateInput" />
                                </LocalizationProvider>
                            </Grid>
                            <Grid item xs={12}>
                                <Typography variant="subtitle1" gutterBottom>
                                    Calculated total price: {price}€
                                </Typography>
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
                            {isCreation ? 'Book property' : 'Update booking'}
                        </Button>
                    </Box>
                </Box>
                <Snackbar open={open} autoHideDuration={6000} onClose={handleClose}>
                    <Alert onClose={handleClose} severity="success" sx={{ width: '100%', backgroundColor: '#4B8D97' }}>
                        {message}
                    </Alert>
                </Snackbar>
            </Container>
        </ThemeProvider >
    );
}