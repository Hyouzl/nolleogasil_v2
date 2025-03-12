package com.fourroro.nolleogasil_backend.service.users;

import com.fourroro.nolleogasil_backend.dto.users.LoginDTO;
import com.fourroro.nolleogasil_backend.dto.users.TokenDTO;

public interface AuthService {
    TokenDTO.ResponseTokenDTO login(LoginDTO.RequestLoginDTO loginRequestDto);
}
