package bekhruz.com.cinemora.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username kiritilishi shart")
    @Size(min = 3, max = 50, message = "Username 3-50 belgidan iborat bo'lishi kerak")
    private String username;

    @NotBlank(message = "Email kiritilishi shart")
    @Email(message = "Email formati noto'g'ri")
    private String email;

    @NotBlank(message = "Parol kiritilishi shart")
    @Size(min = 6, message = "Parol kamida 6 ta belgi bo'lishi kerak")
    private String password;

    private String fullName;
}
