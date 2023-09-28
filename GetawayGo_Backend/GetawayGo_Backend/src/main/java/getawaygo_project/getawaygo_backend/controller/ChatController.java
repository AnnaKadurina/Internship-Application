package getawaygo_project.getawaygo_backend.controller;

import getawaygo_project.getawaygo_backend.business.GetMessagesUseCase;
import getawaygo_project.getawaygo_backend.business.SaveMessageUseCase;
import getawaygo_project.getawaygo_backend.configuration.isauthenticated.IsAuthenticated;
import getawaygo_project.getawaygo_backend.domain.Message;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class ChatController {
    private SimpMessagingTemplate simpMessagingTemplate;
    private SaveMessageUseCase saveMessageUseCase;
    private GetMessagesUseCase getMessagesUseCase;

    @GetMapping("/{username}/messages")
    @IsAuthenticated
    public void loadAllMessages(@PathVariable(value = "username") final String username) {
        List<Message> loadMessages = getMessagesUseCase.loadAllMessages(username);
        for (Message x : loadMessages) {
            if (x.getReceiverUsername().equals(username)) {
                simpMessagingTemplate.convertAndSendToUser(x.getReceiverUsername(), "/private", x);
            } else {
                simpMessagingTemplate.convertAndSendToUser(x.getSenderUsername(), "/private", x);
            }
        }
    }

    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message) {
        return message;
    }

    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message) {
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverUsername(), "/private", message);
        saveMessageUseCase.saveMessage(message);
        return message;
    }
}
