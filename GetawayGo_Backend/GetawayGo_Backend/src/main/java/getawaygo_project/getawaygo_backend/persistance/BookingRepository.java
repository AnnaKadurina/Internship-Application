package getawaygo_project.getawaygo_backend.persistance;

import getawaygo_project.getawaygo_backend.persistance.entity.BookingEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    Optional<BookingEntity> findById(Long id);

    List<BookingEntity> findByPropertyId(PropertyEntity property);
}

