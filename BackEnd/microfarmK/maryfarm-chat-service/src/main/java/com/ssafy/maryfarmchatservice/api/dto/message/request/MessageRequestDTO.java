package com.ssafy.maryfarmchatservice.api.dto.message.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDTO {
    private String roomId;
    private String userId;
    private String content;
}
