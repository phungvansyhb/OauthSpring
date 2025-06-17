
---

## 🔐 **1. Authorization là gì?**

> Authorization là quá trình **phân quyền truy cập** – tức là sau khi người dùng đã đăng nhập (authenticated), họ có **được phép truy cập** vào tài nguyên (URL, method, dữ liệu...) hay không.

---

## ✅ **2. Các cách phân quyền (Authorization) trong Spring Security**

### **2.1. Dựa trên URL – trong `SecurityFilterChain`**

Đây là cách phổ biến nhất:

```java
http
  .authorizeHttpRequests(authz -> authz
      .requestMatchers("/admin/**").hasRole("ADMIN")
      .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
      .anyRequest().authenticated()
  );
```

> 💡 Lưu ý: `hasRole("ADMIN")` tương đương với `hasAuthority("ROLE_ADMIN")` (Spring sẽ tự thêm prefix `ROLE_` khi dùng `hasRole`).

---

### **2.2. Dùng annotation trên controller/method**

#### 👇 Thêm annotation vào method hoặc class:

```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin")
public String adminPage() {
    return "admin";
}
```

#### 🛠 Đừng quên bật hỗ trợ annotation:

```java
@EnableGlobalMethodSecurity(prePostEnabled = true) // Spring Security < 6
```

Với **Spring Security 6**, dùng:

```java
@EnableMethodSecurity(prePostEnabled = true)
```

---

### **2.3. Dựa trên `@Secured`**

```java
@Secured("ROLE_ADMIN")
public String adminPage() {
    return "admin";
}
```

Cũng cần bật:

```java
@EnableMethodSecurity(securedEnabled = true)
```

---

### **2.4. Dựa trên biểu thức SpEL nâng cao (custom logic)**

Ví dụ: chỉ cho user sửa bài viết của chính họ:

```java
@PreAuthorize("#post.owner.username == authentication.name")
public void editPost(Post post) {
    ...
}
```

---

### **2.5. Tạo PermissionEvaluator tùy chỉnh**

Nếu bạn cần kiểm tra phân quyền theo logic phức tạp hơn (ví dụ: user có thể xem resource nếu có subscription active), bạn có thể:

* Cài đặt `PermissionEvaluator`
* Sử dụng `@PreAuthorize("hasPermission('READ')")`

---

## 📌 Ví dụ thực tế

```java
// Chỉ admin mới vào được trang này
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin")
public String admin() {
    return "admin";
}

// Chỉ user mới vào đây
@Secured("ROLE_USER")
@GetMapping("/profile")
public String profile() {
    return "profile";
}

@PreAuthorize("hasRole('ADMIN')") // Chỉ ADMIN mới được gọi phương thức này
public String deleteUserData(String userId) {
    // Logic xóa dữ liệu người dùng
    System.out.println("Deleting data for user: " + userId + " by admin.");
    return "User data deleted successfully.";
}

@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')") // ADMIN hoặc MANAGER đều được
public String viewSensitiveReport() {
    // Logic lấy báo cáo nhạy cảm
    System.out.println("Accessing sensitive report.");
    return "Sensitive Report Data";
}

@PreAuthorize("isAuthenticated()") // Chỉ cần người dùng đã đăng nhập
public String getUserProfile(String username) {
    // Logic lấy profile người dùng
    return "Profile for " + username;
}

// Ví dụ phức tạp hơn với SpEL, kiểm tra quyền dựa trên tham số
@PreAuthorize("hasRole('ADMIN') or authentication.principal.username == #username")
public String updateProfile(String username, String newData) {
    // Chỉ ADMIN hoặc chính người dùng đó mới được cập nhật profile của họ
    System.out.println("Updating profile for " + username);
    return "Profile updated.";
}
```

---

## 💡 Tổng kết

| Cách                                               | Khi nào dùng?                    |
|----------------------------------------------------|----------------------------------|
| `.authorizeHttpRequests()`                         | Phân quyền theo URL              |
| `@PreAuthorize / @Secured`                         | Phân quyền theo logic controller |
| SpEL nâng cao                                      | Phân quyền theo logic dữ liệu    |
| Custom `PermissionEvaluator`                       | Phân quyền phức tạp              |
| hasRole("ADMIN") (tương đương ROLE_ADMIN trong db) | kiểm tra quyền user              |
| hasAuthority("READ")                               | kiểm tra chi tiết permision      |

---

