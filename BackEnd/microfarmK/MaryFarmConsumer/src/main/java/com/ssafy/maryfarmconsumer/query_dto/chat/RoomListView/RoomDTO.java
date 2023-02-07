package com.ssafy.maryfarmconsumer.query_dto.chat.RoomListView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private String roomId;
    private String opponentId;
    private String opponentName;
    private String opponentProfilePath;
    private String latestMessage;
    private LocalDateTime latestTimestamp;

    public RoomDTO(Map<Object, Object> payload, String opRole) {
        this.roomId = (String) payload.get("room_id");
        this.opponentId = (String) payload.get(opRole+"_id");
        this.opponentName = (String) payload.get(opRole+"_name");
        this.opponentProfilePath = (String) payload.get(opRole+"_profile_path");
    }
}
