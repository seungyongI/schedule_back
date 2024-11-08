package com.example.dailyLog.controller;

import com.example.dailyLog.dto.request.ShareDiaryGroupCreateRequestDto;
import com.example.dailyLog.dto.response.ShareDiaryGroupResponseDto;
import com.example.dailyLog.entity.ShareDiaryGroup;
import com.example.dailyLog.service.ShareDiaryGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shareDiaryGroup")
@RequiredArgsConstructor
public class ShareDiaryGroupController {

    private final ShareDiaryGroupService shareDiaryGroupService;

    // 그룹 만들기
    @PostMapping("/create")
    public ResponseEntity<ShareDiaryGroup> createGroup(@RequestBody ShareDiaryGroupCreateRequestDto shareDiaryGroupCreateRequestDto) {
        ShareDiaryGroup shareDiaryGroup = shareDiaryGroupService.createGroup(shareDiaryGroupCreateRequestDto);
        return ResponseEntity.ok(shareDiaryGroup);
    }

    // 그룹 조회
    @GetMapping("/{idx}")
    public ResponseEntity<ShareDiaryGroupResponseDto> getGroupById(@PathVariable(name = "idx") Long groupId) {
        ShareDiaryGroupResponseDto shareDiaryGroupResponseDto = shareDiaryGroupService.getGroupById(groupId);
        return ResponseEntity.ok(shareDiaryGroupResponseDto);
    }

    // 그룹 삭제
    @DeleteMapping("/delete/{idx}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        shareDiaryGroupService.deleteGroup(groupId);
        return ResponseEntity.noContent().build();
    }
}

