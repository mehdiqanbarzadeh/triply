package com.triply.greendrive.api.authentication.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AuthenticationRequest {

    @Email(message = "invalid.email")
    private String email;

    @NotBlank(message = "invalid.password")
    private String password;
}
