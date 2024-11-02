package com.naveen.stayease.entity;

import com.naveen.stayease.model.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * The table name is pluralised because the name 'USER' may throw errors in some databases.
 * User could be a keyword in some databases.
 */

@Entity
@Setter
@Getter
@Table(name = "USERS")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;

    @Column()
    private String lastname;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(nullable = false)
    private Role role;

    //orphan removal will remove booking column from its db when user and booking are not linked anymore.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

}
