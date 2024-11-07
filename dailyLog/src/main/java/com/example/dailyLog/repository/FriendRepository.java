package com.example.dailyLog.repository;

import com.example.dailyLog.constant.Status;
import com.example.dailyLog.entity.Friend;
import com.example.dailyLog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    // 특정 유저가 받은 모든 친구 요청 조회 (수락 대기 상태)
    List<Friend> findByReceiverAndStatus(User receiver, Status status);

    // 두 유저 간의 친구 상태 조회 (중복 요청 방지)
    Optional<Friend> findByRequesterAndReceiver(User requester, User receiver);
}
