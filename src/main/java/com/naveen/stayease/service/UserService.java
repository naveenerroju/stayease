package com.naveen.stayease.service;

import com.naveen.stayease.dto.RegisterRequest;
import com.naveen.stayease.dto.RegisterResponse;
import com.naveen.stayease.entity.User;
import com.naveen.stayease.model.Role;
import com.naveen.stayease.repository.UserRepository;
import com.naveen.stayease.service.templates.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepository repository;

    private final ModelMapper modelMapper = new ModelMapper();

    private final PasswordEncoder passwordEncoder;

    private final CompromisedPasswordChecker compromisedPasswordChecker;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, CompromisedPasswordChecker compromisedPasswordChecker) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.compromisedPasswordChecker = compromisedPasswordChecker;
    }

    public RegisterResponse register(RegisterRequest registerRequest){
        isPasswordCompromised(registerRequest.getPassword());
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        if(registerRequest.getRole()==null || registerRequest.getRole().name().isEmpty()){
            registerRequest.setRole(Role.USER);
        }
        User user = repository.save(modelMapper.map(registerRequest, User.class));

        return modelMapper.map(user, RegisterResponse.class);
    }


    private void isPasswordCompromised(String password){
        CompromisedPasswordDecision decision = compromisedPasswordChecker.check(password);
        if (decision.isCompromised()) {
            throw new CompromisedPasswordException("The provided password is compromised and cannot be used.");
        }
    }
}
