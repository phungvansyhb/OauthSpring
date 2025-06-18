package example.services;

import example.exceptions.ResouceNotFoundException;
import example.models.User;
import example.models.req.LoginedUserDTO;
import example.models.req.RegisterUserDTO;
import example.models.req.UserSSODTO;
import example.models.res.CreatedUserDTO;
import example.models.res.LoginSuccessUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public CreatedUserDTO registerUser(RegisterUserDTO registerUserDTO);


    public LoginSuccessUser loginUser(LoginedUserDTO loginedUserDTO) throws ResouceNotFoundException;

    public void getUserDetails(Long userId);

    public void saveUserSSO(UserSSODTO userSSODTO);

    public List<User> getAllUsers();
}
