package fortech.team2.services;


import fortech.team2.model.Medic;
import fortech.team2.model.enums.MedicSpecialization;
import fortech.team2.persistence.exceptions.RepositoryException;

public interface MedicService {

    Medic getMedicById(Integer id) throws RepositoryException;

    Iterable<Medic> getMedicsBySpecialization(MedicSpecialization specialization);

    Iterable<Medic> getAllMedics();

    Iterable<Medic> getMedicsForProcedure(Integer procedureId) throws RepositoryException;

}
