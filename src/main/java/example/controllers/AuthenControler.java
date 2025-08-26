package example.controllers;

import example.models.req.LoginedUserDTO;
import example.models.req.RegisterUserDTO;
import example.models.req.UserSSODTO;
import example.models.res.CreatedUserDTO;
import example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenControler {

    private final UserService userService;

    public AuthenControler(@Autowired UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("loginedUserDTO", new LoginedUserDTO());
        return "index";
    }


    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new RegisterUserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("user") RegisterUserDTO registerUserDTO, Model model) {
        CreatedUserDTO user = this.userService.registerUser(registerUserDTO);
        model.addAttribute("loginedUserDTO", LoginedUserDTO.builder()
                .username(user.getUsername())
                .build());
        return "redirect:/";
    }

    @GetMapping("/success")
    public String loginSuccess(Model model, @AuthenticationPrincipal OAuth2User principal) {
        model.addAttribute("name", principal.getAttribute("name"));
        model.addAttribute("email", principal.getAttribute("email"));

        userService.saveUserSSO(UserSSODTO.builder()
                .username(principal.getAttribute("name"))
                .email(principal.getAttribute("email"))
                .provider("google") // Assuming Google as the provider
//                .provider(principal.getAttribute("provider"))
                .providerId(principal.getAttribute("sub"))
                .build());
        return "welcome";
    }

    @GetMapping("/admin/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

}
