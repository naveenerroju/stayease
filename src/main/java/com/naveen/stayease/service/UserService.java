package com.naveen.stayease.service;

import com.naveen.stayease.dto.LoginRequest;
import com.naveen.stayease.dto.RegisterRequest;
import com.naveen.stayease.dto.RegisterResponse;
import com.naveen.stayease.entity.User;
import com.naveen.stayease.model.Role;
import com.naveen.stayease.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepository repository;

    private final ModelMapper modelMapper = new ModelMapper();

    private final PasswordEncoder passwordEncoder;

    private final CompromisedPasswordChecker compromisedPasswordChecker;

    private final AuthenticationManager authManager;

    private final JWTService jwtService;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, CompromisedPasswordChecker compromisedPasswordChecker, AuthenticationManager authManager, JWTService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.compromisedPasswordChecker = compromisedPasswordChecker;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    /**
     * Checks if the password is compromised and encrypts it before saving.
     * Assign default role, if user didn't choose.
     * @param registerRequest request body of the registration
     * @return response
     */
    public RegisterResponse register(RegisterRequest registerRequest){
        //check if password is compromised and encrypt it
        isPasswordCompromised(registerRequest.getPassword());
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        //set role if user didn't send
        if(registerRequest.getRole()==null || registerRequest.getRole().name().isEmpty()){
            registerRequest.setRole(Role.USER);
        }

        //save the user
        User user = repository.save(modelMapper.map(registerRequest, User.class));

        //return the response
        return modelMapper.map(user, RegisterResponse.class);
    }


    /**
     * checks if the password is compromised using compromisedPasswordChecker
     * @param password user password
     */
    private void isPasswordCompromised(String password){
        CompromisedPasswordDecision decision = compromisedPasswordChecker.check(password);
        if (decision.isCompromised()) {
            throw new CompromisedPasswordException("The provided password is compromised and cannot be used.");
        }
    }

    public String verify(LoginRequest loginRequest) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(loginRequest.getEmail());
        } else {
            return "Unauthenticated";
        }
    }
}
