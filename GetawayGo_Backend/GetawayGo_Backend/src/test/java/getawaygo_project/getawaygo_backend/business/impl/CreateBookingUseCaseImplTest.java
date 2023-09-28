package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetBookedDatesForPropertyUseCase;
import getawaygo_project.getawaygo_backend.business.exception.BookedDatesException;
import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.CreateBookingRequest;
import getawaygo_project.getawaygo_backend.domain.CreateBookingResponse;
import getawaygo_project.getawaygo_backend.persistance.BookingRepository;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateBookingUseCaseImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GetBookedDatesForPropertyUseCase getBookedDatesForPropertyUseCase;
    @Mock
    private BookingRepository bookingRepository;
    @InjectMocks
    private CreateBookingUseCaseImpl createBookingUseCase;

    @Test
    void createBooking() {

        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        UserEntity user = new UserEntity();
        user.setUserId(3);

        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        property.setPrice(100);

        CreateBookingRequest request = CreateBookingRequest.builder()
                .userId(user.getUserId())
                .propertyId(property.getPropertyId())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(propertyRepository.findById(property.getPropertyId())).thenReturn(Optional.of(property));
        when(getBookedDatesForPropertyUseCase.getBookedDates(property.getPropertyId())).thenReturn(Collections.emptyList());

        CreateBookingResponse response = createBookingUseCase.createBooking(request);

        assertEquals(request.getStartDate(), response.getBookingResponse().getStartDate());
        assertEquals(request.getEndDate(), response.getBookingResponse().getEndDate());

        verify(userRepository).findById(user.getUserId());
        verify(propertyRepository).findById(property.getPropertyId());
        verify(getBookedDatesForPropertyUseCase).getBookedDates(property.getPropertyId());
    }
    @Test
    void createBookingStartDateIsAfterEndDate() {
        Instant startDate = Instant.parse("2022-10-20T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-10T00:00:00Z");

        UserEntity user = new UserEntity();
        user.setUserId(3);

        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        property.setPrice(100);

        CreateBookingRequest request = CreateBookingRequest.builder()
                .userId(user.getUserId())
                .propertyId(property.getPropertyId())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(propertyRepository.findById(property.getPropertyId())).thenReturn(Optional.of(property));

        assertThrows(BookedDatesException.class, () -> createBookingUseCase.createBooking(request));

        verify(userRepository).findById(user.getUserId());
        verify(propertyRepository).findById(property.getPropertyId());
    }

    @Test
    void createBookingDatesAreBooked() {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant inBetweenDate = Instant.parse("2022-10-11T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-12T00:00:00Z");

        UserEntity user = new UserEntity();
        user.setUserId(3);

        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        property.setPrice(100);

        CreateBookingRequest request = CreateBookingRequest.builder()
                .userId(user.getUserId())
                .propertyId(property.getPropertyId())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(propertyRepository.findById(property.getPropertyId())).thenReturn(Optional.of(property));
        when(getBookedDatesForPropertyUseCase.getBookedDates(property.getPropertyId())).thenReturn(List.of(startDate, inBetweenDate, endDate));

        assertThrows(BookedDatesException.class, () -> createBookingUseCase.createBooking(request));

        verify(userRepository).findById(user.getUserId());
        verify(propertyRepository).findById(property.getPropertyId());
        verify(getBookedDatesForPropertyUseCase).getBookedDates(property.getPropertyId());
    }
    @Test
    void createBookingUserNotFound() {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        UserEntity user = new UserEntity();
        user.setUserId(3);

        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        property.setPrice(100);

        CreateBookingRequest request = CreateBookingRequest.builder()
                .userId(user.getUserId())
                .propertyId(property.getPropertyId())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> createBookingUseCase.createBooking(request));

        verify(userRepository).findById(user.getUserId());
    }

    @Test
    void createBookingPropertyNotFound() {
        Instant startDate = Instant.parse("2022-10-10T00:00:00Z");
        Instant endDate = Instant.parse("2022-10-20T00:00:00Z");

        UserEntity user = new UserEntity();
        user.setUserId(3);

        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        property.setPrice(100);

        CreateBookingRequest request = CreateBookingRequest.builder()
                .userId(user.getUserId())
                .propertyId(property.getPropertyId())
                .startDate(startDate)
                .endDate(endDate)
                .build();

        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(propertyRepository.findById(property.getPropertyId())).thenReturn(Optional.empty());

        assertThrows(PropertyIsNotFoundException.class, () -> createBookingUseCase.createBooking(request));

        verify(userRepository).findById(user.getUserId());
        verify(propertyRepository).findById(property.getPropertyId());

    }

}