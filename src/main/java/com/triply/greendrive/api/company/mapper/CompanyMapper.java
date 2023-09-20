package com.triply.greendrive.api.company.mapper;

import com.triply.greendrive.api.company.dto.request.CompanyRegisterRequest;
import com.triply.greendrive.api.company.dto.response.CompanyEmissionsReportResponse;
import com.triply.greendrive.api.company.dto.response.CompanyResponse;
import com.triply.greendrive.api.company.dto.response.CompanyVehicleAverageEmission;
import com.triply.greendrive.api.company.dto.response.UploadResponse;
import com.triply.greendrive.config.infrastructure.ResultStatus;
import com.triply.greendrive.model.company.Company;
import com.triply.greendrive.model.vehicle.Vehicle;
import com.triply.greendrive.model.vehicle.VehicleType;
import com.triply.greendrive.utils.Utils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Mapper(componentModel = "spring", imports = {Random.class, ResultStatus.class})
public interface CompanyMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "code", source = ".", qualifiedByName = "generateUniqCode")
    Company toCompany(CompanyRegisterRequest request);


    @Mapping(target = "name", source = "name")
    @Mapping(target = "companyCode", source = "code")
    @Mapping(target = "result", expression = "java(ResultStatus.SUCCESS)")
    CompanyResponse toCompanyResponse(Company company);

    @Named("generateUniqCode")
    default String generate(CompanyRegisterRequest request) {
        long timestamp = System.currentTimeMillis();
        int randomNum = new Random().nextInt(10000);
        String trackingCode = String.format("%d-%04d", timestamp, randomNum);
        return trackingCode;
    }

    @Mapping(target = "vehicleAverageEmissions", source = ".", qualifiedByName = "toVehicleAverageEmissions")
	@Mapping(target = "result", expression = "java(ResultStatus.SUCCESS)")
	CompanyEmissionsReportResponse toCompanyEmissionsReportResponse(Map<VehicleType, List<Vehicle>> vehicleTypeListMap);

    @Named("toVehicleAverageEmissions")
    default List<CompanyVehicleAverageEmission> toVehicleAverageEmissions(Map<VehicleType, List<Vehicle>> vehicleTypeListMap) {
        List<CompanyVehicleAverageEmission> emissions = new ArrayList<>();
        for (VehicleType vehicleType : vehicleTypeListMap.keySet()) {
            List<Vehicle> vehicles = vehicleTypeListMap.get(vehicleType);
            CompanyVehicleAverageEmission emission = new CompanyVehicleAverageEmission();
            emission.setVehicleType(vehicleType.name());
            emission.setAverageEmission(Utils.calculateAverageEmissions(vehicles, vehicleType));
            emission.setTotalEmissions(Utils.calculateTotalEmissions(vehicles, vehicleType));
            emission.setTotalVehicles(Long.valueOf(vehicles.size()));
            emission.setTotalMileages(Utils.calculateTotalMileages(vehicles));
            emissions.add(emission);
        }
        return emissions;
    }

	@Mapping(target = "message",source = "message")
	@Mapping(target = "result", expression = "java(ResultStatus.SUCCESS)")
	UploadResponse toUploadResponse(String message);
}
