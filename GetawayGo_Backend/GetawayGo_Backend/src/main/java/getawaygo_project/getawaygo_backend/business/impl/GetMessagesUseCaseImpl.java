package getawaygo_project.getawaygo_backend.business.impl;

import getawaygo_project.getawaygo_backend.business.GetMessagesUseCase;
import getawaygo_project.getawaygo_backend.domain.Message;
import getawaygo_project.getawaygo_backend.domain.Status;
import getawaygo_project.getawaygo_backend.persistance.MessageRepository;
import getawaygo_project.getawaygo_backend.persistance.UserRepository;
import getawaygo_project.getawaygo_backend.persistance.entity.MessageEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GetMessagesUseCaseImpl implements GetMessagesUseCase {
    private MessageRepository messageRepository;
    private UserRepository userRepository;

    @Override
    public List<Message> loadAllMessages(String username) {
        List<Message> allMessages = new ArrayList<>();
        List<MessageEntity> messageEntities = messageRepository.findAll().stream()
                .filter(m -> m.getSender().getUsername().equals(username) || m.getReceiver().getUsername().equals(username))
                .toList();

        for (MessageEntity x : messageEntities) {
            Message message = new Message();
            message.setDate(x.getDate().toString());
            message.setMessage(x.getMessage());
            message.setStatus(Status.MESSAGE);
            message.setSenderUsername(x.getSender().getUsername());
            message.setReceiverUsername(x.getReceiver().getUsername());
            allMessages.add(message);
        }
        return allMessages;
    }
}
