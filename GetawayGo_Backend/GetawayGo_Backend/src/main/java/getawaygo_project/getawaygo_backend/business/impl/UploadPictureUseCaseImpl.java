package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.UploadPictureUseCase;
import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.persistance.CloudinaryRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UploadPictureUseCaseImpl implements UploadPictureUseCase {
    private final UserRepository userRepository;
    private final CloudinaryRepository cloudinaryRepository;
    private AccessToken accessToken;

    public void uploadPicture(MultipartFile photo, long id) {
        if (accessToken.getUserId() != id)
            throw new UnauthorizedDataException();
        Optional<UserEntity> userUpdate = userRepository.findById(id);
        if (userUpdate.isEmpty())
            throw new UserNotFoundException();
        else {
            UserEntity user = userUpdate.get();
            List<String> url = cloudinaryRepository.uploadPictures(Collections.singletonList(photo));
            user.setPhoto(url.get(0));
            userRepository.save(user);
        }
    }
}
