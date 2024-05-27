package fortech.team2.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SignupRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private Boolean isAdmin;
    private String password;
}