package com.yapp.crew.controller;

import com.yapp.crew.utils.ResponseDomain;
import com.yapp.crew.dto.EnumListDto;
import com.yapp.crew.dto.EnumListFailDto;
import com.yapp.crew.dto.EnumListSuccessDto;
import com.yapp.crew.utils.EnumToList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExerciseListController {

  @GetMapping(path = "/v1/exercise")
  public EnumListDto getExerciseList() {
    try {
      List<String> exerciseList = EnumToList.exerciseEnumToList();
      EnumListSuccessDto enumListSuccessDto= EnumListSuccessDto.builder(ResponseDomain.EXERCISE.getName());
      enumListSuccessDto.setData(exerciseList);

      return EnumListDto.pass(enumListSuccessDto);
    } catch (Exception e) {
      EnumListFailDto enumListFailDto=EnumListFailDto.builder("exercise");
      enumListFailDto.addMessage(e.getMessage());

      return EnumListDto.fail(enumListFailDto);
    }
  }
}
