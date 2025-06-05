package example.controllers;

import example.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Controller
public class AuthenControler {

    User userSystem = new User("admin", "admin", "true", "en");

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @PostMapping("/login")
    public String handleSubmit(@ModelAttribute("user") User user , Model model) {

        if(Objects.equals(user.getUsername(), userSystem.getUsername()) &&
           user.getPassword().equals(userSystem.getPassword())) {
            model.addAttribute("user", user);
            return "welcome";
        }
        else{
            model.addAttribute("error", "Invalid username or password");
            return "index";

        }

    }

    @GetMapping("/success")
    public String loginSuccess(Model model, @AuthenticationPrincipal OAuth2User principal) {
        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));
        return "welcome";
    }

}
