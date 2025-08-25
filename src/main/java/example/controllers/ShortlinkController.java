package example.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ShortlinkController {
    public String createShortLink(@RequestParam String originalUrl) {
        return "shortLink";
    }

    public void increaseView(String shortLink) {

    }
    public ResponseEntity getDetails(String shortLink) {
        return new ResponseEntity(  Map.of(
            "originalUrl", "https://example.com",
            "shortLink", shortLink,
            "views", 100
        ), null, 200);
    }

}
