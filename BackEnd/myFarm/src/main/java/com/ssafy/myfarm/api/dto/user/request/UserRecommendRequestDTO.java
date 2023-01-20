package com.ssafy.myfarm.api.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRecommendRequestDTO {

    private String userid;
    private String magnitude;
    private String color;
    private String season;
    private String price;
    private String size;
    private String period;
}