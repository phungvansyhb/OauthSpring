package example.controllers;

import example.exceptions.ResouceNotFoundException;
import example.models.req.LoginedUserDTO;
import example.models.res.LoginSuccessUser;
import example.services.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class RestAuthController {
    private final UserService userService;

    public RestAuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginSuccessUser login(@RequestBody LoginedUserDTO loginedUserDTO) throws ResouceNotFoundException {

          return userService.loginUser(loginedUserDTO);
    }
}
