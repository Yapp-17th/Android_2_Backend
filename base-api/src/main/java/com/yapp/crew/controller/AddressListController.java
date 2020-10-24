package com.yapp.crew.controller;

import com.yapp.crew.utils.ResponseDomain;
import com.yapp.crew.dto.EnumListDto;
import com.yapp.crew.dto.EnumListFailDto;
import com.yapp.crew.dto.EnumListSuccessDto;
import com.yapp.crew.service.EnumToList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddressListController {

  @GetMapping(path = "/v1/address/city")
  public EnumListDto getAddressCityList() {
    try {
      List<String> addressList = EnumToList.addressEnumToList();
      EnumListSuccessDto enumListSuccessDto= EnumListSuccessDto.builder(ResponseDomain.ADDRESSCITY.getName());
      enumListSuccessDto.setData(addressList);

      return EnumListDto.pass(enumListSuccessDto);
    } catch (Exception e) {
      EnumListFailDto enumListFailDto=EnumListFailDto.builder("city");
      enumListFailDto.addMessage(e.getMessage());

      return EnumListDto.fail(enumListFailDto);
    }
  }
}
