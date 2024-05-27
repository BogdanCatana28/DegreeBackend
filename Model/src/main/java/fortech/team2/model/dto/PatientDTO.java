package fortech.team2.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientDTO {
    private Integer id;
    private String patientName;
    private String patientSex;
    private String patientType;
    private String patientBreed;
    private String patientColour;
    private Integer patientAgeYears;
    private Integer patientAgeMonths;
    private LocalDate patientBirthdate;
    private Float patientWeight;
    private String patientMedicalHistoryBeforeClinic;
    private UserDTO owner;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    /** You could also use @NotNUll constraint from javax.validation.constraints **/
    private Boolean userAlreadyRegistered;
}
