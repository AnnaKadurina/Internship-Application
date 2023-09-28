package getawaygo_project.getawaygo_backend.persistance.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long messageId;
    @Column(name = "Message", columnDefinition = "varchar(50) not null")
    private String message;
    @Column(name = "Date", columnDefinition = "varchar(50) not null")
    private Instant date;
    @Column(name = "Status", columnDefinition = "varchar(50) not null")
    private String status;
    @ManyToOne()
    @JoinColumn(name = "sender_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity sender;
    @ManyToOne()
    @JoinColumn(name = "receiver_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity receiver;

}
