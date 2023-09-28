package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.GetAllBookingsResponse;

public interface GetBookingsForUserUseCase {
    GetAllBookingsResponse getBookingsForUser(long userId);
}
