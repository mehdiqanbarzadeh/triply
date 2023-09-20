package com.triply.greendrive.service.company.impl;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import com.triply.greendrive.api.company.dto.request.FileModel;
import com.triply.greendrive.api.company.dto.response.CompanyEmissionsReportResponse;
import com.triply.greendrive.api.company.dto.response.CompanyResponse;
import com.triply.greendrive.api.company.mapper.CompanyMapper;
import com.triply.greendrive.exception.CompanyExistException;
import com.triply.greendrive.exception.CompanyNotExistException;
import com.triply.greendrive.exception.VehicleExistException;
import com.triply.greendrive.model.company.Company;
import com.triply.greendrive.model.company.dao.CompanyRepository;
import com.triply.greendrive.model.vehicle.Vehicle;
import com.triply.greendrive.model.vehicle.VehicleType;
import com.triply.greendrive.service.company.CompanyService;
import com.triply.greendrive.service.vehicle.VehicleService;
import com.triply.greendrive.utils.Utils;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static java.util.stream.Collectors.groupingBy;


@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

	private final CompanyRepository companyRepository;

	private final VehicleService vehicleService;

	private final CompanyMapper companyMapper;

	@Override
	public CompanyResponse register(Company company) throws CompanyExistException {
		if (companyRepository.existsByName(company.getName())) {
			throw new CompanyExistException(MessageFormat.format("company with name,{0} exist", company.getName()));
		}
		return companyMapper.toCompanyResponse(companyRepository.save(company));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void uploadCSV(String companyCode, MultipartFile file) throws CompanyNotExistException, VehicleExistException, IOException {
		Company company = companyRepository.findByCode(companyCode).orElseThrow(new CompanyNotExistException("company not exist"));
		List<FileModel> models = Utils.csvToModel(file.getInputStream());
		for (FileModel model : models) {
			vehicleService.save(company, model);
		}
	}

	@Override
	public CompanyEmissionsReportResponse getEmissions(String companyCode) {
		List<Vehicle> companyFleet = vehicleService.findAllByCompanyCode(companyCode);
		Map<VehicleType, List<Vehicle>> groupedByType = companyFleet.stream()
				.collect(groupingBy(Vehicle::getType));
		return companyMapper.toCompanyEmissionsReportResponse(groupedByType);
	}
}
