package getawaygo_project.getawaygo_backend.persistance.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    @Column(name = "Username", columnDefinition = "varchar(50) not null", unique = true)
    private String username;
    @Column(name = "Email", columnDefinition = "varchar(100) not null", unique = true)
    private String email;
    @Column(name = "FirstName", columnDefinition = "varchar(100) not null")
    private String firstName;
    @Column(name = "LastName", columnDefinition = "varchar(100) not null")
    private String lastName;
    @Column(name = "Address", columnDefinition = "varchar(100) not null")
    private String address;
    @Column(name = "Phone", columnDefinition = "varchar(20) not null")
    private String phone;
    @Column(name = "Photo", columnDefinition = "varchar(100) not null")
    private String photo;
    @Column(name = "Password", columnDefinition = "varchar(100) not null")
    private String password;
    @Column(name = "Status", columnDefinition = "varchar(10) not null")
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "AUTHORITY_ID")
    private AuthorityEntity authority;

    public void setAuthority(AuthorityEntity authority) {
        this.authority = authority;

    }

    @OneToMany(mappedBy = "user")
    private List<PropertyEntity> properties;

    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private List<ReviewEntity> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL)
    private List<BookingEntity> bookings = new ArrayList<>();
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<MessageEntity> sentMessages;
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<MessageEntity> receivedMessages;

}
