package com.naveen.stayease.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.naveen.stayease.model.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterResponse {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;

}
