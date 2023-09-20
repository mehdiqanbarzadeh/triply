package com.triply.greendrive.api.vehicle.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplaceableSuggestionVehicle {

    private String vehicleCode;

    private String employeeId;

    private Long mileage;

    private String vehicleType;

}
