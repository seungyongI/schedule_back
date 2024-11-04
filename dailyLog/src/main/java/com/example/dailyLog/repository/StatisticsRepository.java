package com.example.dailyLog.repository;

import com.example.dailyLog.dto.response.StatisticsMonthDto;
import com.example.dailyLog.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StatisticsRepository extends JpaRepository<Diary, Long> {

    @Query("SELECT d.category, COUNT(d) " +
            "FROM Diary d JOIN d.calendars c JOIN c.user u " +
            "WHERE u.idx = :userIdx " +
            "GROUP BY d.category")
    List<Object[]> findCategoryCountsAsObjectArray(@Param("userIdx") Long userIdx);

    @Query("SELECT COUNT(d) FROM Diary d JOIN d.calendars c JOIN c.user u WHERE u.idx = :userIdx")
    Long findTotalCountByUser(@Param("userIdx") Long userIdx);

    // 월별 전체 일기 작성 비율을 가져오는 쿼리
    @Query("SELECT FUNCTION('DATE_FORMAT', d.date, '%Y-%m') AS month, COUNT(d) AS diaryCount " +
            "FROM Diary d " +
            "JOIN d.calendars c " +
            "JOIN c.user u " +
            "WHERE u.idx = :userIdx " +
            "GROUP BY month " +
            "ORDER BY month")
    List<Object[]> findMonthlyDiaryCounts(@Param("userIdx") Long userIdx);

    // 카테고리별 작성 일기 수 조회
    @Query("SELECT d.category, COUNT(d), 0.0 " +
            "FROM Diary d " +
            "JOIN d.calendars c " +
            "JOIN c.user u " +
            "WHERE u.idx = :userIdx " +
            "GROUP BY d.category")
    List<Object[]> findCategoryCountsByUser(@Param("userIdx") Long userIdx);

}
