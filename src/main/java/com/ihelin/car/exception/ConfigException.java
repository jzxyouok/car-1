package com.ihelin.car.exception;

public class ConfigException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ConfigException(String errMsg) {
		super(errMsg);
	}

}
