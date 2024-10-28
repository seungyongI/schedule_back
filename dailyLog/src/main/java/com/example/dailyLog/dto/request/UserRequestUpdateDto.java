package com.example.dailyLog.dto.request;

import lombok.Data;

@Data
public class UserRequestUpdateDto {

    private Long idx;

    private String email;

    private String password;

    private String userName;

}
