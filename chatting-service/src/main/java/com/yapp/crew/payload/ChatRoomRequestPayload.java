package com.yapp.crew.payload;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequestPayload {

	@NotNull
  private Long hostId;

  private Long guestId;

  @NotNull
  private Long boardId;
}
