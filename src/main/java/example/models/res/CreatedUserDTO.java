package example.models.res;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatedUserDTO {
    Integer id;
    String username;
    String password;
    String email;
    LocalDateTime createdAt;
}
