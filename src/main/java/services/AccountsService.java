package services;

import model.Medic;
import model.User;
import model.dto.request.LoginRequest;
import model.dto.request.SignupMedicRequest;
import model.dto.request.SignupRequest;
import model.dto.request.TokenRefreshRequest;
import model.dto.response.JwtResponse;
import validation.utils.ValidatorException;
import org.springframework.http.ResponseEntity;

public interface AccountsService {
    Medic registerMedic(SignupMedicRequest signUpRequest) throws ValidatorException;

    User registerUser(SignupRequest signUpRequest) throws ValidatorException;

    JwtResponse signIn(LoginRequest loginRequest);

    ResponseEntity<?> refreshToken(TokenRefreshRequest request);

    ResponseEntity<?> signOut();

    String generatePassword();

    String generatePasswordResetToken(String email);

    Boolean resetPasswordWithToken(String token, String newPassword) throws ValidatorException;
}
