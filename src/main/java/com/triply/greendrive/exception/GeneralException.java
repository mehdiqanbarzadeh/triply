package com.triply.greendrive.exception;

import com.triply.greendrive.config.infrastructure.ResultStatus;

public class GeneralException extends BusinessException {

	public GeneralException(String message) {
		super(message);
	}

	@Override
	public ResultStatus getResultStatus() {
		return ResultStatus.UNKNOWN;
	}
}
