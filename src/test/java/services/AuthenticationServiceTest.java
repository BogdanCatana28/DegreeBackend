
package services;

import model.User;
import model.dto.request.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import persistence.UserRepository;
import services.impl.AuthenticationService;
import validation.RegisterAccountValidator;
import validation.utils.ValidatorException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RegisterAccountValidator registerAccountValidator;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerUser_Success() throws ValidatorException {
        SignupRequest signupRequest = SignupRequest.builder()
                .firstName("Person")
                .lastName("Test")
                .email("person.test@gmail.com")
                .address("Nowhere")
                .phone("1234567890")
                .isAdmin(false)
                .password("password")
                .build();

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");

        User user = new User();
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail());
        user.setAddress(signupRequest.getAddress());
        user.setPhone(signupRequest.getPhone());
        user.setPassword("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = authenticationService.registerUser(signupRequest);

        assertNotNull(result);
        assertEquals(signupRequest.getFirstName(), result.getFirstName());
        assertEquals(signupRequest.getLastName(), result.getLastName());
        assertEquals(signupRequest.getEmail(), result.getEmail());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository, times(1)).existsByEmail(signupRequest.getEmail());
        verify(passwordEncoder, times(1)).encode(signupRequest.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void registerUser_EmailAlreadyExists() {
        SignupRequest signupRequest = SignupRequest.builder()
                .firstName("Person")
                .lastName("Test")
                .email("person.test@gmail.com")
                .address("Nowhere")
                .phone("1234567890")
                .isAdmin(false)
                .password("password")
                .build();

        when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(true);

        ValidatorException exception = assertThrows(ValidatorException.class, () -> {
            authenticationService.registerUser(signupRequest);
        });

        assertEquals("Account already exists", exception.getMessage());
        verify(userRepository, times(1)).existsByEmail(signupRequest.getEmail());
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    public void registerUser_InvalidData() throws ValidatorException {
        SignupRequest signupRequest = SignupRequest.builder()
                .firstName("Person")
                .lastName("Test")
                .email("person.test@gmail.com")
                .address("Nowhere")
                .phone("1234567890")
                .isAdmin(false)
                .password("password")
                .build();

        doThrow(new ValidatorException("Invalid data")).when(registerAccountValidator).validate(signupRequest);

        ValidatorException exception = assertThrows(ValidatorException.class, () -> {
            authenticationService.registerUser(signupRequest);
        });

        assertEquals("Invalid data", exception.getMessage());
        verify(registerAccountValidator, times(1)).validate(signupRequest);
        verify(userRepository, times(0)).existsByEmail(signupRequest.getEmail());
        verify(userRepository, times(0)).save(any(User.class));
    }
}