package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.SaveMessageUseCase;
import getawaygo_project.getawaygo_backend.business.exception.UserNotFoundException;
import getawaygo_project.getawaygo_backend.domain.Message;
import getawaygo_project.getawaygo_backend.persistance.MessageRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.MessageEntity;
import getawaygo_project.getawaygo_backend.persistance.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class SaveMessageUseCaseImpl implements SaveMessageUseCase {
    private MessageRepository messageRepository;
    private UserRepository userRepository;

    @Override
    public void saveMessage(Message message) {
        UserEntity sender = userRepository.findByUsername(message.getSenderUsername());
        if (sender == null)
            throw new UserNotFoundException();
        UserEntity receiver = userRepository.findByUsername(message.getReceiverUsername());
        if (receiver == null)
            throw new UserNotFoundException();

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessage(message.getMessage());
        messageEntity.setDate(Instant.now());
        messageEntity.setStatus(message.getStatus().toString());
        messageEntity.setSender(sender);
        messageEntity.setReceiver(receiver);

        messageRepository.save(messageEntity);

    }
}
