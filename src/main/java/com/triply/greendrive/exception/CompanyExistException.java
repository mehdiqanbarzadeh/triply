package com.triply.greendrive.exception;

import com.triply.greendrive.config.infrastructure.ResultStatus;

public class CompanyExistException extends BusinessException {
    public CompanyExistException(String message) {
        super(message);
    }


    @Override
    public ResultStatus getResultStatus() {
        return ResultStatus.COMPANY_EXIST;
    }
}
