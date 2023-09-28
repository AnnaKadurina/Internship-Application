package getawaygo_project.getawaygo_backend.persistance;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CloudinaryRepository {
    List<String> uploadPictures(List<MultipartFile> photos);
}
