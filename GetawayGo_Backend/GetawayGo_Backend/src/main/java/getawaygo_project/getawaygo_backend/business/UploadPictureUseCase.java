package getawaygo_project.getawaygo_backend.business;

import org.springframework.web.multipart.MultipartFile;

public interface UploadPictureUseCase {
    void uploadPicture(MultipartFile photo, long id);
}
