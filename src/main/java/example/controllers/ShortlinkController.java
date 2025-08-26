package example.controllers;

import example.services.ShortLinkService;
import example.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shortlinks")
public class ShortlinkController {

    private final ShortLinkService shortLinkService;
    private final UserService userService;

    public ShortlinkController(ShortLinkService shortLinkService, UserService userService) {
        this.shortLinkService = shortLinkService;
        this.userService = userService;
    }

    @GetMapping()
    public String createShortLink(@RequestParam(name = "originUrl") String originalUrl) {
        return shortLinkService.generateShortLink(originalUrl);
    }

    @GetMapping("/view")
    public void increaseView(@RequestParam(name = "shortlink") String shortLink) {
        shortLinkService.increaseViewCount(shortLink);
    }

    @GetMapping("/detail")
    public ResponseEntity getDetails(@RequestParam(name = "shortlink") String shortLink) {
        return shortLinkService.getUrlInfo(shortLink);
    }

    @GetMapping("/users-view/{id}")
    public void increaseViewCount(@PathVariable(name = "id") Long id) {
        userService.increaseView(id);
    }
}
