package top.piao888.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisClient  {
    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key){
        return (T)redisTemplate.opsForValue().get(key);
    }

    public void  set(String key,Object value){
        redisTemplate.opsForValue().set(key,value);
    }
    //具有超时功能的set   此处单位 秒
    public void  set(String key,Object value,int timeout){
        redisTemplate.opsForValue().set(key,value,timeout, TimeUnit.SECONDS);
    }
    //给key 添加过期时间
    public void expire(String key,int timeout){
        redisTemplate.expire(key,timeout,TimeUnit.SECONDS);
    }
}