package getawaygo_project.getawaygo_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Property {
    private long propertyId;
    private String name;
    private String address;
    private String town;
    private String country;
    private int nrOfRooms;
    private String description;
    private double price;
    private long userId;
    private List<String> photosUrls;
    private Boolean active;
}
