
---

## 🔐 1. **Kiến thức nền tảng bắt buộc**

### ✅ Cần hiểu rõ:

| Chủ đề                               | Mô tả                                 |
| ------------------------------------ | ------------------------------------- |
| **Authentication vs Authorization**  | Xác thực người dùng vs Phân quyền     |
| **Security Filter Chain**            | Spring Security dựa trên chuỗi filter |
| **UserDetails & UserDetailsService** | Cách định nghĩa user trong Spring     |
| **PasswordEncoder**                  | Hash password (BCrypt, Argon2...)     |

---

## ⚙️ 2. **Cấu hình Spring Security (Java Config)**

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .formLogin(withDefaults());
    return http.build();
}
```

### 🎯 Cần nắm:

* `HttpSecurity`
* `SecurityFilterChain` thay cho `WebSecurityConfigurerAdapter` (Spring Security 6+)
* `@EnableMethodSecurity` (trước là `@EnableGlobalMethodSecurity`)
* Session management
* Cấu hình Login/Logout

---

## 🔒 3. **Tùy chỉnh Authentication**

| Thành phần               | Ý nghĩa             |
| ------------------------ | ------------------- |
| `UserDetailsService`     | Lấy user từ DB      |
| `AuthenticationProvider` | Xác thực tuỳ chỉnh  |
| `AuthenticationManager`  | Quản lý xác thực    |
| `PasswordEncoder`        | Hash/check password |

---

## 🧾 4. **Phân quyền ([Authorization](authorize.md))**

* Dựa trên URL (`hasRole`, `hasAuthority`)
* Dựa trên phương thức (`@PreAuthorize`, `@PostAuthorize`)
* Dựa trên dữ liệu (fine-grained security)

---

## 🪪 5. **Tích hợp OAuth2 / OpenID Connect**

| Bạn cần hiểu                                                   | Chi tiết                           |
| -------------------------------------------------------------- | ---------------------------------- |
| `spring-boot-starter-oauth2-client`                            | Login với Google, Facebook, GitHub |
| `@AuthenticationPrincipal OAuth2User`                          | Lấy user từ token                  |
| Session hoặc JWT lưu trữ thông tin người dùng                  |                                    |
| Cấu hình trong `application.yml` hoặc `application.properties` |                                    |

---

## 📦 6. **JWT (JSON Web Token)**

| Mục tiêu                                          | Ghi chú             |
| ------------------------------------------------- | ------------------- |
| Stateless Authentication                          | Không dùng session  |
| Tùy chỉnh `OncePerRequestFilter`                  | Đọc token từ header |
| Kết hợp với `UsernamePasswordAuthenticationToken` |                     |

---

## 🛡️ 7. **Các vấn đề bảo mật phổ biến**

| Chủ đề           | Lý do học                             |
| ---------------- | ------------------------------------- |
| CSRF             | Ngăn tấn công qua cookie              |
| CORS             | Cho phép frontend gọi API backend     |
| Session Fixation | Phòng tấn công chiếm session          |
| Brute-force      | Cấu hình throttling hoặc lock account |
| HTTPS-only       | Đảm bảo truyền thông an toàn          |

---

## 🧰 8. **Công cụ hỗ trợ và debug**

| Công cụ                      | Mục đích                    |
| ---------------------------- | --------------------------- |
| Spring Security Debug Mode   | `httpSecurity.debug(true)`  |
| Actuator + `/beans` + `/env` | Kiểm tra cấu hình runtime   |
| Postman / curl               | Gửi request test phân quyền |

---

## 🗂️ 9. **Cấu trúc thư mục mẫu**

```
- config/
  - SecurityConfig.java
- controller/
- service/
- repository/
- security/
  - JwtAuthenticationFilter.java
  - CustomUserDetailsService.java
```

---

## 📚 10. **Nguồn học chi tiết**

| Tên                                                                                 | Ghi chú                       |
| ----------------------------------------------------------------------------------- | ----------------------------- |
| [Spring Security Docs](https://docs.spring.io/spring-security/reference/index.html) | Chính thức, đầy đủ            |
| Baeldung Spring Security                                                            | Hướng dẫn thực hành từng phần |
| Spring Security in Action (Manning)                                                 | Sách rất hay                  |
| Udemy: Spring Security – Zero to Hero                                               | Khóa học chuyên sâu           |

---

