package persistence;


import model.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {

    List<Consultation> findAllByPatientId(Integer patientId);

    List<Consultation> findAllByOwnerId(Integer ownerId);
}

