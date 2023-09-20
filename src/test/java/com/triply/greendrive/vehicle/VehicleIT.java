package com.triply.greendrive.vehicle;

import java.util.List;

import com.triply.greendrive.GreenDriveApplicationTests;
import com.triply.greendrive.api.authentication.dto.response.AuthenticationResponse;
import com.triply.greendrive.api.company.dto.response.CompanyResponse;
import com.triply.greendrive.api.vehicle.dto.response.EmployeeEmissionsReportResponse;
import com.triply.greendrive.api.vehicle.dto.response.ReplaceSuggestionsResponse;
import com.triply.greendrive.api.company.dto.response.UploadResponse;
import com.triply.greendrive.api.vehicle.dto.response.VehicleEmissionsReport;
import com.triply.greendrive.config.infrastructure.ResultStatus;
import com.triply.greendrive.exception.CompanyExistException;
import com.triply.greendrive.exception.InvalidUsernameOrPasswordException;
import com.triply.greendrive.model.company.dao.CompanyRepository;
import com.triply.greendrive.model.vehicle.Vehicle;
import com.triply.greendrive.model.vehicle.VehicleType;
import com.triply.greendrive.model.vehicle.dao.VehicleRepository;
import com.triply.greendrive.service.authentication.AuthenticationService;
import com.triply.greendrive.service.company.CompanyService;
import com.triply.greendrive.util.TestUtils;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class VehicleIT extends GreenDriveApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private VehicleRepository vehicleRepository;

	@Before
	public void destroy() {
		vehicleRepository.deleteAll();
		companyRepository.deleteAll();
	}

	@Test
	public void getEmissionsPerEmployee_successful() throws CompanyExistException, InvalidUsernameOrPasswordException {

		CompanyResponse companyResponse = companyService.register(TestUtils.getValidCompany("test1", "323232"));

		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", new ClassPathResource("files/valid_company_fleet_for_emissions.csv"));
		map.add("companyCode", companyResponse.getCompanyCode());

		String token = registerAdminUser().getToken();
		HttpHeaders defaultHttpHeaders = TestUtils.getDefaultHttpHeaders(token);
		defaultHttpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<Object> entity = new HttpEntity<>(map, defaultHttpHeaders);
		String url = TestUtils.baseUrl(port) + "/company/upload";
		ResponseEntity<UploadResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity,
				UploadResponse.class);

		assertThat(response.getBody().getMessage()).isNotNull();
		assertThat(response.getBody().getResult().getTitle()).isEqualTo(ResultStatus.SUCCESS);

		List<Vehicle> list = vehicleRepository.findAllByCompanyCode(companyResponse.getCompanyCode());
		assertThat(list).hasSize(22);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<Object> emissionBody = new HttpEntity<>(headers, headers);
		String urlEmission = TestUtils.baseUrl(port) + "/vehicle/emissions/E1";

		ResponseEntity<EmployeeEmissionsReportResponse> responseEmission =
				restTemplate.exchange(urlEmission, HttpMethod.GET, emissionBody, EmployeeEmissionsReportResponse.class);

		assertThat(response.getBody().getMessage()).isNotNull();
		assertThat(response.getBody().getResult().getTitle()).isEqualTo(ResultStatus.SUCCESS);

		VehicleEmissionsReport report = responseEmission.getBody().getVehicleEmissionsReports().get(0);

		assertThat(report.getTotalEmissions()).isEqualTo(53460);
		assertThat(report.getVehicleType()).isEqualTo(VehicleType.VAN.name());
		assertThat(report.getAverageEmission()).isEqualTo(17820);
		assertThat(report.getTotalMileages()).isEqualTo(297);
		assertThat(report.getTotalVehicles()).isEqualTo(3);
	}

	@Test
	public void suggestion_successful() throws CompanyExistException, InvalidUsernameOrPasswordException {

		CompanyResponse companyResponse = companyService.register(TestUtils.getValidCompany("test1", "323232"));

		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", new ClassPathResource("files/valid_company_fleet_for_suggestion.csv"));
		map.add("companyCode", companyResponse.getCompanyCode());

		String token = registerAdminUser().getToken();
		HttpHeaders defaultHttpHeaders = TestUtils.getDefaultHttpHeaders(token);
		defaultHttpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<Object> entity = new HttpEntity<>(map, defaultHttpHeaders);
		String url = TestUtils.baseUrl(port) + "/company/upload";
		ResponseEntity<UploadResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity,
				UploadResponse.class);

		assertThat(response.getBody().getMessage()).isNotNull();
		assertThat(response.getBody().getResult().getTitle()).isEqualTo(ResultStatus.SUCCESS);

		List<Vehicle> list = vehicleRepository.findAllByCompanyCode(companyResponse.getCompanyCode());
		assertThat(list).hasSize(21);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		HttpEntity<Object> emissionBody = new HttpEntity<>(headers, headers);
		String urlEmission = TestUtils.baseUrl(port) + "/vehicle/suggestion";

		ResponseEntity<ReplaceSuggestionsResponse> responseEmission =
				restTemplate.exchange(urlEmission, HttpMethod.GET, emissionBody, ReplaceSuggestionsResponse.class);

		assertThat(responseEmission.getBody().getReplaceableSuggestionVehicles()).isNotNull();
		assertThat(responseEmission.getBody().getResult().getTitle()).isEqualTo(ResultStatus.SUCCESS);

		assertThat(responseEmission.getBody().getReplaceableSuggestionVehicles()).hasSize(5);

	}

	private AuthenticationResponse registerAdminUser() throws InvalidUsernameOrPasswordException {
		return authenticationService.authenticate(TestUtils.getAuthenticationRequest());
	}

}
