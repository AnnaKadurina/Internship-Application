package getawaygo_project.getawaygo_backend.persistance.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long propertyId;
    @Column(name = "Name", columnDefinition = "varchar(50) not null")
    private String name;
    @Column(name = "Address", columnDefinition = "varchar(50) not null")
    private String address;
    @Column(name = "Town", columnDefinition = "varchar(50) not null")
    private String town;
    @Column(name = "Country", columnDefinition = "varchar(50) not null")
    private String country;
    @Column(name = "NumberOfRooms", columnDefinition = "int not null")
    private int nrOfRooms;
    @Column(name = "Description", columnDefinition = "varchar(500) not null")
    private String description;
    @Column(name = "Price", columnDefinition = "double not null")
    private double price;
    @Column(name = "Status", columnDefinition = "varchar(10) not null")
    private Boolean active;

    @ManyToOne()
    @JoinColumn(name = "UserId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user;
    public void setUser(UserEntity user) {
        this.user = user;

    }
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<PropertyPhotoEntity> photos = new ArrayList<>();

    @OneToMany(mappedBy = "propertyId", cascade = CascadeType.ALL)
    private List<ReviewEntity> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "propertyId", cascade = CascadeType.ALL)
    private List<BookingEntity> bookings = new ArrayList<>();

}
