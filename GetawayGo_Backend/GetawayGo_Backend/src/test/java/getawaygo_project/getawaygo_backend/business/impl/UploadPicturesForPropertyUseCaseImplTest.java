package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.FileException;
import getawaygo_project.getawaygo_backend.business.exception.PropertyIsNotFoundException;
import getawaygo_project.getawaygo_backend.persistance.CloudinaryRepository;
import getawaygo_project.getawaygo_backend.persistance.PropertyRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.PropertyEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UploadPicturesForPropertyUseCaseImplTest {
    @Mock
    private CloudinaryRepository cloudinaryRepository;
    @Mock
    private PropertyRepository propertyRepository;
    @InjectMocks
    private UploadPicturesForPropertyUseCaseImpl uploadPicturesForPropertyUseCase;

    @Test
    void uploadPictures() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        String fileName = "test.jpg";
        String content = "test content";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile photo = new MockMultipartFile(fileName, fileName, "image/jpg", bytes);
        List<MultipartFile> photos = Collections.singletonList(photo);

        List<String> urls = new ArrayList<>();
        urls.add("testURL");

        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
        when(cloudinaryRepository.uploadPictures(photos)).thenReturn(urls);

        uploadPicturesForPropertyUseCase.uploadPictures(photos, property.getPropertyId());

        assertEquals("testURL", property.getPhotos().get(0).getUrl());
        verify(propertyRepository).findById(anyLong());
        verify(cloudinaryRepository).uploadPictures(photos);
    }

    @Test
    void uploadPicturesFailed() {
        PropertyEntity property = new PropertyEntity();
        property.setPropertyId(1);

        Long id = 1L;

        String fileName = "test.jpg";
        String content = "test content";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile photo = new MockMultipartFile(fileName, fileName, "image/jpg", bytes);
        List<MultipartFile> photos = Collections.singletonList(photo);

        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
        when(cloudinaryRepository.uploadPictures(photos)).thenThrow(new FileException());

        assertThrows(FileException.class, () -> uploadPicturesForPropertyUseCase.uploadPictures(photos, id));

        verify(propertyRepository).findById(anyLong());
        verify(cloudinaryRepository).uploadPictures(photos);
    }
    @Test
    void uploadPicturesPropertyNotFound() {
        Long id = 1L;

        String fileName = "test.jpg";
        String content = "test content";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile photo = new MockMultipartFile(fileName, fileName, "image/jpg", bytes);
        List<MultipartFile> photos = Collections.singletonList(photo);

        when(propertyRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PropertyIsNotFoundException.class, () -> uploadPicturesForPropertyUseCase.uploadPictures(photos, id));

        verify(propertyRepository).findById(anyLong());
    }
}