package example.services.impl;

import example.configs.RedisConf;
import example.services.ShortLinkService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import redis.clients.jedis.UnifiedJedis;

import java.util.Map;


@Service
public class ShortLinkImp implements ShortLinkService {

    private final UnifiedJedis jedis;

    public ShortLinkImp(RedisConf jedis) {
        this.jedis = jedis.jedis();
    }

    @Override
    public String generateShortLink(String originalUrl) {
        String existingShortLink = jedis.get("url:" + originalUrl);
        if (existingShortLink != null) {
            return existingShortLink;
        }
        String shortLink = originalUrl.substring(5);
        jedis.hset(shortLink, "originLink", originalUrl);
        jedis.hset(shortLink, "views", "0");
        jedis.set("url:" + originalUrl, shortLink);
        return shortLink;
    }

    @Override
    public ResponseEntity getUrlInfo(String shortLink) {
        if (jedis.hget(shortLink,"originLink") != null) {
            var originLink = jedis.hget(shortLink, "originLink");
            var views = jedis.hget(shortLink, "views");
            return new ResponseEntity(Map.of(
                    "originalUrl", originLink,
                    "shortLink", shortLink,
                    "views", views
            ), null, 200);
        } else return new ResponseEntity(null, null, 404);
    }

    @Override
    public void increaseViewCount(String shortLink) {
        if (jedis.hget(shortLink,"views") != null) {
            jedis.hincrBy(shortLink, "views", 1);
        }
    }
}
