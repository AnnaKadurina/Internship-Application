package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.FileException;
import getawaygo_project.getawaygo_backend.business.exception.UnauthorizedDataException;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.AccessToken;
import getawaygo_project.getawaygo_backend.persistance.CloudinaryRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UploadPictureUseCaseImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private CloudinaryRepository cloudinaryRepository;
    @Mock
    private AccessToken accessToken;
    @InjectMocks
    private UploadPictureUseCaseImpl uploadPictureUseCase;

    @Test
    void uploadPicture() {
        UserEntity updateUser = new UserEntity();
        updateUser.setUsername("anikadurinaa");
        updateUser.setUserId(1);

        String fileName = "test.jpg";
        String content = "test content";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile photo = new MockMultipartFile(fileName, fileName, "image/jpg", bytes);
        List<MultipartFile> photos = Collections.singletonList(photo);

        List<String> urls = new ArrayList<>();
        urls.add("testURL");

        when(accessToken.getUserId()).thenReturn(updateUser.getUserId());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(updateUser));
        when(cloudinaryRepository.uploadPictures(photos)).thenReturn(urls);

        uploadPictureUseCase.uploadPicture(photo, updateUser.getUserId());

        assertEquals("testURL", updateUser.getPhoto());
        verify(userRepository).findById(anyLong());
        verify(cloudinaryRepository).uploadPictures(photos);
        verify(accessToken).getUserId();
    }

    @Test
    void uploadPictureFailed() {
        UserEntity updateUser = new UserEntity();
        updateUser.setUsername("anikadurinaa");
        updateUser.setUserId(1);

        Long id = 1L;

        String fileName = "test.jpg";
        String content = "test content";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile photo = new MockMultipartFile(fileName, fileName, "image/jpg", bytes);

        List<MultipartFile> photos = Collections.singletonList(photo);

        when(accessToken.getUserId()).thenReturn(updateUser.getUserId());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(updateUser));
        when(cloudinaryRepository.uploadPictures(photos)).thenThrow(new FileException());

        assertThrows(FileException.class, () -> uploadPictureUseCase.uploadPicture(photo, id));

        verify(userRepository).findById(anyLong());
        verify(cloudinaryRepository).uploadPictures(photos);
        verify(accessToken).getUserId();
    }

    @Test
    void uploadPictureUserNotFound() {
        Long id = 1L;

        String fileName = "test.jpg";
        String content = "test content";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile photo = new MockMultipartFile(fileName, fileName, "image/jpg", bytes);

        when(accessToken.getUserId()).thenReturn(id);
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> uploadPictureUseCase.uploadPicture(photo, id));

        verify(userRepository).findById(anyLong());
        verify(accessToken).getUserId();
    }

    @Test
    void uploadPictureNotAuthorized() {
        Long id = 1L;
        Long wrongId = 2L;

        String fileName = "test.jpg";
        String content = "test content";
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        MockMultipartFile photo = new MockMultipartFile(fileName, fileName, "image/jpg", bytes);

        when(accessToken.getUserId()).thenReturn(wrongId);

        assertThrows(UnauthorizedDataException.class, () -> uploadPictureUseCase.uploadPicture(photo, id));

        verify(accessToken).getUserId();
    }
}