package com.example.postapp.service;

import com.example.postapp.entity.UserEntity;
import com.example.postapp.exception.UserNotFoundException;
import com.example.postapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserEntityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserEntityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("user not founded"));
    }

    public boolean notExist (String email) {
        UserEntity user = userRepository.findUserByEmail(email)
                .orElse(null);

        return user == null;
    }
}
