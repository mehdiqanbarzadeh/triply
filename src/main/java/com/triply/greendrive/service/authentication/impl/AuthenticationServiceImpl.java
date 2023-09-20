package com.triply.greendrive.service.authentication.impl;

import com.triply.greendrive.api.authentication.dto.request.AuthenticationRequest;
import com.triply.greendrive.api.authentication.dto.response.AuthenticationResponse;
import com.triply.greendrive.api.authentication.mapper.AuthenticationMapper;
import com.triply.greendrive.exception.InvalidUsernameOrPasswordException;
import com.triply.greendrive.model.user.dao.UserRepository;
import com.triply.greendrive.service.authentication.AuthenticationService;
import com.triply.greendrive.service.authentication.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationMapper mapper;


    public AuthenticationResponse authenticate(AuthenticationRequest request) throws InvalidUsernameOrPasswordException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(new InvalidUsernameOrPasswordException("email or password is invalid!!!"));
        var jwtToken = jwtService.buildToken(user);
        return mapper.toAuthResponse(jwtToken);
    }
}
