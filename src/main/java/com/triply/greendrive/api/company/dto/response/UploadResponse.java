package com.triply.greendrive.api.company.dto.response;


import com.triply.greendrive.config.infrastructure.ResponseService;
import lombok.*;


@Data
@EqualsAndHashCode(callSuper = true)
public class UploadResponse extends ResponseService {

    private String message;
}
