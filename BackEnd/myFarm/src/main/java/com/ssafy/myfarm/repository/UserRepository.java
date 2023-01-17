package com.ssafy.myfarm.repository;

import com.ssafy.myfarm.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT u" +
            " FROM User u" +
            " RIGHT JOIN Follow f ON f.senderUser.id = u.id" +
            " WHERE f.receiverUser.id = :id")
    List<User> findFollower(@Param("id") Long userId);
}
