package fortech.team2.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientExistsResponse {
    private Integer patientId;
    private Boolean exists;
}
