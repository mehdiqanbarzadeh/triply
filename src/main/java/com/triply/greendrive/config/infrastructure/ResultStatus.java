package com.triply.greendrive.config.infrastructure;

import java.io.IOException;
import java.util.Properties;


public enum ResultStatus {

	SUCCESS(0, "success"),
	UNKNOWN(1, "unknownerror"),
	COMPANY_EXIST(2, "company.with.name.exist"),
	VEHICLE_EXIST_EXCEPTION(3, "vehicle.exist.exception"),
	COMPANY_EXIST_WITH_OTHER_COMPANY_NAME(4, "company.exist.with.other.company.name"),
	INVALID_PARAMETER(5, "core.invalid.parameter.exception"),
    USER_NOT_FOUND_EXCEPTION(6, "invalid.email.or.password.exception");

	private final String description;

	private final Integer status;

	ResultStatus(int status, String description) {
		this.status = status;
		String errorMsg = MessageHolder.ERROR_MESSAGE_PROPERTIES.getProperty(description);
		this.description = errorMsg != null ? errorMsg : description;
	}

	public String getDescription() {
		return description;
	}

	public Integer getStatus() {
		return status;
	}

	private static final class MessageHolder {
		private static final Properties ERROR_MESSAGE_PROPERTIES = new Properties();

		static {
			try {
				ERROR_MESSAGE_PROPERTIES.load((ResultStatus.class.getResourceAsStream("/error-messages.properties")));
			} catch (IOException e) {
				throw new ExceptionInInitializerError(e);
			}
		}
	}
}
