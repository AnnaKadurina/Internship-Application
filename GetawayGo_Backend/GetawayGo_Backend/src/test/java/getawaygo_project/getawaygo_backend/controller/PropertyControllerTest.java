package getawaygo_project.getawaygo_backend.controller;

import getawaygo_project.getawaygo_backend.business.*;
import getawaygo_project.getawaygo_backend.business.exception.NoPropertiesFoundException;
import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.*;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PropertyControllerTest {
    @Mock
    private GetAllPropertiesUseCase getAllPropertiesUseCase;
    @Mock
    private GetPropertyUseCase getPropertyUseCase;
    @Mock
    private CreatePropertyUseCase createPropertyUseCase;
    @Mock
    private DeletePropertyUseCase deletePropertyUseCase;
    @Mock
    private UpdatePropertyUseCase updatePropertyUseCase;
    @Mock
    private GetPropertiesByCountryUseCase getPropertiesByCountryUseCase;
    @Mock
    private GetPropertiesForHostUseCase getPropertiesForHostUseCase;
    @Mock
    private UploadPicturesForPropertyUseCase uploadPicturesForPropertyUseCase;
    @Mock
    private GetHighestPropertyPrice getHighestPropertyPrice;
    @Mock
    private FilterPropertiesUseCase filterPropertiesUseCase;
    @InjectMocks
    private PropertyController propertyController;

    @Test
    void createProperty() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        CreatePropertyRequest request = CreatePropertyRequest.builder()
                .name("Test")
                .userId(user.getUserId())
                .build();
        Property property = Property.builder()
                .name("Test")
                .userId(user.getUserId())
                .build();
        CreatePropertyResponse response = CreatePropertyResponse.builder()
                .propertyResponse(property)
                .build();

        when(createPropertyUseCase.createProperty(request)).thenReturn(response);

        ResponseEntity<CreatePropertyResponse> createPropertyResponse = propertyController.createProperty(user.getUserId(), request);

        assertEquals(HttpStatus.CREATED, createPropertyResponse.getStatusCode());
        assertEquals(request.getName(), createPropertyResponse.getBody().getPropertyResponse().getName());
        verify(createPropertyUseCase).createProperty(request);

    }

    @Test
    void createPropertyUserNotFound() {
        Long id = 1L;

        CreatePropertyRequest request = CreatePropertyRequest.builder()
                .name("Test")
                .userId(id)
                .build();

        when(createPropertyUseCase.createProperty(request)).thenThrow(new UserNotFoundException());

        assertThrows(UserNotFoundException.class, () -> propertyController.createProperty(id, request));

        verify(createPropertyUseCase).createProperty(request);

    }

    @Test
    void getAllProperties() {
        List<Property> propertyList = new ArrayList<>();
        propertyList.add(new Property());
        propertyList.add(new Property());
        GetAllPropertiesResponse getResponse = new GetAllPropertiesResponse(propertyList);

        when(getAllPropertiesUseCase.getProperties()).thenReturn(getResponse);

        ResponseEntity<GetAllPropertiesResponse> properties = propertyController.getAllProperties();

        assertEquals(getResponse, properties.getBody());
        assertEquals(HttpStatus.OK, properties.getStatusCode());
        verify(getAllPropertiesUseCase).getProperties();
    }

    @Test
    void getAllPropertiesNoFound() {
        when(getAllPropertiesUseCase.getProperties()).thenThrow(new NoPropertiesFoundException());
        assertThrows(NoPropertiesFoundException.class, () -> propertyController.getAllProperties());

        verify(getAllPropertiesUseCase).getProperties();
    }

    @Test
    void getAllPropertiesByCountry() {
        List<Property> propertyList = new ArrayList<>();
        propertyList.add(new Property());
        propertyList.add(new Property());
        GetAllPropertiesResponse getResponse = new GetAllPropertiesResponse(propertyList);

        when(getPropertiesByCountryUseCase.getPropertiesByCountry("TestCountry")).thenReturn(getResponse);

        ResponseEntity<GetAllPropertiesResponse> properties = propertyController.getAllPropertiesByCountry("TestCountry");

        assertEquals(getResponse, properties.getBody());
        assertEquals(HttpStatus.OK, properties.getStatusCode());
        verify(getPropertiesByCountryUseCase).getPropertiesByCountry("TestCountry");
    }

    @Test
    void getFilteredProperties() {
        List<Property> propertyList = new ArrayList<>();
        propertyList.add(new Property());
        propertyList.add(new Property());
        GetAllPropertiesResponse getResponse = new GetAllPropertiesResponse(propertyList);

        when(filterPropertiesUseCase.getFilteredProperties(100, "Blg")).thenReturn(getResponse);

        ResponseEntity<GetAllPropertiesResponse> properties = propertyController.getFilteredProperties("Blg", 100);

        assertEquals(getResponse, properties.getBody());
        assertEquals(HttpStatus.OK, properties.getStatusCode());
        verify(filterPropertiesUseCase).getFilteredProperties(100, "Blg");
    }

    @Test
    void getHighestPropertyPrice() {
        Double max = 100.0;

        when(getHighestPropertyPrice.getHighestPropertyPrice()).thenReturn(max);

        ResponseEntity<Double> getMax = propertyController.getHighestPrice();

        assertEquals(max, getMax.getBody());
        assertEquals(HttpStatus.OK, getMax.getStatusCode());
        verify(getHighestPropertyPrice).getHighestPropertyPrice();
    }

    @Test
    void getAllPropertiesForHost() {
        List<Property> propertyList = new ArrayList<>();
        propertyList.add(new Property());
        propertyList.add(new Property());

        GetAllPropertiesResponse getResponse = new GetAllPropertiesResponse(propertyList);

        when(getPropertiesForHostUseCase.getPropertiesForHost(1)).thenReturn(getResponse);

        ResponseEntity<GetAllPropertiesResponse> properties = propertyController.getAllPropertiesForHost(1);

        assertEquals(getResponse, properties.getBody());
        assertEquals(HttpStatus.OK, properties.getStatusCode());
        verify(getPropertiesForHostUseCase).getPropertiesForHost(1);
    }

    @Test
    void getProperty() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        Property property = Property.builder()
                .propertyId(1)
                .name("Test")
                .userId(user.getUserId())
                .build();

        when(getPropertyUseCase.getProperty(property.getPropertyId())).thenReturn(Optional.of(property));

        ResponseEntity<Property> getProperty = propertyController.getProperty(property.getPropertyId());

        assertEquals(property, getProperty.getBody());
        assertEquals(getProperty.getBody().getPropertyId(), property.getPropertyId());
        assertEquals(HttpStatus.OK, getProperty.getStatusCode());
        verify(getPropertyUseCase).getProperty(property.getPropertyId());

    }

    @Test
    void getPropertyNotFound() {
        Long id = 1L;

        when(getPropertyUseCase.getProperty(id)).thenThrow(new PropertyIsNotFoundException());

        assertThrows(PropertyIsNotFoundException.class, () -> propertyController.getProperty(id));

        verify(getPropertyUseCase).getProperty(id);

    }

    @Test
    void deleteProperty() {
        UserEntity user = new UserEntity();
        user.setUserId(3);
        Property property = Property.builder()
                .propertyId(1)
                .name("Test")
                .userId(user.getUserId())
                .build();
        doNothing().when(deletePropertyUseCase).deleteProperty(property.getPropertyId());
        ResponseEntity<Void> deleteResult = propertyController.deleteProperty(property.getPropertyId());

        assertEquals(HttpStatus.NO_CONTENT, deleteResult.getStatusCode());
        verify(deletePropertyUseCase).deleteProperty(property.getPropertyId());
    }

    @Test
    void deletePropertyNotFound() {
        Long id = 1L;

        doThrow(new PropertyIsNotFoundException()).when(deletePropertyUseCase).deleteProperty(id);

        assertThrows(PropertyIsNotFoundException.class, () -> propertyController.deleteProperty(id));

        verify(deletePropertyUseCase).deleteProperty(id);
    }

    @Test
    void updateProperty() {
        UpdatePropertyRequest request = UpdatePropertyRequest.builder()
                .id(1)
                .name("Test")
                .build();

        doNothing().when(updatePropertyUseCase).updateProperty(request);
        ResponseEntity<Void> update = propertyController.updateProperty(request);

        assertEquals(HttpStatus.NO_CONTENT, update.getStatusCode());
        verify(updatePropertyUseCase).updateProperty(request);
    }

    @Test
    void updatePropertyNotFound() {
        UpdatePropertyRequest request = UpdatePropertyRequest.builder()
                .id(1)
                .name("Test")
                .build();

        doThrow(new PropertyIsNotFoundException()).when(updatePropertyUseCase).updateProperty(request);

        assertThrows(PropertyIsNotFoundException.class, () -> propertyController.updateProperty(request));

        verify(updatePropertyUseCase).updateProperty(request);
    }

    @Test
    void uploadPhotos() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1L);

        String fileName = "test.jpg";
        String content = "test content";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile photo = new MockMultipartFile(fileName, fileName, "image/jpg", bytes);

        List<MultipartFile> photos = Collections.singletonList(photo);

        doNothing().when(uploadPicturesForPropertyUseCase).uploadPictures(photos, property.getPropertyId());
        ResponseEntity<Void> uploadPicture = propertyController.uploadPhotos(photos, property.getPropertyId());

        assertEquals(HttpStatus.NO_CONTENT, uploadPicture.getStatusCode());
        verify(uploadPicturesForPropertyUseCase).uploadPictures(photos, property.getPropertyId());
    }
}