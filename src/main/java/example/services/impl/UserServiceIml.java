package example.services.impl;

import example.exceptions.ResouceNotFoundException;
import example.models.Role;
import example.models.req.LoginedUserDTO;
import example.models.User;
import example.models.req.RegisterUserDTO;
import example.models.req.UserSSODTO;
import example.models.res.CreatedUserDTO;
import example.models.res.LoginSuccessUser;
import example.repositories.RoleRepository;
import example.repositories.UserRepository;
import example.services.UserService;
import example.utils.CryptographyUtil;
import example.utils.JWTUtil;
import example.utils.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceIml implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    public UserServiceIml(@Autowired UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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

        Role defaultRole = roleRepository.getReferenceById(RoleEnum.USER.getRoleId());

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .role(defaultRole)
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
    public LoginSuccessUser loginUser(LoginedUserDTO loginedUserDTO) throws ResouceNotFoundException {
        var authentication = authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        loginedUserDTO.getUsername(), loginedUserDTO.getPassword()));
        User authUser = userRepository.findUserByUsername(loginedUserDTO.getUsername())
                .orElseThrow(() -> new ResouceNotFoundException("User not found with username: " + loginedUserDTO.getUsername()));

        SecurityContextHolder.getContext().setAuthentication(authentication);  // then get by Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String jwt = jwtUtil.generateToken(authUser); // Generate JWT token for the authenticated user
        return new LoginSuccessUser(authUser.getUsername(), authUser.getEmail() , jwt);
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
        if (!isUserExists) {
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

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

