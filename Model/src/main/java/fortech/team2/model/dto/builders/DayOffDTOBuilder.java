package fortech.team2.model.dto.builders;

import fortech.team2.model.DayOff;
import fortech.team2.model.dto.DayOffDTO;

import java.util.ArrayList;

public class DayOffDTOBuilder {
    /**
     * Use @NoArgsContructor instead of this
     */
    private DayOffDTOBuilder() {
    }

    /**
     * Simply name 'toDTO'
     */
    public static DayOffDTO toDayOffDTO(DayOff dayOff) {
        return DayOffDTO.builder()
                .id(dayOff.getId())
                .medicId(dayOff.getMedic().getId())
                .freeDay(dayOff.getFreeDay())
                .build();
    }

    public static Iterable<DayOffDTO> toDayOffDTOList(Iterable<DayOff> daysOff) {
        ArrayList<DayOffDTO> daysOffDTO = new ArrayList<>();
        daysOff.forEach(dayOff -> daysOffDTO.add(toDayOffDTO(dayOff)));
        return daysOffDTO;
    }
}
