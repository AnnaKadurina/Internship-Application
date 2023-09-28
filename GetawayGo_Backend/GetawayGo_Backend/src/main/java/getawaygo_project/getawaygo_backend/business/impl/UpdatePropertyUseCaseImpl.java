package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.UpdatePropertyUseCase;
import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.domain.UpdatePropertyRequest;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdatePropertyUseCaseImpl implements UpdatePropertyUseCase {
    private PropertyRepository propertyRepository;

    @Override
    public void updateProperty(UpdatePropertyRequest updatePropertyRequest) {
        Optional<PropertyEntity> propertyUpdate = propertyRepository.findById(updatePropertyRequest.getId());
        if (propertyUpdate.isEmpty())
            throw new PropertyIsNotFoundException();
        else {
            PropertyEntity property = propertyUpdate.get();
            property.setName(updatePropertyRequest.getName());
            property.setAddress(updatePropertyRequest.getAddress());
            property.setTown(updatePropertyRequest.getTown());
            property.setCountry(updatePropertyRequest.getCountry());
            property.setNrOfRooms(updatePropertyRequest.getNrOfRooms());
            property.setDescription(updatePropertyRequest.getDescription());
            property.setPrice(updatePropertyRequest.getPrice());
            propertyRepository.save(property);

        }
    }
}
