package persistence;

import model.Medic;
import model.enums.MedicSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicRepository extends JpaRepository<Medic, Integer> {
    Optional<Medic> findByEmail(String email);

    Boolean existsByEmail(String email);

    Iterable<Medic> findAllBySpecializationsContains(MedicSpecialization specialization);
}
