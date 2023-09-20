package com.triply.greendrive.service.authentication;

import com.triply.greendrive.api.authentication.dto.request.AuthenticationRequest;
import com.triply.greendrive.api.authentication.dto.response.AuthenticationResponse;
import com.triply.greendrive.exception.InvalidUsernameOrPasswordException;

public interface AuthenticationService {

	AuthenticationResponse authenticate(AuthenticationRequest request) throws InvalidUsernameOrPasswordException;
}
