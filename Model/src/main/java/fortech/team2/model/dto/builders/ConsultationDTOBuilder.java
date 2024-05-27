package fortech.team2.model.dto.builders;

import fortech.team2.model.Consultation;
import fortech.team2.model.dto.ConsultationDTO;

import java.util.ArrayList;

public class ConsultationDTOBuilder {
    /**
     * Use @NoArgsContructor instead of this
     */
    private ConsultationDTOBuilder() {
    }
    /**
     * Simply name 'toEntity'
     */
    public static Consultation fromConsultationDTO(ConsultationDTO consultationDTO) {
        return Consultation.builder()
                .mainConcern(consultationDTO.getConsultationMainConcern())
                .historyOfConcern(consultationDTO.getConsultationHistoryOfConcern())
                .diagnostic(consultationDTO.getConsultationDiagnostic())
                .treatment(consultationDTO.getConsultationTreatment())
                .extraNotes(consultationDTO.getConsultationExtraNotes())
                .creationDate(consultationDTO.getConsultationCreationDate())
                .build();
    }

    /**
     * Simply name 'toDTO'
     */
    public static ConsultationDTO toConsultationDTO(Consultation consultation) {
        return ConsultationDTO.builder()
                .consultationMainConcern(consultation.getMainConcern())
                .consultationHistoryOfConcern(consultation.getHistoryOfConcern())
                .consultationDiagnostic(consultation.getDiagnostic())
                .consultationTreatment(consultation.getTreatment())
                .consultationExtraNotes(consultation.getExtraNotes())
                .consultationId(consultation.getId()) //adaugat ptr consultDetails
                .consultationCreationDate(consultation.getCreationDate()) //adaugat ptr consultDetails
                .ownerFirstName(consultation.getOwner().getFirstName())
                .ownerLastName(consultation.getOwner().getLastName())
                .ownerEmail(consultation.getOwner().getEmail())
                .ownerPhone(consultation.getOwner().getPhone())
                .ownerAddress(consultation.getOwner().getAddress())
                .patientId(consultation.getPatient().getId())
                .patientName(consultation.getPatient().getName())
                .patientBreed(consultation.getPatient().getBreed())
                .patientColor(consultation.getPatient().getColor())
                .patientType(consultation.getPatient().getType().toString())
                .patientSex(consultation.getPatient().getSex().toString())
                .patientWeight(consultation.getPatient().getWeight())
                .patientBirthDate(consultation.getPatient().getBirthDate())
                .consultationCreationDate(consultation.getCreationDate())
                .build();
    }


    public static Iterable<ConsultationDTO> toConsultationDTOList(Iterable<Consultation> consultations) {
        ArrayList<ConsultationDTO> consultationDTOS = new ArrayList<>();
        consultations.forEach(consultation -> consultationDTOS.add(toConsultationDTO(consultation)));

        return consultationDTOS;
    }
}
