package fortech.team2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private Integer id;
}
