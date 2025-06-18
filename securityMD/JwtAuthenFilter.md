Dưới đây là tóm tắt các bước cần thiết để thêm JWT (JSON Web Token) vào Spring Security trong dự án Spring Boot 3.5.0 (với Spring Security 6.2.x), dựa trên hướng dẫn chi tiết trước đó. Mình sẽ trình bày ngắn gọn, rõ ràng, phù hợp với vai trò senior web dev và phong cách hỗ trợ bạn học Spring Framework.

---

### Các bước tóm tắt để thêm JWT vào Spring Security

1. **Thêm dependencies**:
    - Thêm `spring-boot-starter-security` và `jjwt` vào thư viện`:
2. **Tạo Utility cho JWT**:
    - Tạo class `JwtUtil` để tạo và xác minh token:
      ```java
      @Component
      public class JwtUtil {
          @Value("${jwt.secret}") private String secret;
          @Value("${jwt.expiration}") private long expiration;
 
          public String generateToken(User user) {
              return Jwts.builder()
                      .setSubject(user.getUsername())
                      .claim("role", user.getRole().getName())
                      .setIssuedAt(new Date())
                      .setExpiration(new Date(System.currentTimeMillis() + expiration))
                      .signWith(SignatureAlgorithm.HS512, secret)
                      .compact();
          }
 
          public boolean validateToken(String token, User user) {
              try {
                  Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
                  return claims.getSubject().equals(user.getUsername()) && !isTokenExpired(token);
              } catch (Exception e) {
                  return false;
              }
          }
 
          public String getUsernameFromToken(String token) {
              return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
          }
 
          private boolean isTokenExpired(String token) {
              return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration().before(new Date());
          }
      }
      ```

3. **Cấu hình `application.properties`**:
    - Thêm key secret và thời gian hết hạn:
      ```properties
      jwt.secret=your-very-secure-secret-key
      jwt.expiration=86400000 # 24 giờ (milliseconds)
      ```

4. **Cập nhật `CustomUserDetails` và `UserDetailsService`**:
    - Đảm bảo `CustomUserDetail` trả về quyền (roles):
      ```java
      public class CustomUserDetail implements UserDetails {
          private final User user;
          public CustomUserDetail(User user) { this.user = user; }
          @Override public Collection<? extends GrantedAuthority> getAuthorities() {
              return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName()));
          }
          @Override public String getPassword() { return user.getPassword(); }
          @Override public String getUsername() { return user.getUsername(); }
          @Override public boolean isAccountNonExpired() { return true; }
          @Override public boolean isAccountNonLocked() { return true; }
          @Override public boolean isCredentialsNonExpired() { return true; }
          @Override public boolean isEnabled() { return true; }
      }
 
      @Service
      public class CustomUserDetailsService implements UserDetailsService {
          @Autowired private UserRepository userRepository;
          @Override @Transactional(readOnly = true)
          public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
              return new CustomUserDetail(userRepository.findByUsername(username)
                      .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username)));
          }
      }
      ```

5. **Tạo JWT Authentication Filter**:
    - Tạo filter để xác minh token:
      ```java
      @Component
      public class JwtAuthenticationFilter extends OncePerRequestFilter {
          @Autowired private JwtUtil jwtUtil;
          @Autowired private CustomUserDetailsService userDetailsService;
 
          @Override
          protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                  throws ServletException, IOException {
              String header = request.getHeader("Authorization");
              if (header != null && header.startsWith("Bearer ")) {
                  String token = header.substring(7);
                  String username = jwtUtil.getUsernameFromToken(token);
                  if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                      if (jwtUtil.validateToken(token, (User) userDetails)) {
                          UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                  userDetails, null, userDetails.getAuthorities());
                          auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                          SecurityContextHolder.getContext().setAuthentication(auth);
                      }
                  }
              }
              chain.doFilter(request, response);
          }
      }
      ```

6. **Cấu hình `SecurityConfig`**:
    - Tích hợp JWT filter và tắt session:
      ```java
      @Configuration
      @EnableWebSecurity
      @EnableMethodSecurity
      public class SecurityConfig {
          @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;
 
          @Bean
          public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
              http
                  .csrf().disable()   
                  .authorizeHttpRequests(auth -> auth
                      .requestMatchers("/api/auth/**").permitAll()
                      .requestMatchers("/public/**").permitAll()
                      .anyRequest().authenticated()
                  )
                  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                  .and()
                  .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
              return http.build();
          }
 
         ...
      }
      ```

7. **Tạo Endpoint Authentication**:
    - Thêm endpoint để đăng nhập và tạo JWT:
      ```java
      @RestController
      @RequestMapping("/api/auth")
      public class AuthController {
          @Autowired private AuthenticationManager authenticationManager;
          @Autowired private JwtUtil jwtUtil;
          @Autowired private UserRepository userRepository;
 
          @PostMapping("/login")
          public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
              try {
                  Authentication authentication = authenticationManager.authenticate(
                          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
                  SecurityContextHolder.getContext().setAuthentication(authentication);
                  User user = userRepository.findByUsername(loginRequest.getUsername())
                          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                  String token = jwtUtil.generateToken(user);
                  return ResponseEntity.ok(new JwtResponse(token));
              } catch (Exception e) {
                  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
              }
          }
      }
 
      public class LoginRequest { private String username, password; // Getters, setters }
      public class JwtResponse { private String token; public JwtResponse(String token) { this.token = token; } public String getToken() { return token; } }
      ```

8. **Bảo vệ Endpoint (tùy chọn)**:
    - Sử dụng `@PreAuthorize` để bảo vệ API:
      ```java
      @RestController
      @RequestMapping("/api")
      public class ProtectedController {
          @GetMapping("/hello")
          @PreAuthorize("hasRole('ROLE_ADMIN')")
          public String hello() {
              return "Hello, Admin!";
          }
      }
      ```

---

### Mẹo ghi nhớ
- **JwtUtil**: "Nhà máy" tạo và kiểm tra token.
- **Filter**: "Cổng kiểm tra" token trong mỗi request.
- **Stateless**: Không dùng session, chỉ dựa vào token.

---

### Kết luận
Những bước trên sẽ giúp bạn tích hợp JWT vào Spring Security một cách hiệu quả. Hãy kiểm tra dữ liệu user trong cơ sở dữ liệu và bật log (`logging.level.org.springframework.security=DEBUG`) để debug nếu cần