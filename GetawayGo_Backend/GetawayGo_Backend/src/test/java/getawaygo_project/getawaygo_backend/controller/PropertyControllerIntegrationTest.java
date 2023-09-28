package getawaygo_project.getawaygo_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import getawaygo_project.getawaygo_backend.business.*;
import getawaygo_project.getawaygo_backend.business.exception.NoPropertiesFoundException;
import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PropertyControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CreatePropertyUseCase createPropertyUseCase;
    @MockBean
    private UpdatePropertyUseCase updatePropertyUseCase;
    @MockBean
    private DeletePropertyUseCase deletePropertyUseCase;
    @MockBean
    private GetAllPropertiesUseCase getAllPropertiesUseCase;
    @MockBean
    private GetPropertyUseCase getPropertyUseCase;
    @MockBean
    private GetPropertiesByCountryUseCase getPropertiesByCountryUseCase;
    @MockBean
    private GetPropertiesForHostUseCase getPropertiesForHostUseCase;
    @MockBean
    private GetHighestPropertyPrice getHighestPropertyPrice;
    @MockBean
    private FilterPropertiesUseCase filterPropertiesUseCase;

    @Test
    @WithMockUser(username = "host", roles = {"HOST"})
    void createPropertyReturns201() throws Exception {
        CreatePropertyRequest request = CreatePropertyRequest.builder()
                .userId(1)
                .town("blg")
                .country("bg")
                .address("Ulica 1")
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();

        Property property = Property.builder()
                .propertyId(1)
                .userId(1)
                .town("blg")
                .country("bg")
                .address("Ulica 1")
                .photosUrls(new ArrayList<>())
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();

        CreatePropertyResponse response = CreatePropertyResponse.builder().propertyResponse(property).build();

        when(createPropertyUseCase.createProperty(request)).thenReturn(response);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/properties/{userId}", property.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE));

        verify(createPropertyUseCase, times(1)).createProperty(request);

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void createPropertyReturns403() throws Exception {
        CreatePropertyRequest request = CreatePropertyRequest.builder()
                .userId(1)
                .town("blg")
                .country("bg")
                .address("Ulica 1")
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();

        Property property = Property.builder()
                .propertyId(1)
                .userId(1)
                .town("blg")
                .country("bg")
                .address("Ulica 1")
                .photosUrls(new ArrayList<>())
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/properties/{userId}", property.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isForbidden());

    }

    @Test
    void createPropertyReturns401() throws Exception {
        CreatePropertyRequest request = CreatePropertyRequest.builder()
                .userId(1)
                .town("blg")
                .country("bg")
                .address("Ulica 1")
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();

        Property property = Property.builder()
                .propertyId(1)
                .userId(1)
                .town("blg")
                .country("bg")
                .address("Ulica 1")
                .photosUrls(new ArrayList<>())
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/properties/{userId}", property.getUserId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "host", roles = {"HOST"})
    void createPropertyReturns400() throws Exception {
        CreatePropertyRequest request = CreatePropertyRequest.builder().build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/properties/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllPropertiesReturns200() throws Exception {
        Property property = Property.builder()
                .propertyId(1)
                .userId(1)
                .town("blg")
                .country("bg")
                .address("Ulica 1")
                .photosUrls(new ArrayList<>())
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();


        GetAllPropertiesResponse response = GetAllPropertiesResponse.builder().allProperties(List.of(property)).build();

        when(getAllPropertiesUseCase.getProperties()).thenReturn(response);

        mockMvc.perform(get("/properties").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(""" 
                         {
                          "allProperties": [
                              {
                                  "propertyId": 1,
                                  "name": "Testy",
                                  "address": "Ulica 1",
                                  "town": "blg",
                                  "country": "bg",
                                  "nrOfRooms": 1,
                                  "description": "text",
                                  "price": 1,
                                  "userId": 1,
                                  "photosUrls": []
                              }
                          ]
                        }"""));
        verify(getAllPropertiesUseCase, times(1)).getProperties();

    }

    @Test
    void getAllPropertiesReturns404() throws Exception {
        doThrow(new NoPropertiesFoundException()).when(getAllPropertiesUseCase).getProperties();

        mockMvc.perform(get("/properties").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound());

        verify(getAllPropertiesUseCase, times(1)).getProperties();
    }

    @Test
    void getAllPropertiesByCountryReturns200() throws Exception {
        Property property = Property.builder()
                .propertyId(1)
                .userId(1)
                .town("blg")
                .country("bg")
                .address("Ulica 1")
                .photosUrls(new ArrayList<>())
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();


        GetAllPropertiesResponse response = GetAllPropertiesResponse.builder().allProperties(List.of(property)).build();

        when(getPropertiesByCountryUseCase.getPropertiesByCountry("bg")).thenReturn(response);


        mockMvc.perform(get("/properties/country/{country}", "bg").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(""" 
                         {
                          "allProperties": [
                              {
                                  "propertyId": 1,
                                  "name": "Testy",
                                  "address": "Ulica 1",
                                  "town": "blg",
                                  "country": "bg",
                                  "nrOfRooms": 1,
                                  "description": "text",
                                  "price": 1,
                                  "userId": 1,
                                  "photosUrls": []
                              }
                          ]
                        }"""));
        verify(getPropertiesByCountryUseCase, times(1)).getPropertiesByCountry("bg");

    }

    @Test
    void getFilteredPropertiesReturns200() throws Exception {
        Property property = Property.builder()
                .propertyId(1)
                .userId(1)
                .town("blg")
                .country("bg")
                .address("Ulica 1")
                .photosUrls(new ArrayList<>())
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();

        GetAllPropertiesResponse response = GetAllPropertiesResponse.builder().allProperties(List.of(property)).build();

        when(filterPropertiesUseCase.getFilteredProperties(100, "blg")).thenReturn(response);

        mockMvc.perform(get("/properties/{town}/{max}", "blg", 100).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(""" 
                         {
                          "allProperties": [
                              {
                                  "propertyId": 1,
                                  "name": "Testy",
                                  "address": "Ulica 1",
                                  "town": "blg",
                                  "country": "bg",
                                  "nrOfRooms": 1,
                                  "description": "text",
                                  "price": 1,
                                  "userId": 1,
                                  "photosUrls": []
                              }
                          ]
                        }"""));
        verify(filterPropertiesUseCase, times(1)).getFilteredProperties(100, "blg");

    }

    @Test
    void getHighestPropertyPrice() throws Exception {
        Double max = 100.0;

        when(getHighestPropertyPrice.getHighestPropertyPrice()).thenReturn(max);

        mockMvc.perform(get("/properties/max").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE));

        verify(getHighestPropertyPrice, times(1)).getHighestPropertyPrice();

    }

    @Test
    void getAllPropertiesByCountryReturns404() throws Exception {
        doThrow(new NoPropertiesFoundException()).when(getPropertiesByCountryUseCase).getPropertiesByCountry("bg");

        mockMvc.perform(get("/properties/country/{country}", "bg").contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound());

        verify(getPropertiesByCountryUseCase, times(1)).getPropertiesByCountry("bg");
    }

    @Test
    @WithMockUser(username = "host", roles = {"HOST"})
    void getAllPropertiesForHostReturns200() throws Exception {
        Property property = Property.builder()
                .propertyId(1)
                .userId(1)
                .town("blg")
                .country("bg")
                .address("Ulica 1")
                .photosUrls(new ArrayList<>())
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();


        GetAllPropertiesResponse response = GetAllPropertiesResponse.builder().allProperties(List.of(property)).build();

        when(getPropertiesForHostUseCase.getPropertiesForHost(1)).thenReturn(response);

        mockMvc.perform(get("/properties/host/{id}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(""" 
                         {
                          "allProperties": [
                              {
                                  "propertyId": 1,
                                  "name": "Testy",
                                  "address": "Ulica 1",
                                  "town": "blg",
                                  "country": "bg",
                                  "nrOfRooms": 1,
                                  "description": "text",
                                  "price": 1,
                                  "userId": 1,
                                  "photosUrls": []
                              }
                          ]
                        }"""));
        verify(getPropertiesForHostUseCase, times(1)).getPropertiesForHost(1);

    }

    @Test
    @WithMockUser(username = "host", roles = {"HOST"})
    void getAllPropertiesForHostReturns404() throws Exception {
        doThrow(new NoPropertiesFoundException()).when(getPropertiesForHostUseCase).getPropertiesForHost(1);

        mockMvc.perform(get("/properties/host/{id}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound());

        verify(getPropertiesForHostUseCase, times(1)).getPropertiesForHost(1);
    }

    @Test
    void getAllPropertiesForHostReturns401() throws Exception {
        mockMvc.perform(get("/properties/host/{id}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getPropertyReturns200() throws Exception {
        Property property = Property.builder()
                .propertyId(1)
                .userId(1)
                .town("blg")
                .country("bg")
                .address("Ulica 1")
                .photosUrls(new ArrayList<>())
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();


        when(getPropertyUseCase.getProperty(property.getPropertyId())).thenReturn(Optional.of(property));


        mockMvc.perform(get("/properties/{id}", property.getPropertyId()).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(""" 
                              {
                                  "propertyId": 1,
                                  "name": "Testy",
                                  "address": "Ulica 1",
                                  "town": "blg",
                                  "country": "bg",
                                  "nrOfRooms": 1,
                                  "description": "text",
                                  "price": 1,
                                  "userId": 1,
                                  "photosUrls": []
                              }
                        """));
        verify(getPropertyUseCase, times(1)).getProperty(property.getPropertyId());

    }

    @Test
    void getPropertyReturns404() throws Exception {
        Long id = 1L;

        when(getPropertyUseCase.getProperty(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/properties/{id}", id).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound());

        verify(getPropertyUseCase, times(1)).getProperty(id);

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deletePropertyReturns204() throws Exception {
        Property property = Property.builder()
                .propertyId(1)
                .name("Test")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/properties/{propertyId}", property.getPropertyId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(deletePropertyUseCase, times(1)).deleteProperty(property.getPropertyId());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deletePropertyReturns404() throws Exception {
        Property property = Property.builder()
                .propertyId(1)
                .name("Test")
                .build();

        doThrow(new PropertyIsNotFoundException()).when(deletePropertyUseCase).deleteProperty(property.getPropertyId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/properties/{propertyId}", property.getPropertyId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(deletePropertyUseCase, times(1)).deleteProperty(property.getPropertyId());
    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void deletePropertyReturns403() throws Exception {
        Property property = Property.builder()
                .propertyId(1)
                .name("Test")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/properties/{propertyId}", property.getPropertyId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    void deletePropertyReturns401() throws Exception {
        Property property = Property.builder()
                .propertyId(1)
                .name("Test")
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/properties/{propertyId}", property.getPropertyId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "host", roles = {"HOST"})
    void updatePropertyReturns204() throws Exception {
        UpdatePropertyRequest request = UpdatePropertyRequest.builder()
                .id(1)
                .town("blg2")
                .country("bg")
                .address("Ulica 1")
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isNoContent());

        verify(updatePropertyUseCase, times(1)).updateProperty(request);

    }

    @Test
    @WithMockUser(username = "host", roles = {"HOST"})
    void updatePropertyReturns404() throws Exception {
        UpdatePropertyRequest request = UpdatePropertyRequest.builder()
                .id(1)
                .town("blg2")
                .country("bg")
                .address("Ulica 1")
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();

        doThrow(new PropertyIsNotFoundException()).when(updatePropertyUseCase).updateProperty(request);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isNotFound());

        verify(updatePropertyUseCase, times(1)).updateProperty(request);
    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void updatePropertyReturns403() throws Exception {
        UpdatePropertyRequest request = UpdatePropertyRequest.builder()
                .id(1)
                .town("blg2")
                .country("bg")
                .address("Ulica 1")
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isForbidden());

    }

    @Test
    void updatePropertyReturns401() throws Exception {
        UpdatePropertyRequest request = UpdatePropertyRequest.builder()
                .id(1)
                .town("blg2")
                .country("bg")
                .address("Ulica 1")
                .price(1)
                .description("text")
                .nrOfRooms(1)
                .name("Testy")
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isUnauthorized());

    }
}