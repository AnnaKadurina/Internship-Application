import React, { useState, useEffect } from 'react';
import PropertyCard from '../components/PropertyCard';
import { getAllPropertiesByCountry, getFilteredProperties, getHighestPrice } from '../api/propertyApis';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import AutoComplete from "react-google-autocomplete";
import GOOGLE_API_KEY from '../api/googleApi';
import Pagination from '@mui/material/Pagination';
import Stack from '@mui/material/Stack';
import Smile from '../images/smile.png';
import Box from '@mui/material/Box';
import Slider from '@mui/material/Slider';

export default function AllProperties() {
  const [cardData, setCardData] = useState([]);
  const [page, setPage] = useState(1);
  const [selectedCountry, setSelectedCountry] = useState('');
  const [selectedTown, setSelectedTown] = useState('');
  const [max, setMax] = useState('');
  const [maxSlider, setMaxSlider] = useState('');

  const getPropertiesByCountry = () => {
    if (!selectedCountry || !selectedTown) {
      getAllPropertiesByCountry(selectedCountry)
        .then((response) => {
          const allProperties = response.data.allProperties.map((property) => ({
            id: property.propertyId,
            ...property,
          }));
          setCardData(allProperties);
        })
        .catch((error) => {
          console.error(error);
        });
    } else {
      setCardData([]);

      getFilteredProperties(selectedTown, max)
        .then((response) => {
          const allProperties = response.data.allProperties.map((property) => ({
            id: property.propertyId,
            ...property,
          }));
          setCardData(allProperties);
        })
        .catch((error) => {
          console.error(error);
        });
    }
  };

  const handleMaxChange = (event, value) => {
    setMax(value);
  };

  const handleAutocompleteSelect = (place) => {
    const addressComponents = place.address_components;
    const townComponent = addressComponents.find((component) =>
      component.types.includes('locality')
    );
    const countryComponent = addressComponents.find((component) =>
      component.types.includes('country')
    );

    if (townComponent) {
      setSelectedTown(townComponent.long_name);
    }
    if (countryComponent) {
      setSelectedCountry(countryComponent.long_name);
    }
  };

  useEffect(() => {
    navigator.geolocation.getCurrentPosition(position => {
      const { latitude, longitude } = position.coords;

      /*in this part of the code, we get the country based on the user location with google api */

      fetch(`https://maps.googleapis.com/maps/api/geocode/json?latlng=${latitude},${longitude}&key=${GOOGLE_API_KEY}`)
        .then(response => response.json())
        .then(data => {
          console.log(data)
          const country = data.results[0].address_components.find(component => {
            return component.types.includes('country');
          }).long_name;
          getAllPropertiesByCountry(country)
            .then((response) => {
              const allPropertiesCountry = response.data.allProperties.map((property) => ({
                id: property.propertyId,
                ...property,
              }));
              setCardData(allPropertiesCountry);
            })
            .catch((error) => {
              console.error(error);
            });
        });
    });

    getHighestPrice().then((response) => {
      setMaxSlider(response.data)
      setMax(response.data)
    })

  }, []);

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '30px 0' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', marginRight: '20px' }}>
          <Typography variant="subtitle1" gutterBottom>
            Select your destination town:
          </Typography>
          <AutoComplete
            required
            id="address"
            label="Address"
            name="address"
            apiKey={GOOGLE_API_KEY}
            className='autoCompleteStyle'
            style={{ border: '1px solid black', borderRadius: '4px' }}
            onPlaceSelected={handleAutocompleteSelect}
          />
        </div>

        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', marginRight: '30px' }}>
          <Typography variant="subtitle1" gutterBottom style={{ marginRight: '20px' }}>
            Price range:
          </Typography>
          <Box width={300}>
            <Slider
              id="price"
              aria-labelledby="price-range"
              defaultValue={500}
              min={0}
              max={maxSlider}
              marks={[
                { value: 0, label: '0€' },
                { value: maxSlider, label: `${maxSlider}€` }
              ]}
              valueLabelDisplay="auto"
              style={{ color: '#4B8D97' }}
              onChange={handleMaxChange}
            />
          </Box>
        </div>

        <Button variant="contained" id="showListings" sx={{ backgroundColor: '#4B8D97', '&:hover': { backgroundColor: '#C3ACBA', }, }} onClick={getPropertiesByCountry}
        >Show listings</Button>

      </div>

      <div style={{ display: 'flex', justifyContent: 'center', flexWrap: 'wrap' }} id='propertyCard'>
        {cardData && cardData.length > 0 ? (
          cardData.slice((page - 1) * 12, page * 12).map((card) => (
            <PropertyCard
              key={card.id}
              id={card.id}
              title={card.name}
              images={card.photosUrls}
              town={card.town}
              country={card.country}
              price={card.price}
            />
          ))
        ) : (
          <Typography variant="body1" gutterBottom>
            There are no listings available at this location. We hope there are soon!
            <br />
            <img src={Smile} alt="" style={{ width: 300, height: 300, justifyContent: 'center', flexWrap: 'wrap', marginTop: 10 }} />
          </Typography>
        )}
      </div>
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column' }}>
        <Stack spacing={2} sx={{ my: 2, justifyContent: 'center', textAlign: 'center' }}>
          <Pagination
            count={Math.ceil(cardData.length / 12)}
            page={page}
            onChange={(event, value) => setPage(value)}
            sx={{ "& .Mui-selected": { backgroundColor: "#C3ACBA", color: "#fff" } }}
          />
        </Stack>
      </div>
    </div>

  );
}
