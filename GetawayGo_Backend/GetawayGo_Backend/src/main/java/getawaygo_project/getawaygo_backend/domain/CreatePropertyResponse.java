package getawaygo_project.getawaygo_backend.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePropertyResponse {
    private Property propertyResponse;
}
