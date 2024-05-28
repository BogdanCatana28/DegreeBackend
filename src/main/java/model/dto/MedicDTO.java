package model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import model.enums.MedicSpecialization;

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