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
public class UserTagListController {

  @GetMapping(path = "/v1/user/tag")
  public EnumListDto getUserTagList() {
    try {
      List<String> exerciseList = EnumToList.userTypeEnumToList();
      EnumListSuccessDto enumListSuccessDto = EnumListSuccessDto.builder(ResponseDomain.USER_TAG.getName());
      enumListSuccessDto.setData(exerciseList);

      return EnumListDto.pass(enumListSuccessDto);
    } catch (Exception e) {
      EnumListFailDto enumListFailDto = EnumListFailDto.builder(ResponseDomain.USER_TAG.getName());
      enumListFailDto.addMessage(e.getMessage());

      return EnumListDto.fail(enumListFailDto);
    }
  }
}
