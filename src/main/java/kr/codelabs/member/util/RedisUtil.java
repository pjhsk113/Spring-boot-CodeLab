package kr.codelabs.member.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private static RedisTemplate redisTemplate;

    @Autowired
    public RedisUtil(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public static String generateMemberSeq() {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();

        String key = "MEMBER_SEQ:" + getCurrentYear();

        if (operations.get(key) == null) {
            operations.set(key, "0");
        }

        operations.increment(key, 1L);
        redisTemplate.expire(key, 370, TimeUnit.DAYS);

        return getCurrentYear() + "_" + String.format("%07d", Long.parseLong((String)operations.get(key)));
    }

    private static String getCurrentYear() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy");

        return now.format(dtf);
    }
}
