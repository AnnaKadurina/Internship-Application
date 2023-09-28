package getawaygo_project.getawaygo_backend.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Message {
    private String senderUsername;
    private String receiverUsername;
    private String message;
    private String date;
    private Status status;
}
