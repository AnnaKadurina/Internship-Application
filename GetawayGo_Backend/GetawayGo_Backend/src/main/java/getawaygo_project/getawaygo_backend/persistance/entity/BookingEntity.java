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
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookingId;
    @Column(name = "StartDate", columnDefinition = "date not null")
    private Instant startDate;
    @Column(name = "EndDate", columnDefinition = "date not null")
    private Instant endDate;
    @Column(name = "Price", columnDefinition = "double not null")
    private double price;
    @ManyToOne()
    @JoinColumn(name = "UserId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity userId;

    @ManyToOne()
    @JoinColumn(name = "PropertyId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private PropertyEntity propertyId;
}
