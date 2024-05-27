package fortech.team2.model.dto.request;

import fortech.team2.model.enums.MedicSpecialization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class SignupMedicRequest extends SignupRequest {
    private List<MedicSpecialization> specializations;
    private String education;
    private String experience;
}

