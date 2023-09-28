package getawaygo_project.getawaygo_backend.persistance.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reviewId;
    @Column(name = "Text", columnDefinition = "varchar(500) not null")
    private String text;
    @Column(name = "Rating", columnDefinition = "int not null")
    private int rating;
    @Column(name = "Date", columnDefinition = "date not null")
    private Instant date;

    @ManyToOne()
    @JoinColumn(name = "UserId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity userId;

    @ManyToOne()
    @JoinColumn(name = "PropertyId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PropertyEntity propertyId;
}
