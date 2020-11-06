package com.yapp.crew.controller;

import com.yapp.crew.utils.ResponseDomain;
import com.yapp.crew.dto.EnumListDto;
import com.yapp.crew.utils.EnumToList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressListController {

	@GetMapping(path = "/v1/address/city")
	public ResponseEntity<?> getAddressCityList() {

		List<String> addressList = EnumToList.addressEnumToList();
		EnumListDto enumListDto = EnumListDto.pass(ResponseDomain.ADDRESS_CITY.getName(), addressList);

		return ResponseEntity.ok().body(enumListDto);
	}
}
