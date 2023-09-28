package getawaygo_project.getawaygo_backend.controller;

import getawaygo_project.getawaygo_backend.business.GetMessagesUseCase;
import getawaygo_project.getawaygo_backend.business.SaveMessageUseCase;
import getawaygo_project.getawaygo_backend.domain.Message;
import getawaygo_project.getawaygo_backend.domain.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ChatControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;
    @MockBean
    private GetMessagesUseCase getMessagesUseCase;
    @MockBean
    private SaveMessageUseCase saveMessageUseCase;

    @Test
    @WithMockUser(username = "guest", roles = {"GUEST"})
    void loadAllMessagesReturns200() throws Exception {
        String username1 = "ani";
        String username2 = "kani";

        Message message = new Message();
        message.setMessage("eho");
        message.setDate(Instant.now().toString());
        message.setStatus(Status.MESSAGE);
        message.setReceiverUsername(username1);
        message.setSenderUsername(username2);

        Message message2 = new Message();
        message2.setMessage("eho");
        message2.setDate(Instant.now().toString());
        message2.setStatus(Status.MESSAGE);
        message2.setReceiverUsername(username2);
        message2.setSenderUsername(username1);

        when(getMessagesUseCase.loadAllMessages(username1)).thenReturn(List.of(message, message2));

        mockMvc.perform(get("/{username}/messages", username1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());

        verify(simpMessagingTemplate).convertAndSendToUser(message.getReceiverUsername(), "/private", message2);
        verify(simpMessagingTemplate).convertAndSendToUser(message2.getSenderUsername(), "/private", message);

    }

    @Test
    void loadAllMessagesReturns401() throws Exception {
        String username1 = "ani";

        mockMvc.perform(get("/{username}/messages", username1).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isUnauthorized());

    }
}