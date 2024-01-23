package com.e3ps.project;

public class ControllerException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ControllerException() {
		super();
	}
	
	public ControllerException(String message) {
		super(message);
	}
	
	public ControllerException(Throwable e) {
		super(e);
	}
}
