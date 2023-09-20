package com.triply.greendrive.api.vehicle.dto.response;

import lombok.Data;

@Data
public class VehicleEmissionsReport {


    private String vehicleType;

    private Long averageEmission;

    private Long totalMileages;

    private Long totalEmissions;

    private Long totalVehicles;

}
