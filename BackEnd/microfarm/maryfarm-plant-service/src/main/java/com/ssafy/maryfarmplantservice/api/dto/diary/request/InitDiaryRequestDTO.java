package com.ssafy.maryfarmplantservice.api.dto.diary.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitDiaryRequestDTO {
    private String userId;
    private String name;
    private String title;
    private String content;
}
