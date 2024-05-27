package fortech.team2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftDTO {
    private Integer id;
    private Integer medicId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
