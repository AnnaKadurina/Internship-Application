package getawaygo_project.getawaygo_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private  String phone;
    private String photo;
    private String role;
    private Boolean active;

}
