package com.project.mungfriend.repository;

import com.project.mungfriend.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Repository
public class PhoneCheckNumberRepository {
    private final StringRedisTemplate stringRedisTemplate;
    private ValueOperations<String, String> opsHashPhoneCheckNum;
    private static final String PREFIX = "PHONE_CHECK_NUM:";
    private final int LIMIT_TIME = 3 * 60;

    @PostConstruct
    private void init() {
        opsHashPhoneCheckNum = stringRedisTemplate.opsForValue();
    }

    public void savePhoneCheckNum(String phoneNum, String code){
        opsHashPhoneCheckNum
                .set(PREFIX + phoneNum, code, Duration.ofSeconds(LIMIT_TIME));
    }
    public String getPhoneCheckNum(String phoneNum){
        return opsHashPhoneCheckNum.get(PREFIX + phoneNum);
    }

    public void removePhoneCheckNum(String phoneNum){
        stringRedisTemplate.delete(PREFIX + phoneNum);
    }

}
