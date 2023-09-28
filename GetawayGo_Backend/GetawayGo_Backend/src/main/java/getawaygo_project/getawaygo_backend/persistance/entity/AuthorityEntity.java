package getawaygo_project.getawaygo_backend.persistance.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Table(name = "ALL_ROLES")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class AuthorityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleId;

    @Column(name = "ROLE")
    private String role;

    @OneToMany(mappedBy = "authority")
    private List<UserEntity> users;

}
