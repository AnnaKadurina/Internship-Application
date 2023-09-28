package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.UserDTO;

public interface GetUserDTOUseCase {
    UserDTO getUserDto(long id);

}
