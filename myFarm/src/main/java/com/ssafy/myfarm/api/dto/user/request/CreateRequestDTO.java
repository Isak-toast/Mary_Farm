package com.ssafy.myfarm.api.dto.user.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRequestDTO {
    private String id;
    private String email;
    private String password;
    private String nickname;
    private String birthday;
}
