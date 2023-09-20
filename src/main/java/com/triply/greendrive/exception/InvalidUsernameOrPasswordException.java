package com.triply.greendrive.exception;

import com.triply.greendrive.config.infrastructure.ResultStatus;

import java.util.function.Supplier;

public class InvalidUsernameOrPasswordException extends BusinessException implements Supplier<InvalidUsernameOrPasswordException> {
    public InvalidUsernameOrPasswordException(String s) {
        super(s);
    }
    @Override
    public ResultStatus getResultStatus() {
        return ResultStatus.USER_NOT_FOUND_EXCEPTION;
    }

    @Override
    public InvalidUsernameOrPasswordException get() {
        return this;
    }
}
