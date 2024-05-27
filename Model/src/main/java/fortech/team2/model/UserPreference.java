package fortech.team2.model;

import fortech.team2.model.enums.PatientSex;
import fortech.team2.model.enums.PatientType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
/** I think 'user_preferences' it's a better naming for the table,
more info at the link: https://dev.to/ovid/database-naming-standards-2061
 */
@Table(name = "userPreferences")
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preference_id")
    private Integer id;

    @ManyToOne
    /** You should also add @JoinColumn annotation which references the mapped column.
    * @JoinColumn(name = "user_id")
    */
    private User customer;

    // OWNER
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    // PATIENT
    @Column(name = "name")
    private String name;

    @Column(name = "sex")
    @Enumerated(EnumType.STRING)
    private PatientSex sex;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PatientType type;

    @Column(name = "breed")
    private String breed;

    @Column(name = "colour")
    private String colour;

    @Column(name = "age")
    private Integer age;
}
