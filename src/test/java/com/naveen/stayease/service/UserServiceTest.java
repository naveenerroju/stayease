package com.naveen.stayease.service;

import com.naveen.stayease.dto.LoginRequest;
import com.naveen.stayease.dto.RegisterRequest;
import com.naveen.stayease.dto.RegisterResponse;
import com.naveen.stayease.entity.User;
import com.naveen.stayease.model.Role;
import com.naveen.stayease.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CompromisedPasswordChecker compromisedPasswordChecker;

    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder, compromisedPasswordChecker, authManager, jwtService);
    }

    @Test
    void testRegister_Success() {
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "john.doe@example.com", "password123", Role.USER);
        User user = new User();
        user.setId(1L);
        user.setEmail("john.doe@example.com");

        when(compromisedPasswordChecker.check(anyString())).thenReturn(new CompromisedPasswordDecision(false));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        RegisterResponse registerResponse = userService.register(registerRequest);

        assertNotNull(registerResponse);
        assertEquals(1L, registerResponse.getId());
        assertEquals("john.doe@example.com", registerResponse.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_CompromisedPassword() {
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "john.doe@example.com", "password123", Role.USER);

        when(compromisedPasswordChecker.check(anyString())).thenReturn(new CompromisedPasswordDecision(true));

        CompromisedPasswordException exception = assertThrows(CompromisedPasswordException.class, () -> {
            userService.register(registerRequest);
        });

        assertEquals("The provided password is compromised and cannot be used.", exception.getMessage());
    }

    @Test
    void testVerify_Success() {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password123");
        Authentication authentication = mock(Authentication.class);

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(anyString())).thenReturn("mocked-jwt-token");

        String token = userService.verify(loginRequest);

        assertEquals("mocked-jwt-token", token);
    }

    @Test
    void testVerify_Unauthenticated() {
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password123");
        Authentication authentication = mock(Authentication.class);

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        String token = userService.verify(loginRequest);

        assertEquals("Unauthenticated", token);
    }

    @Test
    void testGetUserUsingToken_Success() {
        String token = "mocked-jwt-token";
        String email = "john.doe@example.com";
        User user = new User();
        user.setEmail(email);

        when(jwtService.extractUserName(anyString())).thenReturn(email);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User result = userService.getUserUsingToken(token);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testGetUserUsingToken_UserNotFound() {
        String token = "mocked-jwt-token";
        String email = "john.doe@example.com";

        when(jwtService.extractUserName(anyString())).thenReturn(email);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserUsingToken(token);
        });

        assertEquals("User not found with id: " + email, exception.getMessage());
    }

    @Test
    void testValidateUser_Success() {
        User user = new User();
        user.setEmail("john.doe@example.com");

        boolean result = userService.validateUser(user, "john.doe@example.com");

        assertTrue(result);
    }

    @Test
    void testValidateUser_Failure() {
        User user = new User();
        user.setEmail("john.doe@example.com");

        boolean result = userService.validateUser(user, "jane.doe@example.com");

        assertFalse(result);
    }
}
