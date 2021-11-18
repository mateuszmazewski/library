package com.github.mateuszmazewski.library.data.service;

import com.github.mateuszmazewski.library.data.MyUserDetails;
import com.github.mateuszmazewski.library.data.entity.User;
import com.github.mateuszmazewski.library.data.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return user.map(MyUserDetails::new).get();
    }
}