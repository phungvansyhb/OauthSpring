package example.services;

import example.models.req.LoginedUserDTO;
import example.models.req.RegisterUserDTO;
import example.models.req.UserSSODTO;
import example.models.res.CreatedUserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public CreatedUserDTO registerUser(RegisterUserDTO registerUserDTO);

    public boolean loginUser(LoginedUserDTO loginedUserDTO);

    public void getUserDetails(Long userId);

    public void saveUserSSO(UserSSODTO userSSODTO);
}
