package com.yapp.crew.domain.errors;

public class AddressNotFoundException extends RuntimeException {

	public AddressNotFoundException(Long addressId) {
		super("Cannot find address with id: " + addressId);
	}
}
