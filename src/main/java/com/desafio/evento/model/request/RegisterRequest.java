package com.desafio.evento.model.request;

import com.desafio.evento.model.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    private String username;
    private String password;
    private  UserRole role;
}
