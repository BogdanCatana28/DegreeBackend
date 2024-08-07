package model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferenceDTO {
    private Integer id;

    private Integer customerId;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;

    private String name;
    private String sex;
    private String type;
    private String breed;
    private String colour;
    private Integer age;
}
