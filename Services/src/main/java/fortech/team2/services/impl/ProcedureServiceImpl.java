package fortech.team2.services.impl;

import fortech.team2.model.Procedure;
import fortech.team2.persistence.ProcedureRepository;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class ProcedureServiceImpl implements ProcedureService {

    /**
     * You could also declare this autowired fields as 'final'
     */
    @Autowired
    private ProcedureRepository procedureRepository;

    @Override
    public Procedure addProcedure(Procedure procedure) {
        return procedureRepository.save(procedure);
    }

    @Override
    public List<Procedure> showProcedures() {
        return procedureRepository.findAll();
    }

    @Override
    public Procedure getProcedureById(Integer id) throws RepositoryException {
        /**
         * You should extract this message as a constant in a separated class named 'Constants' in 'utils' package , as public static final
         */
        Supplier<RepositoryException> e = () -> new RepositoryException("Procedure not found");
        return procedureRepository.findById(id).orElseThrow(e);
    }

    @Override
    public Procedure updateProcedure(Procedure newProcedure, Integer id) throws RepositoryException {
        /**
         * See the example above about String constants
         */
        Supplier<RepositoryException> e = () -> new RepositoryException("Procedure not found");
        return procedureRepository.findById(id)
                .map(procedure -> {
                    /**
                     * Create a builder for the next 5 lines and call it directly
                     */
                    procedure.setName(newProcedure.getName());
                    procedure.setDuration(newProcedure.getDuration());
                    procedure.setPrice(newProcedure.getPrice());
                    procedure.setSpecializations(newProcedure.getSpecializations());
                    procedure.setAnesthesia(newProcedure.getAnesthesia());
                    /**
                     * You should do the save into a try-catch block because of the errors that can appear
                     */
                    return procedureRepository.save(procedure);
                }).orElseThrow(e);
    }

    @Override
    public void delete(Integer id) {
        procedureRepository.deleteById(id);
    }

}
