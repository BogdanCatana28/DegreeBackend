//import VeterinaryClinicBE.VeterinaryClinicBeApplication;
//import fortech.team2.model.Patient;
//import fortech.team2.model.User;
//import fortech.team2.model.dto.ConsultationDTO;
//import fortech.team2.model.enums.PatientSex;
//import fortech.team2.model.enums.UserRole;
//import fortech.team2.persistence.UserRepository;
//import fortech.team2.services.PatientService;
//import fortech.team2.services.impl.ConsultationServiceImpl;
//import fortech.team2.validation.utils.ValidatorException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest(classes = VeterinaryClinicBeApplication.class)
//public class ConsultationServiceImplTest {
//
//    @Mock
//    private PatientService patientService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private ConsultationServiceImpl consultationService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testAddConsultationWithInvalidOwner() {
//
//        Patient mockPatient = new Patient();
//        when(patientService.findPatientByDetails(
//                Mockito.eq("Marius"),
//                Mockito.eq(PatientSex.MALE),
//                Mockito.eq("Dog"),
//                Mockito.eq("Cane Corso"),
//                Mockito.eq("Black"),
//                Mockito.any(LocalDate.class),
//                Mockito.eq(15.2f)
//        )).thenReturn(mockPatient);
//
//        when(userRepository.findByEmail(Mockito.eq("catana_bogdan28@yahoo.com"))).thenReturn(null); // mock owner then set it on null
//
//        ConsultationDTO consultationDTO = new ConsultationDTO();
//        consultationDTO.setMainConcern("Fever");
//        consultationDTO.setHistoryOfConcern("Vomiting and loss of appetite");
//        consultationDTO.setDiagnostic("Possible infection");
//        consultationDTO.setTreatment("Prescribed antibiotics");
//        consultationDTO.setExtraNotes("Monitor for improvement");
//
//        consultationDTO.setName("Marius");
//        consultationDTO.setGender(PatientSex.MALE);
//        consultationDTO.setType("Dog");
//        consultationDTO.setBreed("Cane Corso");
//        consultationDTO.setColor("Black");
//        consultationDTO.setBirthDate(LocalDate.of(2019, 1, 1));
//        consultationDTO.setWeight(15.2f);
//
//        consultationDTO.setEmail("catana_bogdan28@yahoo.com");
//
//        ValidatorException exception = assertThrows(ValidatorException.class, () -> {
//            consultationService.addConsultation(consultationDTO);
//        });
//
//        assertEquals("Owner not found with the provided email!", exception.getMessage());
//
//        verify(patientService).findPatientByDetails(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
//        verify(userRepository).findByEmail(Mockito.any());
//    }
//
//    @Test
//    public void testAddConsultationWithInvalidPatient() {
//
//        when(patientService.findPatientByDetails(
//                Mockito.eq("Fluffy"),
//                Mockito.eq(PatientSex.MALE),
//                Mockito.eq("Dog"),
//                Mockito.eq("Cane Corso"),
//                Mockito.eq("Black"),
//                Mockito.eq(LocalDate.of(2019, 1, 1)),
//                Mockito.eq(15.2f)
//        )).thenReturn(null); // mock patient then set it on null
//
//        User mockUser = new User();
//        mockUser.setId(1);
//        mockUser.setFirstName("John");
//        mockUser.setLastName("Doe");
//        mockUser.setEmail("jhonnytest@gmail.com");
//        mockUser.setPassword("anvelopa09");
//        mockUser.setPhone("0742658992");
//        mockUser.setAddress("Malibu");
//        mockUser.setRole(UserRole.ROLE_CUSTOMER);
//
//        when(userRepository.findByEmail(Mockito.eq("jhonnytest@gmail.com"))).thenReturn(mockUser);
//
//        ConsultationDTO consultationDTO = new ConsultationDTO();
//        consultationDTO.setMainConcern("Fever");
//        consultationDTO.setHistoryOfConcern("Vomiting and loss of appetite");
//        consultationDTO.setDiagnostic("Possible infection");
//        consultationDTO.setTreatment("Prescribed antibiotics");
//        consultationDTO.setExtraNotes("Monitor for improvement");
//
//        consultationDTO.setName("Fluffy");
//        consultationDTO.setGender(PatientSex.MALE);
//        consultationDTO.setType("Dog");
//        consultationDTO.setBreed("Cane Corso");
//        consultationDTO.setColor("Black");
//        consultationDTO.setBirthDate(LocalDate.of(2019, 1, 1));
//        consultationDTO.setWeight(15.2f);
//
//        consultationDTO.setEmail("jhonnytest@gmail.com");
//
//        ValidatorException exception = assertThrows(ValidatorException.class, () -> {
//            consultationService.addConsultation(consultationDTO);
//        });
//
//        assertEquals("Patient not found with the provided details!", exception.getMessage());
//
//        verify(patientService).findPatientByDetails(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
//    }
//}
//
//
