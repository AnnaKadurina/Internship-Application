package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.BookedPropertyStatisticsDTO;
import getawaygo_project.getawaygo_backend.persistance.BookingRepository;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.BookingEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookedPropertiesStatisticsUseCaseImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AccessToken accessToken;
    @InjectMocks
    private BookedPropertiesStatisticsUseCaseImpl bookedPropertiesStatisticsUseCase;

    @Test
    void getBookedPropertiesStatistics() {
        UserEntity user = new UserEntity();
        user.setUserId(1);

        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);
        property.setName("best");
        property.setUser(user);

        PropertyEntity property3 = new PropertyEntity();
        property3.setPropertyId(3);
        property3.setName("best3");
        property3.setUser(user);

        BookingEntity booking = new BookingEntity();
        booking.setBookingId(1);
        booking.setPropertyId(property);

        BookingEntity booking3 = new BookingEntity();
        booking3.setBookingId(3);
        booking3.setPropertyId(property3);

        when(propertyRepository.findByUser(user)).thenReturn(List.of(property, property3));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(bookingRepository.findByPropertyId(property)).thenReturn(List.of(booking));
        when(bookingRepository.findByPropertyId(property3)).thenReturn(List.of(booking3));
        when(accessToken.getUserId()).thenReturn(user.getUserId());

        List<BookedPropertyStatisticsDTO> statisticsDTO = bookedPropertiesStatisticsUseCase.getBookedPropertiesStatistics(user.getUserId());

        assertEquals("best", statisticsDTO.get(0).getPropertyName());
        assertEquals(1, statisticsDTO.get(0).getBookingCount());

        verify(propertyRepository, times(1)).findByUser(user);
        verify(userRepository, times(1)).findById(user.getUserId());
        verify(bookingRepository, times(1)).findByPropertyId(property);
        verify(bookingRepository, times(1)).findByPropertyId(property3);
        verify(accessToken, times(1)).getUserId();
    }

    @Test
    void getBookedPropertiesStatisticsUnauthorized() {
        Long id = 1L;

        when(accessToken.getUserId()).thenReturn(2L);

        assertThrows(UnauthorizedDataException.class, () -> bookedPropertiesStatisticsUseCase.getBookedPropertiesStatistics(id));

        verify(accessToken).getUserId();
    }
    @Test
    void getBookedPropertiesStatisticsUserNotFound() {
        Long id = 1L;

        when(accessToken.getUserId()).thenReturn(id);
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> bookedPropertiesStatisticsUseCase.getBookedPropertiesStatistics(id));

        verify(userRepository, times(1)).findById(id);
        verify(accessToken, times(1)).getUserId();
    }
}