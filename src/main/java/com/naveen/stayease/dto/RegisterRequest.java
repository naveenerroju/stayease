package com.naveen.stayease.dto;

import com.naveen.stayease.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegisterRequest {

    @NotBlank(message = "firstname cannot be null or empty")
    private String firstname;

    private String lastname;

    @NotBlank(message = "Email cannot be null or empty")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email address")
    private String email;

    @NotBlank(message = "Passoword cannot be null or empty")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private Role role;



}
