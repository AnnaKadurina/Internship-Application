package getawaygo_project.getawaygo_backend.business;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UploadPicturesForPropertyUseCase {
    void uploadPictures(List<MultipartFile> photos, long id);
}
