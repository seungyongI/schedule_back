package com.example.dailyLog.dto.request;

import lombok.Data;

@Data
public class ShareDiaryGroupCreateRequestDto {

    private Long creatorIdx;
    private String groupName;

}
