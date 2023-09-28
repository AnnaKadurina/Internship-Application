package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetPropertiesForHostUseCase;
import getawaygo_project.getawaygo_backend.business.exception.NoPropertiesFoundException;
import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.domain.GetAllPropertiesResponse;
import getawaygo_project.getawaygo_backend.domain.Property;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetPropertiesForHostUseCaseImpl implements GetPropertiesForHostUseCase {
    private PropertyRepository propertyRepository;
    private AccessToken accessToken;

    @Override
    public GetAllPropertiesResponse getPropertiesForHost(long id) {
        if (accessToken.getUserId() != id)
            throw new UnauthorizedDataException();
        List<PropertyEntity> properties = propertyRepository.findAll()
                .stream()
                .filter(PropertyEntity::getActive)
                .filter(property -> property.getUser().getUserId() == id)
                .toList();
        final GetAllPropertiesResponse response = new GetAllPropertiesResponse();
        List<Property> results = properties.stream()
                .map(PropertyConverter::convert)
                .toList();
        if (results.isEmpty())
            throw new NoPropertiesFoundException();
        else {
            response.setAllProperties(results);
            return response;
        }
    }
}
