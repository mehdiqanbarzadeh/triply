package com.triply.greendrive.exception;

import java.util.function.Supplier;

import com.triply.greendrive.config.infrastructure.ResultStatus;

public class CompanyNotExistException extends BusinessException implements Supplier<CompanyNotExistException> {
	public CompanyNotExistException(String message) {
		super(message);
	}

	@Override
	public ResultStatus getResultStatus() {
		return null;
	}

	@Override
	public CompanyNotExistException get() {
		return this;
	}
}
