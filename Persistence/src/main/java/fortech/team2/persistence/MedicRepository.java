package fortech.team2.persistence;

import fortech.team2.model.Medic;
import fortech.team2.model.enums.MedicSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicRepository extends JpaRepository<Medic, Integer> {
    Optional<Medic> findByEmail(String email);

    Boolean existsByEmail(String email);

    Iterable<Medic> findAllBySpecializationsContains(MedicSpecialization specialization);
}
