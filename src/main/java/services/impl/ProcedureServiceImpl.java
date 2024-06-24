package services.impl;

import model.Procedure;
import persistence.ProcedureRepository;
import persistence.exceptions.RepositoryException;
import services.ProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class ProcedureServiceImpl implements ProcedureService {

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

        Supplier<RepositoryException> e = () -> new RepositoryException("Procedure not found");
        return procedureRepository.findById(id).orElseThrow(e);
    }

    @Override
    public Procedure updateProcedure(Procedure newProcedure, Integer id) throws RepositoryException {

        Supplier<RepositoryException> e = () -> new RepositoryException("Procedure not found");
        return procedureRepository.findById(id)
                .map(procedure -> {

                    procedure.setName(newProcedure.getName());
                    procedure.setDuration(newProcedure.getDuration());
                    procedure.setPrice(newProcedure.getPrice());
                    procedure.setSpecializations(newProcedure.getSpecializations());
                    procedure.setAnesthesia(newProcedure.getAnesthesia());

                    return procedureRepository.save(procedure);
                }).orElseThrow(e);
    }

    @Override
    public void delete(Integer id) {
        procedureRepository.deleteById(id);
    }

}
