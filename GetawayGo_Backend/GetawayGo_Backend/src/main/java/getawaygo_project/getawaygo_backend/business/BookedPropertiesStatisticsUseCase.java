package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.BookedPropertyStatisticsDTO;

import java.util.List;

public interface BookedPropertiesStatisticsUseCase {
    List<BookedPropertyStatisticsDTO> getBookedPropertiesStatistics(long hostId);
}
