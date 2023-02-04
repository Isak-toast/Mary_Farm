package com.ssafy.maryfarmcalendarservice.repository;

import com.ssafy.maryfarmcalendarservice.domain.calendar.Calendar;
import com.ssafy.maryfarmcalendarservice.domain.calendar.CalendarID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar, CalendarID> {
    @Query(value = "select *" +
            " from calendar" +
            " where calendar.user_id = :userId" +
            " and ((year(calendar.regist_date) = :year) and (month(calendar.regist_date) = :month) and (day(calendar.regist_date) = :day))",nativeQuery = true)
    List<Calendar> findCalendar(@Param("userId") String userId, @Param("year") Integer year,
                                @Param("month") Integer month, @Param("day") Integer day);
}
