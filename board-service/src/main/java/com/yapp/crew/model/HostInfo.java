package com.yapp.crew.model;

import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.User;
import com.yapp.crew.domain.status.UserStatus;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HostInfo {

	private Long hostId;
	private String hostName;
	private Long likes;
	private Long dislikes;
	private String intro;

	public static HostInfo emptyBody() {
		HostInfo hostInfo = new HostInfo();
		hostInfo.hostId = -1L;
		hostInfo.hostName = "(알수없음)";
		hostInfo.likes = 0L;
		hostInfo.dislikes = 0L;
		hostInfo.intro = "";
		return hostInfo;
	}

	public static HostInfo build(User user, List<Evaluation> evaluationList) {

		if (user.isValidUser()) {
			HostInfo hostInfo = new HostInfo();
			hostInfo.hostId = user.getId();
			hostInfo.hostName = user.getNickname();
			hostInfo.likes = user.calculateLikes(evaluationList);
			hostInfo.dislikes = user.calculateDislikes(evaluationList);
			hostInfo.intro = user.getIntro();

			return hostInfo;
		}

		return HostInfo.emptyBody();
	}
}
