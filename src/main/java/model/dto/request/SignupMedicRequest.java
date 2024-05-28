package model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;
import model.enums.MedicSpecialization;

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

