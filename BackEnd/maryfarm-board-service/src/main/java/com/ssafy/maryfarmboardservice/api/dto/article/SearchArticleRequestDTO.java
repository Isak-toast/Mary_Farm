package com.ssafy.maryfarmboardservice.api.dto.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchArticleRequestDTO {
    private String type;
}