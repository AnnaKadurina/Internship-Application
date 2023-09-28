package getawaygo_project.getawaygo_backend.persistance;

import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, Long> {
    Optional<PropertyEntity> findById(Long id);

    List<PropertyEntity> findByUser(UserEntity user);

    @Query("SELECT p FROM PropertyEntity p WHERE p.town = :town AND p.price <= :max")
    List<PropertyEntity> findByPriceAndTown(@Param("max") double max, @Param("town") String town);

    @Query("SELECT MAX(p.price) FROM PropertyEntity p")
    double findHighestPrice();
}
