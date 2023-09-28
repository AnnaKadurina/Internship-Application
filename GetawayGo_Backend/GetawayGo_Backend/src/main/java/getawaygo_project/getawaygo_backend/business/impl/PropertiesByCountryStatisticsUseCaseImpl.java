package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.PropertiesByCountryStatisticsUseCase;
import getawaygo_project.getawaygo_backend.domain.PropertyByCountryStatisticsDTO;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PropertiesByCountryStatisticsUseCaseImpl implements PropertiesByCountryStatisticsUseCase {
    private PropertyRepository propertyRepository;

    @Override
    public List<PropertyByCountryStatisticsDTO> getPropertyStatistics() {
        List<PropertyEntity> properties = propertyRepository.findAll();
        Map<String, Long> propertyCountByCountry = new HashMap<>();

        for (PropertyEntity property : properties) {
            String country = property.getCountry();
            propertyCountByCountry.put(country, propertyCountByCountry.getOrDefault(country, 0L) + 1);
        }

        List<PropertyByCountryStatisticsDTO> propertyStatistics = new ArrayList<>();

        for (Map.Entry<String, Long> entry : propertyCountByCountry.entrySet()) {
            String country = entry.getKey();
            long propertyCount = entry.getValue();

            PropertyByCountryStatisticsDTO propertyStat = PropertyByCountryStatisticsDTO.builder()
                    .country(country)
                    .propertyCount(propertyCount)
                    .build();

            propertyStatistics.add(propertyStat);
        }

        propertyStatistics.sort(Comparator.comparingLong(PropertyByCountryStatisticsDTO::getPropertyCount).reversed());

        List<PropertyByCountryStatisticsDTO> top10Countries = propertyStatistics.stream()
                .limit(10)
                .toList();

        return top10Countries;
    }
}
