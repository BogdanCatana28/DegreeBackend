package fortech.team2.persistence;

import fortech.team2.model.DayOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DayOffRepository extends JpaRepository<DayOff, Integer> {
    Iterable<DayOff> getDaysOffByMedicId(Integer medicId);

    boolean existsByMedicIdAndFreeDay(Integer medicId, LocalDate freeDay);
}
