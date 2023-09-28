package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.UploadPicturesForPropertyUseCase;
import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.persistance.CloudinaryRepository;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyPhotoEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UploadPicturesForPropertyUseCaseImpl implements UploadPicturesForPropertyUseCase {
    private final PropertyRepository propertyRepository;
    private final CloudinaryRepository cloudinaryRepository;

    @Override
    public void uploadPictures(List<MultipartFile> photos, long id) {
        Optional<PropertyEntity> propertyUpdate = propertyRepository.findById(id);
        if (propertyUpdate.isEmpty())
            throw new PropertyIsNotFoundException();
        else {
            PropertyEntity property = propertyUpdate.get();
            List<String> urls = cloudinaryRepository.uploadPictures(photos);

            List<PropertyPhotoEntity> propertyPhotos = new ArrayList<>();
            for (String url : urls) {
                PropertyPhotoEntity propertyPhoto = new PropertyPhotoEntity();
                propertyPhoto.setUrl(url);
                propertyPhoto.setProperty(property);
                propertyPhotos.add(propertyPhoto);
            }
            property.setPhotos(propertyPhotos);

            propertyRepository.save(property);
        }
    }
}
