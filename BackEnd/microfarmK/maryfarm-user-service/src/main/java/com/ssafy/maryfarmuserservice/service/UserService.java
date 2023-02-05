package com.ssafy.maryfarmuserservice.service;

import com.ssafy.maryfarmuserservice.domain.Land;
import com.ssafy.maryfarmuserservice.domain.user.Recommend;
import com.ssafy.maryfarmuserservice.domain.user.User;
import com.ssafy.maryfarmuserservice.repository.jpa.RecommendRepository;
import com.ssafy.maryfarmuserservice.repository.jpa.UserRepository;
import com.ssafy.maryfarmuserservice.repository.mongo.MongoUserRepository;
import com.ssafy.maryfarmuserservice.repository.mongo.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final MongoUserRepository mongoUserRepository;
    private final RecommendRepository recommendRepository;
    private final MongoTemplate mongoTemplate;

    @Transactional
    public User saveUser(final User user) {
        User saveUser = userRepository.save(user);
        return saveUser;
    }
    @Cacheable(value = "user", key = "#id")
    public User findUser(final String id) {
        return userRepository.findById(id).get();
    }

    public UserInfoDTO findUserByM(final String id) {
        return mongoUserRepository.findById(id).get();
    }

    public User loginUser(final String kakaoid) {
        Optional<User> user = userRepository.findById(kakaoid);
        if(user.isPresent()) {
            return user.get();
        }
        return null;
    }
    @CachePut(value = "user", key = "#id")
    @Transactional
    public User updateUser(final String id, final String nickname, final String profilePath) {
        Optional<User> user = userRepository.findById(id);
        user.get().setNickname(nickname);
        user.get().setProfilePath(profilePath);
        return user.get();
    }

    @Transactional
    public User registLand(String id, String latitude, String longitude) {
        Optional<User> user = userRepository.findById(id);
        Land land = new Land(latitude, longitude);
        user.get().setLand(land);
        return user.get();
    }

    @Transactional
    public Recommend saveRecommend(String id, String fullCode) {
        Optional<User> user = userRepository.findById(id);
        Recommend recommend = Recommend.of(user.get(), fullCode);
        Recommend saveRecommend = recommendRepository.save(recommend);
        return saveRecommend;
    }

    public List<User> searchFollowers(String userId) {
        return userRepository.findFollower(userId);
    }
}
