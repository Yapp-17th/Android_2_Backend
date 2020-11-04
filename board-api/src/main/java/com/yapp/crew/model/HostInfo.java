package com.yapp.crew.model;

import com.yapp.crew.domain.model.Evaluation;
import com.yapp.crew.domain.model.User;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HostInfo {

  private Long hostId;
  private String hostName;
  private Long likes;
  private Long dislikes;

  public static HostInfo build(User user, List<Evaluation> evaluationList) {
    HostInfo hostInfo = new HostInfo();
    hostInfo.hostId = user.getId();
    hostInfo.hostName = user.getNickname();

    hostInfo.likes = user.calculateLikes(evaluationList);
    hostInfo.dislikes = user.calculateDislikes(evaluationList);

    return hostInfo;
  }
}
