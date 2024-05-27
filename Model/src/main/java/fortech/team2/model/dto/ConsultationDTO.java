package fortech.team2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationDTO {

    // consult
    private int consultationId; //adaugat ptr consultDetails
    private LocalDate consultationCreationDate; //adaugat ptr consultDetails
    private String consultationMainConcern;
    private String consultationHistoryOfConcern;
    private String consultationDiagnostic;
    private String consultationTreatment;
    private String consultationExtraNotes;

    // owner
    private String ownerEmail;
    private String ownerFirstName;
    private String ownerLastName;
    private String ownerPhone;
    private String ownerAddress;

    //patient
    private int patientId;
    private String patientName;
    private String patientSex;
    private String patientType;
    private String patientBreed;
    private String patientColor;
    private LocalDate patientBirthDate;
    private Float patientWeight;
}


