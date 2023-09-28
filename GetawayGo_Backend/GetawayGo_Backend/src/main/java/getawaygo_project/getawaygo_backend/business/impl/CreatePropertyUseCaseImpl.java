package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.CreatePropertyUseCase;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.CreatePropertyRequest;
import getawaygo_project.getawaygo_backend.domain.CreatePropertyResponse;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CreatePropertyUseCaseImpl implements CreatePropertyUseCase {
    private PropertyRepository propertyRepository;
    private UserRepository userRepository;
    @Override
    public CreatePropertyResponse createProperty(CreatePropertyRequest propertyRequest) {
        Optional<UserEntity> user = userRepository.findById(propertyRequest.getUserId());
        if (user.isEmpty())
            throw new UserNotFoundException();

        PropertyEntity createdProperty = new PropertyEntity();
        createdProperty.setName(propertyRequest.getName());
        createdProperty.setAddress(propertyRequest.getAddress());
        createdProperty.setTown(propertyRequest.getTown());
        createdProperty.setCountry(propertyRequest.getCountry());
        createdProperty.setNrOfRooms(propertyRequest.getNrOfRooms());
        createdProperty.setDescription(propertyRequest.getDescription());
        createdProperty.setPrice(propertyRequest.getPrice());
        createdProperty.setUser(user.get());
        createdProperty.setActive(true);

        propertyRepository.save(createdProperty);
        return CreatePropertyResponse.builder()
                .propertyResponse(PropertyConverter.convert(createdProperty))
                .build();
    }
}
