package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.domain.Property;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyPhotoEntity;

import java.util.ArrayList;
import java.util.List;
public class PropertyConverter {
    private PropertyConverter(){}
    public static Property convert(PropertyEntity property){
        List<String> photoUrls = new ArrayList<>();
        if (!property.getPhotos().isEmpty()){
            for (PropertyPhotoEntity photoEntity : property.getPhotos()) {
                photoUrls.add(photoEntity.getUrl());
            }
        }
        return Property.builder()
                .propertyId(property.getPropertyId())
                .name(property.getName())
                .address(property.getAddress())
                .town(property.getTown())
                .country(property.getCountry())
                .nrOfRooms(property.getNrOfRooms())
                .description(property.getDescription())
                .price(property.getPrice())
                .photosUrls(photoUrls)
                .userId(property.getUser().getUserId())
                .active(property.getActive())
                .build();
    }
}

