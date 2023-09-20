package com.triply.greendrive.service.vehicle;

import java.util.List;

import com.triply.greendrive.api.company.dto.request.FileModel;
import com.triply.greendrive.api.vehicle.dto.response.EmployeeEmissionsReportResponse;
import com.triply.greendrive.api.vehicle.dto.response.ReplaceSuggestionsResponse;
import com.triply.greendrive.exception.VehicleExistException;
import com.triply.greendrive.model.company.Company;
import com.triply.greendrive.model.vehicle.Vehicle;


public interface VehicleService {

	Vehicle save(Company company, FileModel model) throws VehicleExistException;

	List<Vehicle> findAllByCompanyCode(String companyCode);

	EmployeeEmissionsReportResponse getEmissionsByEmployeeId(String employeeId);

	ReplaceSuggestionsResponse getReplaceableVehicles();
}
