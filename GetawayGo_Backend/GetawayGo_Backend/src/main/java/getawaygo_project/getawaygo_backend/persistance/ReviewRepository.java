package getawaygo_project.getawaygo_backend.persistance;

import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Optional<ReviewEntity> findById(Long id);
    List<ReviewEntity> findByPropertyId(PropertyEntity property);
    @Query("select avg(r.rating) from ReviewEntity r where r.propertyId.propertyId = :propertyId")
    Double getAverageRatingForProperty(@Param("propertyId") Long studentPcn);
    @Query("SELECT count(r) FROM ReviewEntity r WHERE r.propertyId.propertyId = :propertyId")
    Long countReviewsForProperty(@Param("propertyId") Long propertyId);

}
