package com.triply.greendrive.api.company.dto.response;

import lombok.Data;

@Data
public class CompanyVehicleAverageEmission {

	private String vehicleType;

	private Long averageEmission;

	private Long totalMileages;

	private Long totalEmissions;

	private Long totalVehicles;

}
