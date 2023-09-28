package getawaygo_project.getawaygo_backend.controller;

import getawaygo_project.getawaygo_backend.business.BookedPropertiesStatisticsUseCase;
import getawaygo_project.getawaygo_backend.business.PropertiesByCountryStatisticsUseCase;
import getawaygo_project.getawaygo_backend.business.ReviewStatisticsUseCase;
import getawaygo_project.getawaygo_backend.business.UserRoleStatisticsUseCase;
import getawaygo_project.getawaygo_backend.configuration.isauthenticated.IsAuthenticated;
import getawaygo_project.getawaygo_backend.domain.BookedPropertyStatisticsDTO;
import getawaygo_project.getawaygo_backend.domain.PropertyByCountryStatisticsDTO;
import getawaygo_project.getawaygo_backend.domain.ReviewStatisticsDTO;
import getawaygo_project.getawaygo_backend.domain.UserStatisticsDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class StatisticsController {
    private UserRoleStatisticsUseCase userRoleStatisticsUseCase;
    private PropertiesByCountryStatisticsUseCase propertiesByCountryStatisticsUseCase;
    private ReviewStatisticsUseCase reviewStatisticsUseCase;
    private BookedPropertiesStatisticsUseCase bookedPropertiesStatisticsUseCase;

    @IsAuthenticated
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("users")
    public ResponseEntity<UserStatisticsDTO> getUserRoleStatistics() {
        final UserStatisticsDTO roleStatistics = userRoleStatisticsUseCase.getRoleStatistics();
        return ResponseEntity.ok().body(roleStatistics);
    }

    @IsAuthenticated
    @RolesAllowed("ROLE_ADMIN")
    @GetMapping("properties")
    public ResponseEntity<List<PropertyByCountryStatisticsDTO>> getPropertiesByCountryStatistics() {
        final List<PropertyByCountryStatisticsDTO> top10Countries = propertiesByCountryStatisticsUseCase.getPropertyStatistics();
        return ResponseEntity.ok().body(top10Countries);
    }

    @IsAuthenticated
    @RolesAllowed("ROLE_HOST")
    @GetMapping("reviews/{hostId}")
    public ResponseEntity<ReviewStatisticsDTO> getReviewStatistics(@PathVariable(value = "hostId") long hostId) {
        final ReviewStatisticsDTO reviewStatistics = reviewStatisticsUseCase.getReviewStatistics(hostId);
        return ResponseEntity.ok().body(reviewStatistics);
    }

    @IsAuthenticated
    @RolesAllowed("ROLE_HOST")
    @GetMapping("booked/properties/{hostId}")
    public ResponseEntity<List<BookedPropertyStatisticsDTO>> getBookedPropertiesStatistics(@PathVariable(value = "hostId") long hostId) {
        final List<BookedPropertyStatisticsDTO> top10Properties = bookedPropertiesStatisticsUseCase.getBookedPropertiesStatistics(hostId);
        return ResponseEntity.ok().body(top10Properties);
    }
}
