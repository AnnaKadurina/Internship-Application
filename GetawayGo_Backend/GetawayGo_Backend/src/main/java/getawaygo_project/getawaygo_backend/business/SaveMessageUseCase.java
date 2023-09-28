package getawaygo_project.getawaygo_backend.business;

import getawaygo_project.getawaygo_backend.domain.Message;

public interface SaveMessageUseCase {
    void saveMessage(Message message);
}
