package fortech.team2.persistence;

import fortech.team2.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    Iterable<Shift> getShiftsByEndTimeIsAfterAndStartTimeIsBefore(LocalDateTime startTime, LocalDateTime endTime);

    Iterable<Shift> getShiftsByMedicIdAndEndTimeIsAfterAndStartTimeIsBefore(Integer medicId, LocalDateTime startTime, LocalDateTime endTime);

}
