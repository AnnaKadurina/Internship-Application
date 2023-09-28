package getawaygo_project.getawaygo_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import getawaygo_project.getawaygo_backend.business.*;
import getawaygo_project.getawaygo_backend.business.exception.BookingNotFoundException;
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

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BookingControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CreateBookingUseCase createBookingUseCase;
    @MockBean
    private UpdateBookingUseCase updateBookingUseCase;
    @MockBean
    private DeleteBookingUseCase deleteBookingUseCase;
    @MockBean
    private GetBookingsForUserUseCase getBookingsForUserUseCase;
    @MockBean
    private GetBookingsForPropertyUseCase getBookingsForPropertyUseCase;
    @MockBean
    private GenerateBookingFileUseCase generateBookingFileUseCase;
    @MockBean
    private GetBookedDatesForPropertyUseCase getBookedDatesForPropertyUseCase;

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void createBookingReturns200() throws Exception {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        CreateBookingRequest request = CreateBookingRequest.builder()
                .userId(1)
                .propertyId(1)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        Booking booking = Booking.builder()
                .userId(1)
                .propertyId(1)
                .startDate(startDate)
                .endDate(endDate)
                .price(100)
                .build();
        CreateBookingResponse response = CreateBookingResponse.builder()
                .bookingResponse(booking)
                .build();

        when(createBookingUseCase.createBooking(request)).thenReturn(response);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE));

        verify(createBookingUseCase, times(1)).createBooking(request);

    }

    @Test
    @WithMockUser(username = "host", roles = {"HOST"})
    void createBookingReturns403() throws Exception {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        CreateBookingRequest request = CreateBookingRequest.builder()
                .userId(1)
                .propertyId(1)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isForbidden());

    }

    @Test
    void createBookingReturns401() throws Exception {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        CreateBookingRequest request = CreateBookingRequest.builder()
                .userId(1)
                .propertyId(1)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void getBookingsForUserReturns200() throws Exception {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        Booking booking = Booking.builder()
                .bookingId(1)
                .userId(1)
                .propertyId(1)
                .startDate(startDate)
                .endDate(endDate)
                .price(100)
                .build();

        GetAllBookingsResponse response = GetAllBookingsResponse.builder().allBookings(List.of(booking)).build();

        when(getBookingsForUserUseCase.getBookingsForUser(1)).thenReturn(response);

        mockMvc.perform(get("/bookings/user/{userId}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(""" 
                         {
                          "allBookings": [
                              {
                                  "bookingId": 1,
                                  "startDate": "2022-10-10T00:00:00Z",
                                  "endDate": "2022-10-20T00:00:00Z",
                                  "price": 100,
                                  "userId": 1,
                                  "propertyId": 1
                              }
                          ]
                        }"""));
        verify(getBookingsForUserUseCase, times(1)).getBookingsForUser(1);

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void getBookedDatesForPropertyReturns200() throws Exception {
        Instant bookedDate1 = Instant.parse("2022-10-10T00:00:00Z");
        Instant bookedDate2 = Instant.parse("2022-10-11T00:00:00Z");

        when(getBookedDatesForPropertyUseCase.getBookedDates(1)).thenReturn(List.of(bookedDate1, bookedDate2));

        mockMvc.perform(get("/bookings/booked/{propertyId}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE));

        verify(getBookedDatesForPropertyUseCase, times(1)).getBookedDates(1);
    }

    @Test
    @WithMockUser(username = "host", roles = {"HOST"})
    void getBookingsForUserReturns403() throws Exception {
        mockMvc.perform(get("/bookings/user/{userId}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    void getBookingsForUserReturns401() throws Exception {
        mockMvc.perform(get("/bookings/user/{userId}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "host", roles = {"HOST"})
    void getBookingsForPropertyReturns200() throws Exception {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        Booking booking = Booking.builder()
                .bookingId(1)
                .userId(1)
                .propertyId(1)
                .startDate(startDate)
                .endDate(endDate)
                .price(100)
                .build();

        GetAllBookingsResponse response = GetAllBookingsResponse.builder().allBookings(List.of(booking)).build();

        when(getBookingsForPropertyUseCase.getBookingsForProperty(1)).thenReturn(response);

        mockMvc.perform(get("/bookings/property/{propertyId}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(content().json(""" 
                         {
                          "allBookings": [
                              {
                                  "bookingId": 1,
                                  "startDate": "2022-10-10T00:00:00Z",
                                  "endDate": "2022-10-20T00:00:00Z",
                                  "price": 100,
                                  "userId": 1,
                                  "propertyId": 1
                              }
                          ]
                        }"""));
        verify(getBookingsForPropertyUseCase, times(1)).getBookingsForProperty(1);

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void getBookingsForPropertyReturns403() throws Exception {

        mockMvc.perform(get("/bookings/property/{propertyId}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isForbidden());

    }

    @Test
    void getBookingsForPropertyReturns401() throws Exception {

        mockMvc.perform(get("/bookings/property/{propertyId}", 1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void updateBookingReturns204() throws Exception {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        UpdateBookingRequest request = UpdateBookingRequest.builder()
                .bookingId(1)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isNoContent());

        verify(updateBookingUseCase, times(1)).updateBooking(request);

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void updateBookingReturns404() throws Exception {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        UpdateBookingRequest request = UpdateBookingRequest.builder()
                .bookingId(1)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        doThrow(new BookingNotFoundException()).when(updateBookingUseCase).updateBooking(request);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isNotFound());

        verify(updateBookingUseCase, times(1)).updateBooking(request);

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateBookingReturns403() throws Exception {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        UpdateBookingRequest request = UpdateBookingRequest.builder()
                .bookingId(1)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJSON = ow.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJSON))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void deleteBookingReturns204() throws Exception {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        Booking booking = Booking.builder()
                .bookingId(1)
                .userId(1)
                .propertyId(1)
                .startDate(startDate)
                .endDate(endDate)
                .price(100)
                .build();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/bookings/{bookingId}", booking.getBookingId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(deleteBookingUseCase, times(1)).deleteBooking(booking.getBookingId());

    }

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void deleteBookingReturns404() throws Exception {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        Booking booking = Booking.builder()
                .bookingId(1)
                .userId(1)
                .propertyId(1)
                .startDate(startDate)
                .endDate(endDate)
                .price(100)
                .build();

        doThrow(new BookingNotFoundException()).when(deleteBookingUseCase).deleteBooking(booking.getBookingId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/bookings/{bookingId}", booking.getBookingId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(deleteBookingUseCase, times(1)).deleteBooking(booking.getBookingId());

    }

    @Test
    void deleteBookingReturns401() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/bookings/{bookingId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteBookingReturns403() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/bookings/{bookingId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    @Test
    void generateFileReturns200() throws Exception {
        Long bookingId = 1L;
        String url = "url";

        when(generateBookingFileUseCase.generateFile(bookingId)).thenReturn(url);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/bookings/confirmation/{id}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", containsString("text/plain")));

        verify(generateBookingFileUseCase, times(1)).generateFile(bookingId);

    }

}