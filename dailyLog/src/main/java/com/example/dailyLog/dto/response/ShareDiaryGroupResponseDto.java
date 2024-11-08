package com.example.dailyLog.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ShareDiaryGroupResponseDto {

    private Long idx;

    private String groupName;

    private String UserName;

    private List<String> participantNames;

}
