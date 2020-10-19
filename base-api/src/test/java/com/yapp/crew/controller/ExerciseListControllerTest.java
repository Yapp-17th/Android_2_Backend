package com.yapp.crew.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.yapp.crew.dto.EnumListDto;
import com.yapp.crew.service.EnumToList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {EnumToList.class, ExerciseListController.class})
public class ExerciseListControllerTest {

  @Autowired
  private ExerciseListController exerciseListController;

  @Test
  void getMaxProfitDates(){
    EnumListDto enumListDto = exerciseListController.getExerciseList();
    assertNotNull(enumListDto.getResult());
    assertNull(enumListDto.getError());
    assertEquals("exercise", enumListDto.getResult().getType());
    assertEquals(enumListDto.getResult().getStatus(), 200);
    assertNotNull(enumListDto.getResult().getData());
  }
}
