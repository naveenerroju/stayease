package com.naveen.stayease.entity;

import com.naveen.stayease.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "firstname cannot be null or empty")
    private String firstname;

    @Column()
    private String lastname;

    @Column(unique = true)
    @NotBlank(message = "Email cannot be null or empty")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email address")
    private String email;

    @Min(value = 6, message = "Password length should be at least 6")
    @NotBlank(message = "Password cannot be null or empty")
    private String password;

    @Column(nullable = false)
    @NotBlank(message = "Role cannot be null or empty")
    private Role role;

    //orphan removal will remove booking column from its db when user and booking are not linked anymore.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

}
