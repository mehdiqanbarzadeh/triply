package com.triply.greendrive.service.company;

import java.io.IOException;

import com.triply.greendrive.api.company.dto.response.CompanyEmissionsReportResponse;
import com.triply.greendrive.api.company.dto.response.CompanyResponse;
import com.triply.greendrive.exception.CompanyExistException;
import com.triply.greendrive.exception.CompanyNotExistException;
import com.triply.greendrive.exception.VehicleExistException;
import com.triply.greendrive.model.company.Company;

import org.springframework.web.multipart.MultipartFile;


public interface CompanyService {
    CompanyResponse register(Company toCompany) throws CompanyExistException;

    void uploadCSV(String companyCode,MultipartFile file) throws CompanyExistException, VehicleExistException, CompanyNotExistException, IOException;

    CompanyEmissionsReportResponse getEmissions(String companyCode);
}
