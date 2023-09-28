package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetPropertiesByCountryUseCase;
import getawaygo_project.getawaygo_backend.business.exception.NoPropertiesFoundException;
import getawaygo_project.getawaygo_backend.domain.GetAllPropertiesResponse;
import getawaygo_project.getawaygo_backend.domain.Property;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class GetPropertiesByCountryUseCaseImpl implements GetPropertiesByCountryUseCase {
    private PropertyRepository propertyRepository;

    @Override
    public GetAllPropertiesResponse getPropertiesByCountry(String country) {
        List<PropertyEntity> properties = propertyRepository.findAll()
                .stream()
                .filter(PropertyEntity::getActive)
                .filter(property -> property.getCountry().equals(country))
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
