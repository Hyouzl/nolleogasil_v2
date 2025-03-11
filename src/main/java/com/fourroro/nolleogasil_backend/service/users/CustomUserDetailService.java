package com.fourroro.nolleogasil_backend.service.users;

import com.fourroro.nolleogasil_backend.entity.users.PrincipalDetails;
import com.fourroro.nolleogasil_backend.entity.users.Users;
import com.fourroro.nolleogasil_backend.repository.users.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UsersRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Users user = userRepository.findByUsersId(Long.valueOf(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));

        return new PrincipalDetails(user);
    }
}
