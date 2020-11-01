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
public class BoardReportTypeListController {

  @GetMapping(path = "/v1/board/report/type")
  public EnumListDto getBoardReportTypeList() {
    try {
      List<String> addressList = EnumToList.boardReportTypeEnumToList();
      EnumListSuccessDto enumListSuccessDto= EnumListSuccessDto.builder(ResponseDomain.BOARD_REPORT_TYPE.getName());
      enumListSuccessDto.setData(addressList);

      return EnumListDto.pass(enumListSuccessDto);
    } catch (Exception e) {
      EnumListFailDto enumListFailDto=EnumListFailDto.builder(ResponseDomain.BOARD_REPORT_TYPE.getName());
      enumListFailDto.addMessage(e.getMessage());

      return EnumListDto.fail(enumListFailDto);
    }
  }
}
