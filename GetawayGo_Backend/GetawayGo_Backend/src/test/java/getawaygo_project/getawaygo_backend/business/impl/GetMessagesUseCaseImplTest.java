package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.domain.Message;
import getawaygo_project.getawaygo_backend.persistance.MessageRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.MessageEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMessagesUseCaseImplTest {
    @Mock
    private MessageRepository messageRepository;
    @InjectMocks
    private GetMessagesUseCaseImpl getMessagesUseCase;

    @Test
    void loadAllMessages() {
        UserEntity person1 = new UserEntity();
        person1.setUserId(1);
        person1.setUsername("kani");

        UserEntity person2 = new UserEntity();
        person2.setUserId(2);
        person2.setUsername("kadurina");

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessageId(1);
        messageEntity.setDate(Instant.now());
        messageEntity.setStatus("MESSAGE");
        messageEntity.setSender(person1);
        messageEntity.setReceiver(person2);
        messageEntity.setMessage("ehoo");

        MessageEntity messageEntity2 = new MessageEntity();
        messageEntity2.setMessageId(2);
        messageEntity2.setDate(Instant.now());
        messageEntity2.setStatus("MESSAGE");
        messageEntity2.setSender(person2);
        messageEntity2.setReceiver(person1);
        messageEntity2.setMessage("ehoo2");

        MessageEntity messageEntity3 = new MessageEntity();
        messageEntity3.setMessageId(2);
        messageEntity3.setDate(Instant.now());
        messageEntity3.setStatus("MESSAGE");
        messageEntity3.setSender(person2);
        messageEntity3.setReceiver(person2);
        messageEntity3.setMessage("ehoo2");

        when(messageRepository.findAll()).thenReturn(List.of(messageEntity, messageEntity2, messageEntity3));

        List<Message> messages = getMessagesUseCase.loadAllMessages("kani");

        assertEquals("ehoo", messages.get(0).getMessage());
        assertEquals(2, messages.size());
        verify(messageRepository).findAll();
    }
}