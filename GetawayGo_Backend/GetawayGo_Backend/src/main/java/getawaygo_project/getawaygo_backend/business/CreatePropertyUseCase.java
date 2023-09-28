package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.CreatePropertyRequest;
import getawaygo_project.getawaygo_backend.domain.CreatePropertyResponse;

public interface CreatePropertyUseCase {
    CreatePropertyResponse createProperty(CreatePropertyRequest propertyRequest);
}
