package com.triply.greendrive.api.vehicle.dto.response;

import com.triply.greendrive.config.infrastructure.ResponseService;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EmissionsResponse extends ResponseService {

    private String test;
}
