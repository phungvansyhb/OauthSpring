package example;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Base64;

@SpringBootApplication
public class Application {
     public static void main(String[] args) {
         SpringApplication.run(Application.class, args);

//         byte[] keyBytes512 = Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded();
//         String base64Key512 = Base64.getEncoder().encodeToString(keyBytes512);
//         System.out.println("Generated HS512 key: " + base64Key512);

     }
}
