package com.example.dailyLog.service;

import com.example.dailyLog.dto.request.ShareDiaryGroupCreateRequestDto;
import com.example.dailyLog.dto.response.ShareDiaryGroupResponseDto;
import com.example.dailyLog.entity.ShareDiaryGroup;

public interface ShareDiaryGroupService {
    ShareDiaryGroup createGroup(ShareDiaryGroupCreateRequestDto shareDiaryGroupCreateRequestDto);
    ShareDiaryGroupResponseDto getGroupById(Long groupId);
    void deleteGroup(Long groupId);



}
