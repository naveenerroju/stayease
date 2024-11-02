package com.naveen.stayease.service;

import com.naveen.stayease.dto.RegisterRequest;
import com.naveen.stayease.dto.RegisterResponse;

public interface IUserService {
    RegisterResponse register(RegisterRequest registerRequest);
}
