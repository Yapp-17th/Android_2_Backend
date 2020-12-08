package com.yapp.crew.domain.errors;

public class ReportCodeNotFoundException extends RuntimeException {

	public ReportCodeNotFoundException(long reportCode) {
		super("Cannot find tag with id: " + reportCode);
	}
}
