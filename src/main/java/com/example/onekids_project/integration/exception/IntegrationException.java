package com.example.onekids_project.integration.exception;

public class IntegrationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 411441485657412987L;

	public IntegrationException(String errMsg) {
		super(errMsg);
	}

	public IntegrationException(Throwable throwable) {
		super(throwable);
	}
}
