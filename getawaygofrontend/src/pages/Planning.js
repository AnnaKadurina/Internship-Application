import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { useNavigate } from "react-router-dom";
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import { getAllBookingsForProperty, getBookedDatesForProperty } from '../api/bookingApis';
import Stack from '@mui/material/Stack';
import Pagination from '@mui/material/Pagination';
import { getUserDto } from '../api/userApis';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DateCalendar } from '@mui/x-date-pickers/DateCalendar';
import { createTheme, responsiveFontSizes, ThemeProvider } from '@mui/material/styles';
import Switch from '@mui/material/Switch';
import FormControlLabel from '@mui/material/FormControlLabel';
import { alpha, styled } from '@mui/material/styles';

const BlueSwitch = styled(Switch)(({ theme }) => ({
    '& .MuiSwitch-switchBase.Mui-checked': {
        color: '#4B8D97',
        '&:hover': {
            backgroundColor: alpha('#4B8D97', theme.palette.action.hoverOpacity),
        },
    },
    '& .MuiSwitch-switchBase.Mui-checked + .MuiSwitch-track': {
        backgroundColor: '#76B2BC',
    },
}));

let theme = createTheme();
theme = responsiveFontSizes(theme);

export default function StandardSlider() {
    const location = useLocation();
    const property = location.state;
    const [bookingData, setBookingData] = useState([]);
    const [page, setPage] = useState(1);
    const [receiverUsername, setReceiverUsername] = useState('');
    const [receiverPhoto, setReceiverPhoto] = useState('');
    const navigate = useNavigate();
    const [bookedDates, setBookedDates] = useState([]);
    const [showOnlyUpcoming, setShowOnlyUpcoming] = useState(false);

    useEffect(() => {
        getBookedDatesForProperty(property.propertyId)
            .then((response) => {
                setBookedDates(response.data);
            })
            .catch((error) => console.error(error));
    }, [property]);

    const shouldDisableDate = (date) => {
        return bookedDates.some((bookedDate) => {
            const bookedDateCheck = new Date(bookedDate);
            return date >= bookedDateCheck && date <= bookedDateCheck;
        });
    };

    useEffect(() => {
        getAllBookingsForProperty(property.propertyId)
            .then((response) => {
                const allBookings = response.data.allBookings.map(async (booking) => {
                    const userResponse = await getUserDto(booking.userId);
                    const username = userResponse.data.username;
                    const photo = userResponse.data.photo;
                    return {
                        id: booking.bookingId,
                        startDate: booking.startDate,
                        endDate: booking.endDate,
                        price: booking.price,
                        username,
                        photo,
                    };
                });
                Promise.all(allBookings).then((bookings) => {
                    const filteredBookings = showOnlyUpcoming
                        ? bookings.filter((booking) => isUpcomingBooking(booking.startDate))
                        : bookings;
                    setBookingData(filteredBookings);
                });
            })
            .catch((error) => console.error('Error: ', error));
    }, [property, showOnlyUpcoming]);

    const isUpcomingBooking = (startDate) => {
        const currentDate = new Date();
        const bookingStartDate = new Date(startDate);
        return bookingStartDate >= currentDate;
    };

    const handleChatOpen = (username, photo) => {
        setReceiverUsername(username)
        setReceiverPhoto(photo)
        console.log(receiverUsername, receiverPhoto)
        navigate("/chatroom", { state: { receiverUsername: username, receiverPhoto: photo } });
    }

    return (
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flexDirection: 'column' }}>
            <ThemeProvider theme={theme}>
                <Typography variant="h4" style={{ color: '#4B8D97', margin: '15px' }}>
                    Planning of property: {property.name}
                </Typography>
            </ThemeProvider>
            <div style={{ width: 700, marginTop: 20 }} id='calendarPlanning'>

                <LocalizationProvider
                    dateAdapter={AdapterDayjs}
                    localeText={{
                        calendarWeekNumberHeaderText: '#',
                        calendarWeekNumberText: (weekNumber) => `${weekNumber}.`,
                    }}
                >
                    <DateCalendar shouldDisableDate={shouldDisableDate} style={{ backgroundColor: 'white', width: '40rem' }} />
                </LocalizationProvider>
            </div>

            <div style={{ width: '40rem', marginTop: 20 }}>
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    <FormControlLabel
                        control={<BlueSwitch checked={showOnlyUpcoming} onChange={() => setShowOnlyUpcoming(!showOnlyUpcoming)} />}
                        label="Show only upcoming bookings"
                    />
                </div>
                <Card sx={{ minWidth: 275 }} id='bookingsOfProperty'>
                    <CardContent>
                        <Typography variant="h5" component="div">
                            Bookings of the property:
                        </Typography>
                        {bookingData.slice((page - 1) * 3, page * 3).map(booking => (
                            <div key={booking.id} style={{ backgroundColor: '#D4DCDE', margin: '28px 0', minHeight: '9rem' }}>
                                <Typography>
                                    Booking ID: {booking.id} <br />
                                    Start date: {new Date(booking.startDate).toLocaleDateString('en-GB')} <br />
                                    End date: {new Date(booking.endDate).toLocaleDateString('en-GB')} <br />
                                    Price: {booking.price}€ <br />
                                </Typography>
                                <Button
                                    variant="contained"
                                    id='chat'
                                    sx={{
                                        backgroundColor: '#4B8D97',
                                        '&:hover': {
                                            backgroundColor: '#C3ACBA',
                                        },
                                    }}
                                    onClick={() => handleChatOpen(booking.username, booking.photo)}
                                >Chat with guest</Button>
                            </div>
                        ))}
                    </CardContent>

                </Card>
                <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column' }}>
                    <Stack spacing={2} sx={{ my: 2, justifyContent: 'center', textAlign: 'center' }}>
                        <Pagination
                            count={Math.ceil(bookingData.length / 2)}
                            page={page}
                            onChange={(event, value) => setPage(value)}
                            sx={{ "& .Mui-selected": { backgroundColor: "#C3ACBA", color: "#fff" } }}
                        />
                    </Stack>
                </div>
            </div>

        </div >


    );
}
