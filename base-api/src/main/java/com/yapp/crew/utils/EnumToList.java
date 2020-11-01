package com.yapp.crew.utils;

import com.yapp.crew.domain.type.CityType;
import com.yapp.crew.domain.type.ExerciseType;
import com.yapp.crew.domain.type.ReportType;
import com.yapp.crew.domain.type.UserTag;
import java.util.ArrayList;
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

  public static List<String> boardReportTypeEnumToList() {
    ArrayList<String> boardReportTypeList = new ArrayList<>();
    for (ReportType reportType : ReportType.values()) {
      if (reportType.toString().startsWith("BOARD")) {
        boardReportTypeList.add(reportType.getName());
      }
    }
    boardReportTypeList.add(ReportType.OTHERS.getName());
    return boardReportTypeList;
  }

  public static List<String> userReportTypeEnumToList() {
    ArrayList<String> userReportTypeList = new ArrayList<>();
    for (ReportType reportType : ReportType.values()) {
      if (reportType.toString().startsWith("USER")) {
        userReportTypeList.add(reportType.getName());
      }
    }
    userReportTypeList.add(ReportType.OTHERS.getName());
    return userReportTypeList;
  }
}
