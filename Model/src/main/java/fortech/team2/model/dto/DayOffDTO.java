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
public class DayOffDTO {
    private Integer id;
    private Integer medicId;
    private LocalDate freeDay;
}
