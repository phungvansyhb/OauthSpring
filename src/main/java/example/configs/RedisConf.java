package example.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.UnifiedJedis;


/*
* Redis run in mode standalone has DB 0-15;
* Each DB is independent forexample use DB 0 for cache, DB 1 for session storage;
* At this mode , redis usually used as cache or session storage;
**************************
* Redis run in mode cluster has no DB;
* All data is stored in one big key space;
* At this mode , redis usually used as main database;
* */
@Configuration
public class RedisConf {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public UnifiedJedis jedis() {
//        return new UnifiedJedis("redis://localhost:6379");
        return new UnifiedJedis("redis://" + redisHost + ":" + redisPort);

    }
}
