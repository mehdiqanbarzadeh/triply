package com.triply.greendrive.api.company;


import java.io.IOException;

import com.triply.greendrive.api.company.dto.request.CompanyRegisterRequest;
import com.triply.greendrive.api.company.dto.response.CompanyEmissionsReportResponse;
import com.triply.greendrive.api.company.dto.response.CompanyResponse;
import com.triply.greendrive.api.company.dto.response.UploadResponse;
import com.triply.greendrive.api.company.mapper.CompanyMapper;
import com.triply.greendrive.exception.CompanyExistException;
import com.triply.greendrive.exception.CompanyNotExistException;
import com.triply.greendrive.exception.VehicleExistException;
import com.triply.greendrive.service.company.CompanyService;
import com.triply.greendrive.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyResource {

	private final CompanyService companyService;

	private final CompanyMapper companyMapper;

	@PostMapping("/register")
	public ResponseEntity<CompanyResponse> register(@RequestBody CompanyRegisterRequest request) throws CompanyExistException {
		return ResponseEntity.ok(companyService.register(companyMapper.toCompany(request)));
	}


	@PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UploadResponse> uploadFile(@RequestParam("companyCode") String companyCode,
			@RequestParam("file") MultipartFile file) throws CompanyExistException, VehicleExistException, IOException, CompanyNotExistException {
		String message = "";
		if (Utils.hasCSVFormat(file)) {
			companyService.uploadCSV(companyCode, file);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(companyMapper.toUploadResponse(message));
		}
		message = "Please upload a valid csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(companyMapper.toUploadResponse(message));
	}

	@GetMapping(value = "/emissions/{companyCode}")
	public ResponseEntity<CompanyEmissionsReportResponse> getEmissions(@PathVariable("companyCode") String companyCode) {
		return ResponseEntity.ok(companyService.getEmissions(companyCode));
	}
}
