package com.triply.greendrive.company;

import java.util.List;

import com.triply.greendrive.GreenDriveApplicationTests;
import com.triply.greendrive.api.company.dto.request.CompanyRegisterRequest;
import com.triply.greendrive.api.authentication.dto.response.AuthenticationResponse;
import com.triply.greendrive.api.company.dto.response.CompanyEmissionsReportResponse;
import com.triply.greendrive.api.company.dto.response.CompanyResponse;
import com.triply.greendrive.api.company.dto.response.CompanyVehicleAverageEmission;
import com.triply.greendrive.api.company.dto.response.UploadResponse;
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


public class CompanyIT extends GreenDriveApplicationTests {

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
	public void registerCompany_successful() throws InvalidUsernameOrPasswordException {

		String url = TestUtils.baseUrl(port) + "/company/register";
		HttpHeaders defaultHttpHeaders = TestUtils.getDefaultHttpHeaders(registerAdminUser().getToken());

		CompanyRegisterRequest companyRegisterRequest = new CompanyRegisterRequest();
		companyRegisterRequest.setName("new_company");

		HttpEntity<?> entity = new HttpEntity<>(companyRegisterRequest, defaultHttpHeaders);

		ResponseEntity<CompanyResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity,
				CompanyResponse.class);

		assertThat(response.getBody().getCompanyCode()).isNotNull();
		assertThat(response.getBody().getResult().getTitle()).isEqualTo(ResultStatus.SUCCESS);
		assertThat(response.getBody().getName()).isEqualTo("new_company");
	}


	@Test
	public void uploadCompanyFleet_successful() throws CompanyExistException, InvalidUsernameOrPasswordException {

		CompanyResponse companyResponse = companyService.register(TestUtils.getValidCompany("company_name", "123123"));

		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", new ClassPathResource("files/valid_company_fleet.csv"));
		map.add("companyCode", companyResponse.getCompanyCode());

		HttpHeaders defaultHttpHeaders = TestUtils.getDefaultHttpHeaders(registerAdminUser().getToken());
		defaultHttpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<Object> entity = new HttpEntity<>(map, defaultHttpHeaders);
		String url = TestUtils.baseUrl(port) + "/company/upload";
		ResponseEntity<UploadResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity,
				UploadResponse.class);

		assertThat(response.getBody().getMessage()).isNotNull();
		assertThat(response.getBody().getResult().getTitle()).isEqualTo(ResultStatus.SUCCESS);

		List<Vehicle> list = vehicleRepository.findAllByCompanyCode(companyResponse.getCompanyCode());
		assertThat(list).hasSize(6);
	}


	@Test
	public void getCompanyEmissions_successful() throws CompanyExistException, InvalidUsernameOrPasswordException {

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
		String urlEmission = TestUtils.baseUrl(port) + "/company/emissions/" + companyResponse.getCompanyCode();

		ResponseEntity<CompanyEmissionsReportResponse> responseEmission =
				restTemplate.exchange(urlEmission, HttpMethod.GET, emissionBody, CompanyEmissionsReportResponse.class);


		assertThat(response.getBody().getMessage()).isNotNull();
		assertThat(response.getBody().getResult().getTitle()).isEqualTo(ResultStatus.SUCCESS);
		assertThat(responseEmission.getBody().getVehicleAverageEmissions()).hasSize(6);
		CompanyVehicleAverageEmission emission = responseEmission.getBody().getVehicleAverageEmissions().stream().filter(c -> c.getVehicleType().equals(VehicleType.DUMP_TRUCK.name())).findFirst().get();
		assertThat(emission.getAverageEmission()).isEqualTo(40000);
		assertThat(emission.getTotalEmissions()).isEqualTo(120000);
		assertThat(emission.getTotalVehicles()).isEqualTo(3);
		assertThat(emission.getTotalMileages()).isEqualTo(300);
		//and check for each all types that are in csv file
	}


	private AuthenticationResponse registerAdminUser() throws InvalidUsernameOrPasswordException {
		return authenticationService.authenticate(TestUtils.getAuthenticationRequest());
	}
}
