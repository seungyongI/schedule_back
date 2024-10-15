package com.example.dailyLog.repository;

import com.example.dailyLog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
