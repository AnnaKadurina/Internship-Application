import React, { useState, useEffect } from 'react';
import BookingCard from '../components/BookingCard';
import { getAllBookingsForUser } from '../api/bookingApis';
import { getProperty } from '../api/propertyApis';
import { getUserDto } from '../api/userApis';
import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';
import Switch from '@mui/material/Switch';
import FormControlLabel from '@mui/material/FormControlLabel';
import { alpha, styled } from '@mui/material/styles';

const PinkSwitch = styled(Switch)(({ theme }) => ({
    '& .MuiSwitch-switchBase.Mui-checked': {
        color: '#B295A7',
        '&:hover': {
            backgroundColor: alpha('#8E677F', theme.palette.action.hoverOpacity),
        },
    },
    '& .MuiSwitch-switchBase.Mui-checked + .MuiSwitch-track': {
        backgroundColor: '#C3ACBA',
    },
}));

export default function AllBookings() {
    const [cardData, setCardData] = useState([]);
    const [page, setPage] = useState(1);
    const [showOnlyUpcoming, setShowOnlyUpcoming] = useState(false);

    useEffect(() => {
        getAllBookingsForUser(localStorage.getItem('id'))
            .then((response) => {
                const allBookings = response.data.allBookings.map(async (booking) => {
                    const propertyResponse = await getProperty(booking.propertyId);
                    const property = propertyResponse.data;
                    const userResponse = await getUserDto(property.userId);
                    const username = userResponse.data.username;
                    const photo = userResponse.data.photo;
                    return {
                        id: booking.bookingId,
                        startDate: booking.startDate,
                        endDate: booking.endDate,
                        price: booking.price,
                        property,
                        username,
                        photo,
                    };
                });
                Promise.all(allBookings).then((bookingsWithDetails) => {
                    const filteredBookings = showOnlyUpcoming
                        ? bookingsWithDetails.filter((booking) => isUpcomingBooking(booking.startDate))
                        : bookingsWithDetails;
                    setCardData(filteredBookings);
                });
            })
            .catch((error) => console.error('Error: ', error));
    }, [showOnlyUpcoming]);

    const isUpcomingBooking = (startDate) => {
        const currentDate = new Date();
        const bookingStartDate = new Date(startDate);
        return bookingStartDate >= currentDate;
    };

    return (
        <div>
            <div style={{ display: 'flex', alignItems: 'center', margin: '2vw' }}>
                <FormControlLabel
                    control={<PinkSwitch checked={showOnlyUpcoming} onChange={() => setShowOnlyUpcoming(!showOnlyUpcoming)} />}
                    label="Show only upcoming bookings"
                />
            </div>
            {cardData && cardData.length > 0 ? (
                <>
                    <div style={{ display: 'flex', justifyContent: 'center', flexWrap: 'wrap' }} id='bookingInfo'>
                        {cardData.slice((page - 1) * 1, page * 1).map((card) => (
                            <BookingCard
                                key={card.id}
                                id={card.id}
                                property={card.property}
                                images={card.property.photosUrls && card.property.photosUrls.length > 0 ? card.property.photosUrls : ''}
                                startDate={card.startDate}
                                endDate={card.endDate}
                                price={card.price}
                                username={card.username}
                                photo={card.photo}
                            />
                        ))}
                    </div>
                    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column' }}>
                        <Stack spacing={2} sx={{ my: 2, justifyContent: 'center', textAlign: 'center' }}>
                            <Pagination
                                count={Math.ceil(cardData.length / 1)}
                                page={page}
                                onChange={(event, value) => setPage(value)}
                                sx={{ '& .Mui-selected': { backgroundColor: '#C3ACBA', color: '#fff' } }}
                            />
                        </Stack>
                    </div>
                </>
            ) : (
                <p style={{ marginTop: 220 }}>There are no bookings yet.</p>
            )}
        </div>
    );
}
