package com.triply.greendrive.api.vehicle;

import com.triply.greendrive.api.vehicle.dto.response.EmployeeEmissionsReportResponse;
import com.triply.greendrive.api.vehicle.dto.response.ReplaceSuggestionsResponse;
import com.triply.greendrive.service.vehicle.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/vehicle")
@RequiredArgsConstructor
public class VehicleResource {

    private final VehicleService vehicleService;

    @GetMapping("/emissions/{employeeId}")
    public ResponseEntity<EmployeeEmissionsReportResponse> getEmissions(@PathVariable("employeeId") String employeeId) {
        return ResponseEntity.ok(vehicleService.getEmissionsByEmployeeId(employeeId));
    }

    @GetMapping("/suggestion")
    public ResponseEntity<ReplaceSuggestionsResponse> getSuggestion() {
        return ResponseEntity.ok(vehicleService.getReplaceableVehicles());
    }


}
