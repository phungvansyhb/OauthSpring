package example.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ShortLinkService {
    public String generateShortLink(String originalUrl);
    public ResponseEntity getUrlInfo(String shortLink);
    public void increaseViewCount(String shortLink);
}
