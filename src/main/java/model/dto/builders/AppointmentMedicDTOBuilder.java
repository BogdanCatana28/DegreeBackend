package model.dto.builders;

import model.Medic;
import model.dto.AppointmentMedicDTO;

public class AppointmentMedicDTOBuilder {

    private AppointmentMedicDTOBuilder() {
    }

    public static AppointmentMedicDTO toAppointmentMedicDTO(Medic medic) {
        return AppointmentMedicDTO.builder()
                .id(medic.getId())
                .firstName(medic.getFirstName())
                .lastName(medic.getLastName())
                .build();
    }
}
