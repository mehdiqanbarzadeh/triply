package com.triply.greendrive.api.company.dto.response;

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
public class CompanyEmissionsReportResponse extends ResponseService {

    private List<CompanyVehicleAverageEmission> vehicleAverageEmissions;
}
