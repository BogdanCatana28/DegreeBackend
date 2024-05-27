package fortech.team2.model.dto;

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
public class MedicDTO extends UserDTO {
    private List<MedicSpecialization> specializations;
    private String experience;
    private String education;
}