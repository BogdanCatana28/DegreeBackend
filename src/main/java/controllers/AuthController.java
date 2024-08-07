package controllers;

import model.Medic;
import model.User;
import model.dto.MedicDTO;
import model.dto.UserDTO;
import model.dto.builders.MedicDTOBuilder;
import model.dto.builders.UserDTOBuilder;
import model.dto.request.LoginRequest;
import model.dto.request.SignupMedicRequest;
import model.dto.request.SignupRequest;
import model.dto.request.TokenRefreshRequest;
import model.dto.response.JwtResponse;
import services.AccountsService;
import validation.utils.ValidatorException;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AccountsService accountsService;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            return ResponseEntity.ok().body(accountsService.signIn(loginRequest));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bad credentials", e);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        return accountsService.refreshToken(request);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserDTO> registerUser(@RequestBody SignupRequest signUpRequest) {
        try {
            User user = accountsService.registerUser(signUpRequest);
            return ResponseEntity.ok(UserDTOBuilder.toUserDTO(user));
        } catch (ValidatorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }
    }

    @PostMapping("/sign-up/medic")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MedicDTO> registerMedic(@RequestBody SignupMedicRequest medicSignupRequest) {
        try {
            Medic medic = accountsService.registerMedic(medicSignupRequest);
            return ResponseEntity.ok(MedicDTOBuilder.toMedicDTO(medic));
        } catch (ValidatorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
        }

    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> requestBody) {

        String token = requestBody.get("token");
        String password = requestBody.get("password");

        boolean isPasswordReset = false;
        try {
            isPasswordReset = accountsService.resetPasswordWithToken(token, password);
        } catch (ValidatorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        if (isPasswordReset) {

            return ResponseEntity.ok("Password reset successful.");
        } else {

            return ResponseEntity.badRequest().body("Password reset failed.");
        }
    }

    @PermitAll
    @PostMapping("/generate-password-reset-token")
    public ResponseEntity<?> generatePasswordResetToken(@RequestBody Map<String, String> requestBody) {

        String email = requestBody.get("email");
        String token = accountsService.generatePasswordResetToken(email);

        return ResponseEntity.ok("Password reset token generated successfully.");
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> logoutUser() {
        return accountsService.signOut();
    }
}