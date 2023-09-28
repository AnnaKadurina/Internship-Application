import React, { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { createTheme, responsiveFontSizes, ThemeProvider } from '@mui/material/styles';
import Typography from '@mui/material/Typography';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import Button from '@mui/material/Button';
import Stack from '@mui/material/Stack';
import { getReviewsForProperty, getAverageReview } from '../api/reviewApis';
import Rating from '@mui/material/Rating';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Pagination from '@mui/material/Pagination';
import { useNavigate } from 'react-router-dom';
import { getUser } from '../api/authorizationApis';
import StarIcon from '@mui/icons-material/Star';
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

const PropertyCardInfo = ({ property, handleBookingOpen, showBookButton }) => {
    return (
        <div id="propertyInfo">
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
                <LocationOnIcon sx={{ color: '#8E3B46', fontSize: 35 }} />
                <Typography variant="subtitle1" gutterBottom>
                    {property.address}, {property.town}, {property.country}
                </Typography>
            </div>
            <Typography variant="subtitle1" gutterBottom style={{ justifyContent: 'center' }}>
                Number of rooms: {property.nrOfRooms} <br />
                Price per night: {property.price}€
            </Typography>
            {showBookButton && (
                <Stack spacing={2} direction="row" style={{ display: 'flex', justifyContent: 'center', flexWrap: 'wrap' }}>
                    <Button
                        id='book'
                        variant="contained"
                        sx={{ backgroundColor: '#4B8D97', '&:hover': { backgroundColor: '#C3ACBA' } }}
                        onClick={handleBookingOpen}
                    >
                        Book
                    </Button>
                </Stack>
            )}
        </div>
    );
};

export default function PropertyDetailsPage() {
    const location = useLocation();
    const property = location.state;
    const [reviews, setReviews] = useState([]);
    const [average, setAverage] = useState([]);
    const [total, setTotal] = useState([]);
    const [page, setPage] = useState(1);
    const [showBookButton, setShowBookButton] = useState(false);
    const navigate = useNavigate();

    const handleBookingOpen = () => {
        console.log(property)
        navigate(`/guest/create/booking/${property.propertyId}`, { state: { property: property, isCreation: true } });
    }

    useEffect(() => {
        getReviewsForProperty(property.propertyId)
            .then((response) => {
                const allReviews = response.data.allReviews.map((review) => ({
                    id: review.id,
                    ...review,
                }));
                setReviews(allReviews);
                console.log(getAverageReview)
            })
            .catch((error) => console.error('Error: ', error));
    }, [property.propertyId]);

    useEffect(() => {
        getAverageReview(property.propertyId)
            .then((response) => {
                const average = response.data.averageRating;
                const totalCount = response.data.totalCount;
                const formattedAverage = average.toFixed(1);
                setTotal(totalCount)
                setAverage(formattedAverage)
            })
            .catch((error) => console.error('Error: ', error));
    }, [property.propertyId]);

    useEffect(() => {
        const userId = localStorage.getItem('id');
        if (userId) {
            getUser(userId)
                .then((response) => {
                    const userRole = response.data.role;
                    setShowBookButton(userRole === 'GUEST');
                })
                .catch((error) => console.error('Error: ', error));
        } else {
            setShowBookButton(false);
        }
    }, []);

    return (
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', flexDirection: 'column' }}>

            <div style={{ width: 700, marginTop: 20 }}>
                <Card sx={{ minWidth: 275 }}>
                    <CardContent>
                        <Typography variant="h5" component="div">
                            Property Details
                        </Typography>
                        <PropertyCardInfo property={property} handleBookingOpen={handleBookingOpen} showBookButton={showBookButton} />
                    </CardContent>
                </Card>
            </div>
            <div style={{ width: 700, marginTop: 20 }}>
                <Card sx={{ minWidth: 275 }}>
                    <CardContent>
                        <Typography variant="h5" component="div" style={{ textAlign: 'left' }} id="reviews">
                            Total reviews: {total} <br />
                            Average rating: {average} <StarIcon style={{ color: 'orange' }} />
                        </Typography>
                        <Stack spacing={2} sx={{ maxWidth: 700, width: '100%' }}>
                            {reviews.slice((page - 1) * 5, page * 5).map((review) => (
                                <Card key={review.reviewId} sx={{ backgroundColor: '#4B8D97' }}>
                                    <CardContent>
                                        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                            <Typography variant="body1" component="span" color='white'>{review.text}</Typography>
                                            <Rating name="read-only" value={review.rating} max={10} readOnly />
                                        </div>
                                    </CardContent>
                                </Card>
                            ))}
                        </Stack>

                    </CardContent>
                </Card>

            </div>
            {reviews && reviews.length !== 0 && (
                <Stack spacing={2} sx={{ my: 2, justifyContent: 'center', textAlign: 'center' }}>
                    <Pagination
                        count={Math.ceil(reviews.length / 5)}
                        page={page}
                        onChange={(event, value) => setPage(value)}
                        sx={{ "& .Mui-selected": { backgroundColor: "#4B8D97", color: "#fff" } }}
                    />
                </Stack>
            )}
        </div>
    );
}







