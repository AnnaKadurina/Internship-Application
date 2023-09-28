package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.DeletePropertyUseCase;
import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DeletePropertyUseCaseImpl implements DeletePropertyUseCase {
    private PropertyRepository propertyRepository;

    @Override
    public void deleteProperty(long propertyId) {
        Optional<PropertyEntity> property = propertyRepository.findById(propertyId);
        if (property.isEmpty())
            throw new PropertyIsNotFoundException();
        else {
            PropertyEntity updateProperty = property.get();
            updateProperty.setActive(false);
            propertyRepository.save(updateProperty);
        }
    }
}
