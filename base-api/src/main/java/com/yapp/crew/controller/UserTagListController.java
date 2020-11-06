package com.yapp.crew.controller;

import com.yapp.crew.dto.EnumListDto;
import com.yapp.crew.utils.EnumToList;
import com.yapp.crew.utils.ResponseDomain;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserTagListController {

  @GetMapping(path = "/v1/user/tag")
  public ResponseEntity<?> getUserTagList() {

    List<String> userTagList = EnumToList.userTypeEnumToList();
    EnumListDto enumListDto = EnumListDto.pass(ResponseDomain.USER_TAG.getName(), userTagList);

    return ResponseEntity.ok().body(enumListDto);
  }
}
