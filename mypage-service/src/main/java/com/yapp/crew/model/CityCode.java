package com.yapp.crew.model;

import com.yapp.crew.domain.model.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CityCode {

	private long id = -1L;
	private String name = "";

	public static CityCode build(long id, String name) {
		CityCode cityCode = new CityCode();
		return cityCode;
	}

	public static CityCode build(Address address) {
		CityCode cityCode = new CityCode();
		cityCode.id = address.getId();
		cityCode.name = address.getCity().getName();

		return cityCode;
	}
}
