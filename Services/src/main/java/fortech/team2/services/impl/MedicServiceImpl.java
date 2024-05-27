package fortech.team2.services.impl;

import fortech.team2.model.Medic;
import fortech.team2.model.Procedure;
import fortech.team2.model.enums.MedicSpecialization;
import fortech.team2.persistence.MedicRepository;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.MedicService;
import fortech.team2.services.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class MedicServiceImpl implements MedicService {
    /**
     * You could also declare these autowired fields as 'final'
     */
    @Autowired
    private MedicRepository medicRepository;
    @Autowired
    private ProcedureService procedureService;


    public Medic getMedicById(Integer id) throws RepositoryException {
        /**
         * You should extract this message as a constant in a separated class named 'Constants' in 'utils' package , as public static final
         */
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
