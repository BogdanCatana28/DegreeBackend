package model.dto.builders;

import model.Medic;
import model.dto.AppointmentMedicDTO;

public class AppointmentMedicDTOBuilder {
    /**
     * Use @NoArgsContructor instead of this
     */
    private AppointmentMedicDTOBuilder() {
    }

    /**
     * Simply name 'toDTO'
     */
    public static AppointmentMedicDTO toAppointmentMedicDTO(Medic medic) {
        return AppointmentMedicDTO.builder()
                .id(medic.getId())
                .firstName(medic.getFirstName())
                .lastName(medic.getLastName())
                .build();
    }
}
