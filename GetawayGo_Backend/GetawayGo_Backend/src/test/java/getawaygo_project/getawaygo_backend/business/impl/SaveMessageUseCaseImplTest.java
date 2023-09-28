package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.Message;
import getawaygo_project.getawaygo_backend.domain.Status;
import getawaygo_project.getawaygo_backend.persistance.MessageRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.MessageEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveMessageUseCaseImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    private SaveMessageUseCaseImpl saveMessageUseCase;
    @Test
    void saveMessage() {
        UserEntity sender = new UserEntity();
        sender.setUsername("Ani");

        UserEntity receiver = new UserEntity();
        receiver.setUsername("Ani2");

        Message message = new Message();
        message.setSenderUsername(sender.getUsername());
        message.setReceiverUsername(receiver.getUsername());
        message.setStatus(Status.MESSAGE);
        message.setMessage("hi");

        when(userRepository.findByUsername(sender.getUsername())).thenReturn(sender);
        when(userRepository.findByUsername(receiver.getUsername())).thenReturn(receiver);

        saveMessageUseCase.saveMessage(message);

        verify(userRepository).findByUsername(sender.getUsername());
        verify(userRepository).findByUsername(receiver.getUsername());
    }

    @Test
    void saveMessageSenderNotFound() {
        UserEntity sender = new UserEntity();
        sender.setUsername("Ani");

        UserEntity receiver = new UserEntity();
        receiver.setUsername("Ani2");

        Message message = new Message();
        message.setSenderUsername(sender.getUsername());
        message.setReceiverUsername(receiver.getUsername());
        message.setStatus(Status.MESSAGE);
        message.setMessage("hi");

        when(userRepository.findByUsername(sender.getUsername())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> saveMessageUseCase.saveMessage(message));

        verify(userRepository).findByUsername(sender.getUsername());

    }
    @Test
    void saveMessageReceiverNotFound() {
        UserEntity sender = new UserEntity();
        sender.setUsername("Ani");

        UserEntity receiver = new UserEntity();
        receiver.setUsername("Ani2");

        Message message = new Message();
        message.setSenderUsername(sender.getUsername());
        message.setReceiverUsername(receiver.getUsername());
        message.setStatus(Status.MESSAGE);
        message.setMessage("hi");

        when(userRepository.findByUsername(sender.getUsername())).thenReturn(sender);
        when(userRepository.findByUsername(receiver.getUsername())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> saveMessageUseCase.saveMessage(message));

        verify(userRepository).findByUsername(sender.getUsername());
        verify(userRepository).findByUsername(receiver.getUsername());

    }
}