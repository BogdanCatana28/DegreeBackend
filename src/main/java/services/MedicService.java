package services;


import model.Medic;
import model.enums.MedicSpecialization;
import persistence.exceptions.RepositoryException;

public interface MedicService {

    Medic getMedicById(Integer id) throws RepositoryException;

    Iterable<Medic> getMedicsBySpecialization(MedicSpecialization specialization);

    Iterable<Medic> getAllMedics();

    Iterable<Medic> getMedicsForProcedure(Integer procedureId) throws RepositoryException;
}
