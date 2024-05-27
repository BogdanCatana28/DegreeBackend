package fortech.team2.validation;

import fortech.team2.model.dto.ConsultationDTO;
import fortech.team2.validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsultationValidator {

    @Autowired
    private RegisterAccountValidator registerAccountValidator;
    @Autowired
    private PatientValidator patientValidator;

    public void validate(ConsultationDTO consultationDTO) throws ValidatorException {
        validateNotNull(consultationDTO, "ConsultationDTO cannot be null");

        validateNotNull(consultationDTO.getConsultationMainConcern(), "Main concern cannot be null or empty");
        validateNotNull(consultationDTO.getConsultationHistoryOfConcern(), "History of concern cannot be null or empty");
        validateNotNull(consultationDTO.getConsultationDiagnostic(), "Diagnostic cannot be null or empty");
        validateNotNull(consultationDTO.getConsultationTreatment(), "Treatment cannot be null or empty");

        System.out.println(consultationDTO.getOwnerPhone());
        registerAccountValidator.validatePhoneNumber(consultationDTO.getOwnerPhone());
        validateNotNull(consultationDTO.getOwnerAddress(), "Address cannot be null or empty");

        patientValidator.validateWeight(consultationDTO.getPatientWeight());
        patientValidator.validateColor(consultationDTO.getPatientColor());

    }

    private void validateNotNull(Object value, String errorMessage) throws ValidatorException {
        if (value == null) {
            throw new ValidatorException(errorMessage);
        }
    }
}

