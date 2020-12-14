package com.yapp.crew.domain.type;

import org.apache.commons.lang3.StringUtils;

public enum HistoryType {
	CONTINUE("continue"),
	COMPLETE("complete");

	private final String name;

	HistoryType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static HistoryType getHistoryType(final String name) {
		for (final HistoryType historyType : values()) {
			if (StringUtils.equalsIgnoreCase(historyType.name, name)) {
				return historyType;
			}
		}
		return CONTINUE;
	}
}
