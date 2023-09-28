package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.PropertyByCountryStatisticsDTO;

import java.util.List;

public interface PropertiesByCountryStatisticsUseCase {
    List<PropertyByCountryStatisticsDTO> getPropertyStatistics();
}
