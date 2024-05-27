package fortech.team2.services;

import fortech.team2.model.Procedure;
import fortech.team2.persistence.exceptions.RepositoryException;

import java.util.List;

public interface ProcedureService {

    Procedure addProcedure(Procedure procedure);

    List<Procedure> showProcedures();

    Procedure getProcedureById(Integer id) throws RepositoryException;

    Procedure updateProcedure(Procedure newProcedure, Integer id) throws RepositoryException;

    void delete(Integer id);

}
