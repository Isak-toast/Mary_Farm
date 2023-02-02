package com.ssafy.maryfarmplantservice.api.dto.diary.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyDiaryRequestDTO {
    private String diaryId;
    private String content;
}
