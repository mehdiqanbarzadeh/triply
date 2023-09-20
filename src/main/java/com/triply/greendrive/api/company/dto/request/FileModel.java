package com.triply.greendrive.api.company.dto.request;


import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FileModel {

    private String employeeId;

    private String vehicleIdCode;

    private String vehicleType;

    private String vehicleMileage;

}
