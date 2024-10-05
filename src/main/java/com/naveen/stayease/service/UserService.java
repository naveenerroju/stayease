package com.naveen.stayease.service;

import com.naveen.stayease.dto.RegisterRequest;
import com.naveen.stayease.dto.RegisterResponse;
import com.naveen.stayease.entity.User;
import com.naveen.stayease.model.Role;
import com.naveen.stayease.repository.UserRepository;
import com.naveen.stayease.service.templates.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    private final UserRepository repository;

    private final ModelMapper modelMapper = new ModelMapper();

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public RegisterResponse register(RegisterRequest registerRequest){

        if(registerRequest.getRole()==null || registerRequest.getRole().name().isEmpty()){
            registerRequest.setRole(Role.USER);
        }
        User user = repository.save(modelMapper.map(registerRequest, User.class));

        return modelMapper.map(user, RegisterResponse.class);
    }
}
