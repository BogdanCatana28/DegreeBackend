package fortech.team2.persistence;

import fortech.team2.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    Iterable<Appointment> getAppointmentByMedicIdAndDateReservationBetween(Integer medicId, LocalDateTime startDate, LocalDateTime endDate);


    List<Appointment> findByMedicId(Integer medicId);
}
