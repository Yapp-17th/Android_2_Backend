package com.yapp.crew.controller;

import com.yapp.crew.dto.EnumListDto;
import com.yapp.crew.utils.EnumToList;
import com.yapp.crew.utils.ResponseDomain;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserReportTypeListController {

  @GetMapping(path = "/v1/user/report/type")
  public ResponseEntity<?> getUserReportTypeList() {

    List<String> userReportTypeList = EnumToList.userReportTypeEnumToList();
    EnumListDto enumListDto = EnumListDto.pass(ResponseDomain.USER_REPORT_TYPE.getName(), userReportTypeList);

    return ResponseEntity.ok().body(enumListDto);
  }
}
