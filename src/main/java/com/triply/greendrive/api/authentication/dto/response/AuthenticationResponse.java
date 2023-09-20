package com.triply.greendrive.api.authentication.dto.response;


import com.triply.greendrive.config.infrastructure.ResponseService;
import lombok.*;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthenticationResponse extends ResponseService {

    private String token;

}
