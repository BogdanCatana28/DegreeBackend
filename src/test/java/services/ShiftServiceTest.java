package services;

import model.Shift;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import persistence.ShiftRepository;
import services.impl.ShiftServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShiftServiceTest {

    @Mock
    private ShiftRepository shiftRepository;

    @InjectMocks
    private ShiftServiceImpl shiftService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getWeeklyShifts_Success() {
        LocalDate date = LocalDate.of(2023, 7, 4);
        Shift shift1 = new Shift(null, LocalDateTime.of(2023, 7, 3, 9, 0), LocalDateTime.of(2023, 7, 3, 17, 0));
        Shift shift2 = new Shift(null, LocalDateTime.of(2023, 7, 5, 9, 0), LocalDateTime.of(2023, 7, 5, 17, 0));

        when(shiftRepository.findAll()).thenReturn(Arrays.asList(shift1, shift2));

        Iterable<Shift> shifts = shiftService.getWeeklyShifts(date);

        assertNotNull(shifts);
        assertEquals(2, ((Collection<?>) shifts).size());

        verify(shiftRepository, times(1)).findAll();
    }

    @Test
    public void getWeeklyShifts_Empty() {
        LocalDate date = LocalDate.of(2023, 7, 4);

        when(shiftRepository.findAll()).thenReturn(Collections.emptyList());

        Iterable<Shift> shifts = shiftService.getWeeklyShifts(date);

        assertNotNull(shifts);
        assertEquals(0, ((Collection<?>) shifts).size());

        verify(shiftRepository, times(1)).findAll();
    }
}

