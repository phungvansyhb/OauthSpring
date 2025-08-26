package example;

import example.configs.RedisConf;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.UnifiedJedis;

@SpringBootApplication
public class Application {
     public static void main(String[] args) {
         SpringApplication.run(Application.class, args);
     }
}
