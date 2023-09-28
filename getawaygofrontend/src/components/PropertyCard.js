import * as React from 'react';
import { styled } from '@mui/material/styles';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { useNavigate } from 'react-router-dom';
import { getProperty } from '../api/propertyApis';
import LocationOnIcon from '@mui/icons-material/LocationOn';
import { Carousel } from 'react-responsive-carousel';
import 'react-responsive-carousel/lib/styles/carousel.min.css';

const StyledCard = styled(Card)({
  maxWidth: 345,
  minHeight: '28rem',
  width: 350,
  margin: '1rem',
});

const StyledCarousel = styled(Carousel)({
  maxWidth: 345,
  minHeight: '15rem',
  width: 350,
});

export default function PropertyCard({ id, title, images, town, country, price }) {
  const navigate = useNavigate();

  const handleShowMore = () => {
    getProperty(id).then(response => {
      console.log(id, response.data)
      navigate(`/info/${id}`, { state: response.data });
    }).catch(error => {
      console.log(error);
      console.log(id)
    });
  }

  return (
    <StyledCard>
      <StyledCarousel>
        <Carousel showArrows={true} showThumbs={false}>
          {images.map((imageUrl, index) => (
            <div key={index}>
              <img src={imageUrl} alt={title} style={{ height: 220 }} />
            </div>
          ))}
        </Carousel>
      </StyledCarousel>

      <CardContent>
        <Typography gutterBottom variant="h5" component="div">
          {title}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          <LocationOnIcon sx={{ color: '#4B8D97' }} />
          {town}, {country} <br />
          {price}€ p/n
        </Typography>
      </CardContent>
      <Button
        id="showMore"
        variant="contained"
        sx={{
          backgroundColor: '#4B8D97',
          '&:hover': {
            backgroundColor: '#C3ACBA',
          },
        }}
        onClick={handleShowMore}
      >
        Show more
      </Button>
    </StyledCard>
  );
}