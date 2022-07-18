package com.inside.messagesystem.security;

import com.inside.messagesystem.entity.UserEntity;
import com.inside.messagesystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserEntity user = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("This user is not registered yet"));

        return JwtUserFactory.create(user);
    }
}
