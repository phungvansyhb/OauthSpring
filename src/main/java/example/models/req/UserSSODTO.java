package example.models.req;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSSODTO {
    private String username;
    private String email;
    private String provider;
    private String providerId;

}
