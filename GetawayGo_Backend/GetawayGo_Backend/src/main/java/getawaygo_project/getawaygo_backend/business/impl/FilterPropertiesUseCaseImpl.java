package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.FilterPropertiesUseCase;
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
public class FilterPropertiesUseCaseImpl implements FilterPropertiesUseCase {
    private PropertyRepository propertyRepository;

    @Override
    public GetAllPropertiesResponse getFilteredProperties(double maxPrice, String town) {
        List<PropertyEntity> propertyEntities = propertyRepository.findByPriceAndTown(maxPrice, town);
        if (propertyEntities.isEmpty())
            throw new NoPropertiesFoundException();
        final GetAllPropertiesResponse response = new GetAllPropertiesResponse();
        List<Property> filteredProperties = propertyEntities.stream()
                .map(PropertyConverter::convert)
                .toList();

        response.setAllProperties(filteredProperties);
        return response;
    }
}
