package services;
import model.Medic;
import model.Procedure;
import model.enums.MedicSpecialization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import persistence.MedicRepository;
import persistence.exceptions.RepositoryException;
import services.ProcedureService;
import services.impl.MedicServiceImpl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MedicServiceTest {

    @Mock
    private MedicRepository medicRepository;

    @Mock
    private ProcedureService procedureService;

    @InjectMocks
    private MedicServiceImpl medicService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getMedicsForProcedure_Success() throws RepositoryException {
        Integer procedureId = 1;
        Procedure procedure = new Procedure(procedureId, "Procedure1", 60, 100, List.of(MedicSpecialization.DENTISTRY), true);
        Medic medic = new Medic();
        medic.setSpecializations(List.of(MedicSpecialization.DENTISTRY));

        when(procedureService.getProcedureById(procedureId)).thenReturn(procedure);
        when(medicRepository.findAllBySpecializationsContains(MedicSpecialization.DENTISTRY)).thenReturn(Collections.singletonList(medic));

        Iterable<Medic> medics = medicService.getMedicsForProcedure(procedureId);

        assertNotNull(medics);
        assertEquals(1, ((Set<Medic>) medics).size());

        verify(procedureService, times(1)).getProcedureById(procedureId);
        verify(medicRepository, times(1)).findAllBySpecializationsContains(MedicSpecialization.DENTISTRY);
    }

    @Test
    public void getMedicsForProcedure_ProcedureNotFound() throws RepositoryException {
        Integer procedureId = 1;

        when(procedureService.getProcedureById(procedureId)).thenThrow(new RepositoryException("Procedure not found"));

        RepositoryException exception = assertThrows(RepositoryException.class, () -> {
            medicService.getMedicsForProcedure(procedureId);
        });

        assertEquals("Procedure not found", exception.getMessage());

        verify(procedureService, times(1)).getProcedureById(procedureId);
        verify(medicRepository, times(0)).findAllBySpecializationsContains(any(MedicSpecialization.class));
    }
}
