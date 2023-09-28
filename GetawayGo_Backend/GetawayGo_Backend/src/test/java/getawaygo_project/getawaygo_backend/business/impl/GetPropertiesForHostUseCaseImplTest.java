package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.NoPropertiesFoundException;
import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.GetAllPropertiesResponse;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPropertiesForHostUseCaseImplTest {
    @Mock
    private PropertyRepository propertyRepository;
    @Mock
    private AccessToken accessToken;
    @InjectMocks
    private GetPropertiesForHostUseCaseImpl getPropertiesForHostUseCase;
    @Test
    void getPropertiesForHost() {
        UserEntity user = new UserEntity();
        user.setUserId(1);

        PropertyEntity property1 = new PropertyEntity();
        property1.setPropertyId(1);
        property1.setName("Test");
        property1.setActive(true);
        property1.setUser(user);

        PropertyEntity property2 = new PropertyEntity();
        property2.setPropertyId(2);
        property2.setName("Test2");
        property2.setActive(false);
        property2.setUser(user);

        List<PropertyEntity> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);

        when(accessToken.getUserId()).thenReturn(user.getUserId());
        when(propertyRepository.findAll()).thenReturn(properties);

        GetAllPropertiesResponse response = getPropertiesForHostUseCase.getPropertiesForHost(1);

        assertEquals(1, response.getAllProperties().size());

        verify(propertyRepository).findAll();
        verify(accessToken).getUserId();
    }
    @Test
    void getPropertiesForHostNotFound() {
        Long id = 1L;

        PropertyEntity property1 = new PropertyEntity();
        property1.setPropertyId(1);
        property1.setName("Test");
        property1.setActive(true);
        property1.setUser(new UserEntity());

        PropertyEntity property2 = new PropertyEntity();
        property2.setPropertyId(2);
        property2.setName("Test2");
        property2.setActive(true);
        property2.setUser(new UserEntity());

        List<PropertyEntity> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);

        when(accessToken.getUserId()).thenReturn(id);
        when(propertyRepository.findAll()).thenReturn(properties);

        assertThrows(NoPropertiesFoundException.class, () -> getPropertiesForHostUseCase.getPropertiesForHost(1L));

        verify(propertyRepository).findAll();
        verify(accessToken).getUserId();
    }
    @Test
    void getPropertiesForHostNotAuthorized() {
        Long id = 1L;
        Long wrongId = 2L;

        when(accessToken.getUserId()).thenReturn(wrongId);

        assertThrows(UnauthorizedDataException.class, () -> getPropertiesForHostUseCase.getPropertiesForHost(id));

        verify(accessToken).getUserId();
    }
}