package com.yapp.crew.controller;

import com.yapp.crew.dto.EnumListDto;
import com.yapp.crew.dto.EnumListFailDto;
import com.yapp.crew.dto.EnumListSuccessDto;
import com.yapp.crew.utils.EnumToList;
import com.yapp.crew.utils.ResponseDomain;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserReportTypeListController {

  @GetMapping(path = "/v1/user/report/type")
  public EnumListDto getUserReportTypeList() {
    try {
      List<String> addressList = EnumToList.userReportTypeEnumToList();
      EnumListSuccessDto enumListSuccessDto= EnumListSuccessDto.builder(ResponseDomain.USER_REPORT_TYPE.getName());
      enumListSuccessDto.setData(addressList);

      return EnumListDto.pass(enumListSuccessDto);
    } catch (Exception e) {
      EnumListFailDto enumListFailDto=EnumListFailDto.builder(ResponseDomain.USER_REPORT_TYPE.getName());
      enumListFailDto.addMessage(e.getMessage());

      return EnumListDto.fail(enumListFailDto);
    }
  }
}
