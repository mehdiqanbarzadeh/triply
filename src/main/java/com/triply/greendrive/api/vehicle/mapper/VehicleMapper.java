package com.triply.greendrive.api.vehicle.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.triply.greendrive.api.company.dto.request.FileModel;
import com.triply.greendrive.api.vehicle.dto.response.EmployeeEmissionsReportResponse;
import com.triply.greendrive.api.vehicle.dto.response.ReplaceableSuggestionVehicle;
import com.triply.greendrive.api.vehicle.dto.response.VehicleEmissionsReport;
import com.triply.greendrive.config.infrastructure.ResultStatus;
import com.triply.greendrive.model.company.Company;
import com.triply.greendrive.model.vehicle.Vehicle;
import com.triply.greendrive.model.vehicle.VehicleType;
import com.triply.greendrive.utils.Utils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", imports = ResultStatus.class)
public interface VehicleMapper {


	@Mapping(target = "id", expression = "java(null)")
	@Mapping(target = "company", source = "company")
	@Mapping(target = "IDCode", source = "model.vehicleIdCode")
	@Mapping(target = "employeeId", source = "model.employeeId")
	@Mapping(target = "type", source = "model.vehicleType")
	@Mapping(target = "mileage", source = "model.vehicleMileage")
	Vehicle toVehicle(Company company, FileModel model);


	@Mapping(target = "vehicleEmissionsReports", source = ".", qualifiedByName = "toVehicleEmissionsReports")
	@Mapping(target = "result", expression = "java(ResultStatus.SUCCESS)")
	EmployeeEmissionsReportResponse toEmployeeEmissionsReportResponse(Map<VehicleType, List<Vehicle>> vehicleTypeList);

	@Named("toVehicleEmissionsReports")
	default List<VehicleEmissionsReport> toVehicleEmissionsReports(Map<VehicleType, List<Vehicle>> vehicleTypeListMap) {
		List<VehicleEmissionsReport> emissions = new ArrayList<>();
		for (VehicleType vehicleType : vehicleTypeListMap.keySet()) {
			List<Vehicle> vehicles = vehicleTypeListMap.get(vehicleType);
			VehicleEmissionsReport emission = new VehicleEmissionsReport();
			emission.setVehicleType(vehicleType.name());
			emission.setAverageEmission(Utils.calculateAverageEmissions(vehicles, vehicleType));
			emission.setTotalEmissions(Utils.calculateTotalEmissions(vehicles, vehicleType));
			emission.setTotalVehicles(Long.valueOf(vehicles.size()));
			emission.setTotalMileages(Utils.calculateTotalMileages(vehicles));
			emissions.add(emission);
		}
		return emissions;
	}


	@Named("toReplaceableSuggestionVehicles")
	default List<ReplaceableSuggestionVehicle> toReplaceableSuggestionVehicles(List<Vehicle> suggestElectricCarReplacements) {
		List<ReplaceableSuggestionVehicle> list = new ArrayList<>();
		for (Vehicle suggestElectricCarReplacement : suggestElectricCarReplacements) {
			ReplaceableSuggestionVehicle suggestionVehicle = new ReplaceableSuggestionVehicle();
			suggestionVehicle.setVehicleCode(suggestElectricCarReplacement.getIDCode());
			suggestionVehicle.setMileage(suggestElectricCarReplacement.getMileage());
			suggestionVehicle.setEmployeeId(suggestElectricCarReplacement.getEmployeeId());
			suggestionVehicle.setVehicleType(suggestElectricCarReplacement.getType().name());
			list.add(suggestionVehicle);
		}
		return list;
	}

}
