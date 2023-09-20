package com.triply.greendrive.service.vehicle.impl;

import com.triply.greendrive.api.company.dto.request.FileModel;
import com.triply.greendrive.api.vehicle.dto.response.EmployeeEmissionsReportResponse;
import com.triply.greendrive.api.vehicle.dto.response.ReplaceSuggestionsResponse;
import com.triply.greendrive.api.vehicle.mapper.VehicleMapper;
import com.triply.greendrive.config.infrastructure.ResultStatus;
import com.triply.greendrive.model.company.Company;
import com.triply.greendrive.model.vehicle.Vehicle;
import com.triply.greendrive.model.vehicle.VehicleType;
import com.triply.greendrive.model.vehicle.dao.VehicleRepository;
import com.triply.greendrive.service.vehicle.VehicleService;
import com.triply.greendrive.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService {

    private final VehicleMapper vehicleMapper;

    private final VehicleRepository vehicleRepository;

    @Override
    public Vehicle save(Company company, FileModel model) {
        Vehicle vehicle = vehicleMapper.toVehicle(company, model);
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> findAllByCompanyCode(String companyCode) {
        return vehicleRepository.findByCompanyCodeAndCreationDateAfter(companyCode,
                LocalDate.now().minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public EmployeeEmissionsReportResponse getEmissionsByEmployeeId(String employeeId) {
        List<Vehicle> emissionsByEmployee = vehicleRepository.findByEmployeeIdAndCreationDateAfter(employeeId,
                LocalDate.now().minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());

        Map<VehicleType, List<Vehicle>> vehicleTypeList = emissionsByEmployee.stream()
                .collect(groupingBy(Vehicle::getType));

        return vehicleMapper.toEmployeeEmissionsReportResponse(vehicleTypeList);
    }

    @Override
    public ReplaceSuggestionsResponse getReplaceableVehicles() {

        List<Vehicle> lastWeekReports = vehicleRepository.findByCreationDateAfter(LocalDate.now().minusDays(7).atStartOfDay(ZoneId.systemDefault()).toInstant());

        Map<VehicleType, List<Vehicle>> groupedByType = lastWeekReports.stream()
                .collect(groupingBy(Vehicle::getType));
        Map<VehicleType, Long> vehicleTypeEmissionAverage = Utils.calculateEmissionsByVehicleTypeAndAverageMileage(groupedByType);
        List<Vehicle> suggestElectricCarReplacements = Utils.suggestElectricCarReplacements(groupedByType, vehicleTypeEmissionAverage);
        ReplaceSuggestionsResponse replaceSuggestionsResponse = new ReplaceSuggestionsResponse(vehicleMapper.toReplaceableSuggestionVehicles(suggestElectricCarReplacements));
        replaceSuggestionsResponse.setResult(ResultStatus.SUCCESS);
        return replaceSuggestionsResponse;
    }

}
