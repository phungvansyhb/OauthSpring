
---

## 🧭 Tổng quan luồng xử lý Authentication trong Spring Security

Khi một **HTTP request** đến ứng dụng, luồng xử lý bảo mật hoạt động theo thứ tự sau:

```
Client → Filter Chain → AuthenticationManager → AuthenticationProvider → UserDetailsService → UserDetails
```

---

## 🧱 Chi tiết từng bước trong luồng

### 1. **Request đến ứng dụng**

* Ví dụ: người dùng gửi request `POST /login` với username & password.

---

### 2. **Filter Chain tiếp nhận request**

Spring Security có một chuỗi filter được kích hoạt. Quan trọng nhất:

* `UsernamePasswordAuthenticationFilter` sẽ xử lý các request `/login`.

---

### 3. **Tạo `UsernamePasswordAuthenticationToken`**

Filter tạo object:

```java
new UsernamePasswordAuthenticationToken(username, password)
```

Sau đó gọi `AuthenticationManager.authenticate(token)`.

---

### 4. **`AuthenticationManager` xử lý**

* Đối tượng `AuthenticationManager` sẽ chuyển token tới danh sách các `AuthenticationProvider`.

---

### 5. **`AuthenticationProvider` được gọi**

* Mặc định là `DaoAuthenticationProvider`.
* Nó gọi đến `UserDetailsService.loadUserByUsername(username)`.

---

### 6. **`UserDetailsService` trả về `UserDetails`**

* Nếu username hợp lệ, trả về object `CustomUserDetails`.
* Nếu không, ném ra `UsernameNotFoundException`.

---

### 7. **PasswordEncoder kiểm tra password**

* So sánh password nhập vào (raw) với password đã mã hóa (encoded) lưu trong DB.
* Thông qua: `passwordEncoder.matches(raw, encoded)`.

---

### 8. **Xác thực thành công**

* Spring Security tạo một `Authentication` object (đã authenticated).
* Gán nó vào `SecurityContextHolder`.

---

### 9. **Chuyển tiếp request đến Controller**

* Sau khi xác thực thành công, request được chuyển tiếp đến controller của bạn.
* Từ đây, bạn có thể truy cập user đã login thông qua:

```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
```

---

## 🖼️ Luồng minh họa

```
                +-------------------------+
                |   HTTP Request (/login)|
                +-------------------------+
                            |
                            v
       +--------------------+-------------------+
       | UsernamePasswordAuthenticationFilter   |
       +--------------------+-------------------+
                            |
                            v
              +-------------+-------------+
              | AuthenticationManager     |
              +-------------+-------------+
                            |
                            v
              +-------------+-------------+
              | DaoAuthenticationProvider |
              +-------------+-------------+
                            |
                            v
              +---------------------------+
              | UserDetailsService        |
              +---------------------------+
                            |
                            v
              +---------------------------+
              | PasswordEncoder.matches() |
              +---------------------------+
                            |
                    (authenticated)
                            |
                            v
           +----------------------------------------+
           | SecurityContextHolder.setAuthentication |
           +----------------------------------------+
                            |
                            v
                 → Controller xử lý logic

```

---

## ✅ Tổng kết

| Bước | Thành phần            | Vai trò                  |
| ---- | --------------------- | ------------------------ |
| 1    | Filter                | Chặn request và xác thực |
| 2    | Token                 | Chứa thông tin đăng nhập |
| 3    | AuthManager           | Điều phối việc xác thực  |
| 4    | Provider              | Xác thực cụ thể          |
| 5    | UserDetailsService    | Lấy dữ liệu người dùng   |
| 6    | PasswordEncoder       | So sánh mật khẩu         |
| 7    | SecurityContextHolder | Lưu trạng thái đăng nhập |


---

## ✅ Khi nào **KHÔNG cần** viết controller cho `/login`

Khi bạn cấu hình như sau:

```java
http
  .authorizeHttpRequests(auth -> auth
      .anyRequest().authenticated()
  )
  .formLogin(); // hoặc formLogin(withDefaults())
```

Spring Security sẽ:

* Tự động tạo **form login HTML** ở `/login`
* Xử lý POST `/login` để xác thực
* Chuyển hướng đến trang mặc định (`/` hoặc nơi bạn chỉ định sau khi login)

---

## ✅ Khi nào **CẦN** viết controller cho `/login`

Bạn cần viết controller nếu bạn muốn:

| Trường hợp            | Mô tả                                                                             |
| --------------------- | --------------------------------------------------------------------------------- |
| Giao diện tùy chỉnh   | Bạn muốn dùng form login của riêng mình (`login.html`, JSP, Thymeleaf, React,...) |
| API login (REST)      | Login thông qua JSON (chứ không phải form), ví dụ dùng trong SPA/mobile           |
| Giao diện đa ngôn ngữ | Form login có nhiều phiên bản theo locale                                         |

---

## 🧪 Ví dụ tùy chỉnh login view với Thymeleaf

```java
http
  .formLogin(form -> form
      .loginPage("/login") // bạn tự tạo controller & view này
      .permitAll()
  );
```

```java
@GetMapping("/login")
public String loginForm() {
    return "login"; // login.html hoặc login.jsp
}
```

---

## 🚀 Nếu dùng REST API?

* Spring Security không hỗ trợ sẵn JSON login
* Bạn phải viết controller như:

```java
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    // dùng AuthenticationManager để authenticate
}
```

---

## 📝 Tổng kết

| Trường hợp sử dụng   | Cần controller `/login`?       |
| -------------------- | ------------------------------ |
| Mặc định (formLogin) | ❌ Không                        |
| Custom login page    | ✅ Có                           |
| REST API login       | ✅ Có                           |
| OAuth2 login         | ❌ Không (Spring xử lý tự động) |

---
