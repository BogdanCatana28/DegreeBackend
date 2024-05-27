package fortech.team2.model.dto.builders;

import fortech.team2.model.Medic;
import fortech.team2.model.dto.AppointmentMedicDTO;

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
