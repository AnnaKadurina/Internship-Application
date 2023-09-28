package getawaygo_project.getawaygo_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePropertyRequest {
    @NotNull
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotBlank
    private String town;
    @NotBlank
    private String country;
    @NotNull
    private int nrOfRooms;
    @NotBlank
    private String description;
    @NotNull
    private double price;
}
