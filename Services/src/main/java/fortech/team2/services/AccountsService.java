package fortech.team2.services;

import fortech.team2.model.Medic;
import fortech.team2.model.User;
import fortech.team2.model.dto.request.LoginRequest;
import fortech.team2.model.dto.request.SignupMedicRequest;
import fortech.team2.model.dto.request.SignupRequest;
import fortech.team2.model.dto.request.TokenRefreshRequest;
import fortech.team2.model.dto.response.JwtResponse;
import fortech.team2.validation.utils.ValidatorException;
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
