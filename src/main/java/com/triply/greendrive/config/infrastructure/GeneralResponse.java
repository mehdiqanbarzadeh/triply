package com.triply.greendrive.config.infrastructure;

import lombok.ToString;

@ToString(callSuper = true)
public class GeneralResponse extends ResponseService {

	public GeneralResponse(ResultStatus resultStatus) {
		setResult(resultStatus);
	}


	public GeneralResponse(Result result) {
		setResult(result);
	}

	public static GeneralResponse success() {
		return of(ResultStatus.SUCCESS);
	}

	public static GeneralResponse of(ResultStatus resultStatus) {
		return new GeneralResponse(resultStatus);
	}

}
