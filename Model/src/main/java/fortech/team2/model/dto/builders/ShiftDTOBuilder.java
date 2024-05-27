package fortech.team2.model.dto.builders;

import fortech.team2.model.Shift;
import fortech.team2.model.dto.ShiftDTO;

import java.util.ArrayList;

public class ShiftDTOBuilder {
    private ShiftDTOBuilder() {
    }

    /**
     * Simply name 'toDTO'
     */
    public static ShiftDTO toShiftDTO(Shift shift) {
        return ShiftDTO.builder()
                .id(shift.getId())
                .medicId(shift.getMedic().getId())
                .startTime(shift.getStartTime())
                .endTime(shift.getEndTime())
                .build();
    }

    /**
     * Simply name 'toEntity'
     */
    public static Shift fromShiftDTO(ShiftDTO shiftDTO) {
        return Shift.builder()
                .startTime(shiftDTO.getStartTime())
                .endTime(shiftDTO.getEndTime())
                .build();
    }

    public static Iterable<ShiftDTO> toShiftDTOList(Iterable<Shift> shifts) {
        ArrayList<ShiftDTO> shiftDTOS = new ArrayList<>();
        shifts.forEach(shift -> shiftDTOS.add(toShiftDTO(shift)));
        return shiftDTOS;
    }
}
