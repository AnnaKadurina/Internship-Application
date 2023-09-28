package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetPropertyUseCase;
import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.domain.Property;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GetPropertyUseCaseImpl implements GetPropertyUseCase {
    private PropertyRepository propertyRepository;

    @Override
    public Optional<Property> getProperty(long id) {
        Optional<Property> findProperty = propertyRepository.findById(id).map(PropertyConverter::convert);
        if (findProperty.isEmpty() || !findProperty.get().getActive())
            throw new PropertyIsNotFoundException();
        else
            return findProperty;
    }
}
