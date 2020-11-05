package com.yapp.crew.utils;

import org.apache.commons.lang3.StringUtils;

public enum SortingType {
  REMAIN("remain"),
  LATEST("latest"),
  DEADLINE("deadline");

  private final String name;

  SortingType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public static SortingType getSortingType(final String name) {
    for (final SortingType sortingType : values()) {
      if (StringUtils.equalsIgnoreCase(sortingType.name, name)) {
        return sortingType;
      }
    }
    return LATEST;
  }
}
