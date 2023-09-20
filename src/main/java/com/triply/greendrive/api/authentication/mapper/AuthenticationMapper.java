package com.triply.greendrive.api.authentication.mapper;

import com.triply.greendrive.api.authentication.dto.response.AuthenticationResponse;
import com.triply.greendrive.config.infrastructure.ResultStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",imports = ResultStatus.class, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AuthenticationMapper {

    @Mapping(target = "token", source = ".")
    @Mapping(target = "result", expression = "java(ResultStatus.SUCCESS)")
    AuthenticationResponse toAuthResponse(String jwtToken);

}
