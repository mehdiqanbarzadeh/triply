package com.triply.greendrive.exception;

import com.triply.greendrive.config.infrastructure.ResultStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class UserExistException extends BusinessException {

	public UserExistException(String message) {
		super(message);
	}

	@Override
	public ResultStatus getResultStatus() {
		return ResultStatus.UNKNOWN;
	}
}
