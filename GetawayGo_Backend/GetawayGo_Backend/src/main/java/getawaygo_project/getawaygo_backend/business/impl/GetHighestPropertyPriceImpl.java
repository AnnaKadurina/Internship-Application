package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetHighestPropertyPrice;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetHighestPropertyPriceImpl implements GetHighestPropertyPrice {
    private PropertyRepository propertyRepository;

    @Override
    public Double getHighestPropertyPrice() {
        Double max = propertyRepository.findHighestPrice();
        return max;
    }
}
