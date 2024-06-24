package services.impl;

import model.Medic;
import model.Procedure;
import model.enums.MedicSpecialization;
import persistence.MedicRepository;
import persistence.exceptions.RepositoryException;
import services.MedicService;
import services.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class MedicServiceImpl implements MedicService {

    @Autowired
    private MedicRepository medicRepository;
    @Autowired
    private ProcedureService procedureService;


    public Medic getMedicById(Integer id) throws RepositoryException {

        Supplier<RepositoryException> e = () -> new RepositoryException("Medic with id " + id + " does not exist");
        return medicRepository.findById(id).orElseThrow(e);
    }

    @Override
    public Iterable<Medic> getMedicsBySpecialization(MedicSpecialization specialization) {
        return medicRepository.findAllBySpecializationsContains(specialization);
    }

    @Override
    public Iterable<Medic> getAllMedics() {
        return medicRepository.findAll();
    }

    @Override
    public Iterable<Medic> getMedicsForProcedure(Integer procedureId) throws RepositoryException {
        Procedure procedure = procedureService.getProcedureById(procedureId);

        Set<Medic> medics = new HashSet<>();

        procedure.getSpecializations().forEach(specialization -> getMedicsBySpecialization(specialization).forEach(medics::add));

        return medics;
    }
}
