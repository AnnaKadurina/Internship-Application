package getawaygo_project.getawaygo_backend.persistance;

import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername (String username);
    UserEntity findByEmail (String email);

}
