package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.GetAllBookingsResponse;

public interface GetBookingsForPropertyUseCase {
    GetAllBookingsResponse getBookingsForProperty(long propertyId);
}
