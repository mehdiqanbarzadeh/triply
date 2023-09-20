package com.triply.greendrive.api.vehicle.dto.response;

import com.triply.greendrive.config.infrastructure.ResponseService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmployeeEmissionsReportResponse extends ResponseService {
    List<VehicleEmissionsReport> vehicleEmissionsReports;
}
