package com.yapp.crew.utils;

import com.yapp.crew.domain.type.CityType;
import com.yapp.crew.domain.type.ExerciseType;
import com.yapp.crew.domain.type.UserTag;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumToList {

  public static List<String> addressEnumToList() {
    return Arrays.stream(CityType.values()).map(CityType::getName).collect(Collectors.toList());
  }

  public static List<String> exerciseEnumToList() {
    return Arrays.stream(ExerciseType.values()).map(ExerciseType::getName).collect(Collectors.toList());
  }

  public static List<String> userTypeEnumToList() {
    return Arrays.stream(UserTag.values()).map(UserTag::getName).collect(Collectors.toList());
  }
}
