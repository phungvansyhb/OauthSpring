package example.services.impl;

import example.models.req.LoginedUserDTO;
import example.models.User;
import example.models.req.RegisterUserDTO;
import example.models.req.UserSSODTO;
import example.models.res.CreatedUserDTO;
import example.repositories.UserRepository;
import example.services.UserService;
import example.utils.CryptographyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceIml implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceIml(@Autowired UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public CreatedUserDTO registerUser(RegisterUserDTO registerUserDTO) {
        // first check if the user already exists
        String username = registerUserDTO.getUsername();
        String password = registerUserDTO.getPassword();
        String email = registerUserDTO.getEmail();

        if (username == null || password == null || email == null) {
            throw new IllegalArgumentException("Username, password, and email cannot be null");
        }
        // check if the user already exists in the database
        boolean isUserExists = userRepository.existsByUsername(username) || userRepository.existsByEmail(email);
        if (isUserExists) {
            throw new IllegalArgumentException("User with this username or email already exists");
        }
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .createdAt(LocalDateTime.now())
                .build();

        User createdUser = userRepository.save(user);

        return CreatedUserDTO.builder()
                .id(createdUser.getId())
                .username(createdUser.getUsername())
                .email(createdUser.getEmail())
                .createdAt(createdUser.getCreatedAt())
                .build();
    }

    @Override
    public boolean loginUser(LoginedUserDTO loginedUserDTO) {
        // Implementation for logging in a user
        return true; // Assuming login is successful for demonstration
    }

    @Override
    public void getUserDetails(Long userId) {
        // Implementation for fetching user details
        System.out.println("Fetching details for user ID: " + userId);
    }

    @Override
    public void saveUserSSO(UserSSODTO userSSODTO) {
        // check if the user already exists
        String providerId = userSSODTO.getProviderId();
        String provider = userSSODTO.getProvider();
        boolean isUserExists = userRepository.existsByProviderAndProviderId(provider, providerId);
        if(!isUserExists){
            User user = User.builder()
                    .username(userSSODTO.getUsername())
                    .email(userSSODTO.getEmail())
                    .provider(provider)
                    .providerId(providerId)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(user);
        } else {
            System.out.println("User with this provider and provider ID already exists.");
        }
    }
}

