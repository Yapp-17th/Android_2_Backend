package com.yapp.crew.model;

import com.yapp.crew.domain.status.AppliedStatus;
import com.yapp.crew.domain.status.BoardStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApplyStatusInfo {

	int code;
	String name;

	public static ApplyStatusInfo build(AppliedStatus appliedStatus) {
		ApplyStatusInfo applyStatusInfo = new ApplyStatusInfo();
		applyStatusInfo.code = appliedStatus.getCode();
		applyStatusInfo.name = appliedStatus.getName();

		return applyStatusInfo;
	}
}
