package com.triply.greendrive.api.authentication;

import com.triply.greendrive.api.authentication.dto.request.AuthenticationRequest;
import com.triply.greendrive.api.authentication.dto.response.AuthenticationResponse;
import com.triply.greendrive.exception.InvalidUsernameOrPasswordException;
import com.triply.greendrive.service.authentication.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationResource {

    private final AuthenticationService authenticationService;

    @PostMapping("/getToken")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws InvalidUsernameOrPasswordException {
        log.info("request with email :{} start to authenticate", request.getEmail());

        AuthenticationResponse response = authenticationService.authenticate(request);

        log.info("authentication is successful with email :{}", request.getEmail());
        return ResponseEntity.ok(response);
    }
}
