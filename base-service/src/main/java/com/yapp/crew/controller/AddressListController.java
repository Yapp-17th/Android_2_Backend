package com.yapp.crew.controller;

import com.yapp.crew.utils.ResponseDomain;
import com.yapp.crew.dto.EnumListResponseDto;
import com.yapp.crew.utils.EnumToList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "Base Service")
@RestController
public class AddressListController {

	@GetMapping(path = "/v1/address/city")
	public ResponseEntity<?> getAddressCityList() {
		log.info("Get Address City List");

		List<String> addressList = EnumToList.addressEnumToList();
		EnumListResponseDto enumListResponseDto = EnumListResponseDto.pass(ResponseDomain.ADDRESS_CITY.getName(), addressList);

		return ResponseEntity.ok().body(enumListResponseDto);
	}
}
