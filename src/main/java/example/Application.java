package example;

import example.configs.RedisConf;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.UnifiedJedis;

@SpringBootApplication
public class Application {
     public static void main(String[] args) {
         var context = SpringApplication.run(Application.class, args);
         UnifiedJedis jedis = context.getBean(UnifiedJedis.class);
         System.out.println("hello redis" + jedis.get("age"));
     }
}
