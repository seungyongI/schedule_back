package com.example.dailyLog.service;

import com.example.dailyLog.constant.Status;
import com.example.dailyLog.dto.response.FriendListResponseDto;
import com.example.dailyLog.dto.response.UserSearchResponseDto;
import com.example.dailyLog.entity.Friend;
import com.example.dailyLog.entity.User;
import com.example.dailyLog.repository.FriendRepository;
import com.example.dailyLog.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService{

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    // 친구 요청 보내기
    @Transactional
    @Override
    public void sendFriendRequest(Long requesterId, Long receiverId) {
        User requester = userRepository.findById(requesterId).orElseThrow(() -> new IllegalArgumentException("Invalid requester ID"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new IllegalArgumentException("Invalid receiver ID"));

        // 중복 요청 방지 로직
        if (friendRepository.findByRequesterAndReceiver(requester, receiver).isPresent()) {
            throw new IllegalStateException("Already sent a friend request to this user.");
        }

        Friend friendRequest = new Friend();
        friendRequest.setRequester(requester);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus(Status.PENDING);
        friendRepository.save(friendRequest);
    }

    //친구 요청 목록 조회
    @Override
    @Transactional
    public List<UserSearchResponseDto> getFriendRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        List<Friend> friendRequests = friendRepository.findByReceiverAndStatus(user, Status.PENDING);

        return friendRequests.stream()
                .map(friend -> {
                    User requester = friend.getRequester();
                    return new UserSearchResponseDto(
                            requester.getIdx(),
                            requester.getUserName(),
                            requester.getProfileImage() != null ? requester.getProfileImage().getImgUrl() : "/images/default.png", // 기본 이미지 경로 설정
                            requester.getEmail()
                    );
                })
                .collect(Collectors.toList());
    }

    // 친구 요청 수락
    @Transactional
    @Override
    public void acceptFriendRequest(Long requesterId, Long receiverId) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid requester ID"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid receiver ID"));

        // 요청자와 수신자 간의 기존 요청을 수락
        Friend friendRequest = friendRepository.findByRequesterAndReceiver(requester, receiver)
                .orElseThrow(() -> new IllegalArgumentException("Invalid friend request"));
        friendRequest.setStatus(Status.ACCEPTED);
        friendRepository.save(friendRequest);

        // 반대 방향의 관계도 추가 (receiver가 requester의 친구로 추가됨)
        Friend reverseFriendRequest = new Friend();
        reverseFriendRequest.setRequester(receiver); // 반대 방향의 요청자
        reverseFriendRequest.setReceiver(requester); // 반대 방향의 수신자
        reverseFriendRequest.setStatus(Status.ACCEPTED);
        friendRepository.save(reverseFriendRequest); // 새로운 관계 저장
    }

    // 친구 요청 거절
    @Transactional
    @Override
    public void rejectFriendRequest(Long friendRequestId) {
        Friend friendRequest = friendRepository.findById(friendRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid friend request ID"));
        friendRequest.setStatus(Status.REJECTED);
        friendRepository.save(friendRequest);
    }

    // 친구 목록 조회
    @Transactional
    @Override
    public List<FriendListResponseDto> getFriendsList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        List<Friend> friends = friendRepository.findByReceiverAndStatus(user, Status.ACCEPTED);

        return friends.stream()
                .map(friend -> {
                    User friendUser = friend.getRequester().getIdx().equals(userId) ? friend.getReceiver() : friend.getRequester();
                    return new FriendListResponseDto(friendUser.getIdx(), friendUser.getUserName());
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserSearchResponseDto> searchUsersByUserName(String userName) {
        return userRepository.findByUserNameContaining(userName).stream()
                .map(user -> new UserSearchResponseDto(
                        user.getIdx(),
                        user.getUserName(),
                        user.getProfileImage() != null ? user.getProfileImage().getImgUrl() : "/images/default.png", // 기본 이미지 경로
                        user.getEmail()
                ))
                .collect(Collectors.toList());
    }
}

