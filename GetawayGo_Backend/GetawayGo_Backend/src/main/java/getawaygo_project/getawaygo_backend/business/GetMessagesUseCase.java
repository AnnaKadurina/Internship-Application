package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.Message;

import java.util.List;

public interface GetMessagesUseCase {
    List<Message> loadAllMessages(String username);
}
