package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.GetAllPropertiesResponse;

public interface FilterPropertiesUseCase {
    GetAllPropertiesResponse getFilteredProperties(double maxPrice, String town);
}
