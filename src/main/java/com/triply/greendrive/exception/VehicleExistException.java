package com.triply.greendrive.exception;

import com.triply.greendrive.config.infrastructure.ResultStatus;
import jakarta.validation.constraints.NotNull;

public class VehicleExistException extends BusinessException {
    public VehicleExistException(@NotNull String idCode) {
        super(idCode);
    }

    @Override
    public ResultStatus getResultStatus() {
        return ResultStatus.VEHICLE_EXIST_EXCEPTION;
    }


}
