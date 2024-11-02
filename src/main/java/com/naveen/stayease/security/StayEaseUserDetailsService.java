package com.naveen.stayease.security;

import com.naveen.stayease.entity.User;
import com.naveen.stayease.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StayEaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads the user details based on the provided email.
     * This method is part of the Spring Security framework and is used for authentication.
     *
     * @param email the email of the user to be loaded
     * @return UserDetails containing the user's information, including email, password, and authorities (roles)
     * @throws UsernameNotFoundException if the user is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find the user by email
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("User details not found in the database: " + email)
        );

        // Create a list of authorities (roles) for the user
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));

        // Return a UserDetails object containing the user's email, password, and authorities
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }



}
