package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.Property;

import java.util.Optional;

public interface GetPropertyUseCase {
    Optional<Property> getProperty(long id);
}
