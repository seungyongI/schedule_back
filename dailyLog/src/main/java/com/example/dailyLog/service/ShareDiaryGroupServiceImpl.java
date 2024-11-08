package com.example.dailyLog.service;

import com.example.dailyLog.dto.request.ShareDiaryGroupCreateRequestDto;
import com.example.dailyLog.dto.response.ShareDiaryGroupResponseDto;
import com.example.dailyLog.entity.ShareDiaryGroup;
import com.example.dailyLog.entity.User;
import com.example.dailyLog.repository.ShareDiaryGroupRepository;
import com.example.dailyLog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShareDiaryGroupServiceImpl implements ShareDiaryGroupService{

    private final ShareDiaryGroupRepository shareDiaryGroupRepository;
    private final UserRepository userRepository;

    //그룹 생성
    @Transactional
    @Override
    public ShareDiaryGroup createGroup(ShareDiaryGroupCreateRequestDto shareDiaryGroupCreateRequestDto) {
        User creator = userRepository.findById(shareDiaryGroupCreateRequestDto.getCreatorIdx())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + shareDiaryGroupCreateRequestDto.getCreatorIdx()));

        ShareDiaryGroup group = ShareDiaryGroup.builder()
                .creatorIdx(creator)
                .groupName(shareDiaryGroupCreateRequestDto.getGroupName())
                .build();
        return shareDiaryGroupRepository.save(group);
    }

    // 그룹 조회
    @Transactional
    @Override
    public ShareDiaryGroupResponseDto getGroupById(Long groupId) {
        ShareDiaryGroup group = shareDiaryGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // 친구 이름 목록 추출
        List<String> participantNames = group.getFriends().stream()
                .map(friendRelation -> friendRelation.getFriend().getRequester().getUserName())
                .collect(Collectors.toList());

        ShareDiaryGroupResponseDto shareDiaryGroupResponseDto= new ShareDiaryGroupResponseDto();
        shareDiaryGroupResponseDto.setIdx(group.getIdx());
        shareDiaryGroupResponseDto.setGroupName(group.getGroupName());
        shareDiaryGroupResponseDto.setUserName(group.getCreatorIdx().getUserName());
        shareDiaryGroupResponseDto.setParticipantNames(participantNames);

        return shareDiaryGroupResponseDto;
    }

    // 그룹 삭제
    @Transactional
    @Override
    public void deleteGroup(Long groupId) {
        ShareDiaryGroup group = shareDiaryGroupRepository
                .findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        shareDiaryGroupRepository.delete(group);
    }
}

