package services.impl;

import model.Medic;
import model.RefreshToken;
import model.User;
import model.dto.builders.AuthenticationDTOBuilder;
import model.dto.request.LoginRequest;
import model.dto.request.SignupMedicRequest;
import model.dto.request.SignupRequest;
import model.dto.request.TokenRefreshRequest;
import model.dto.response.JwtResponse;
import model.dto.response.TokenRefreshResponse;
import persistence.RefreshTokenRepository;
import persistence.UserRepository;
import services.AccountsService;
import services.EmailService;
import services.jwt.JwtUtils;
import validation.RegisterAccountValidator;
import validation.utils.TokenRefreshException;
import validation.utils.ValidatorException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class AuthenticationService implements AccountsService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RegisterAccountValidator registerAccountValidator;
    @Autowired
    private EmailService emailService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public Medic registerMedic(SignupMedicRequest medicSignupRequest) throws ValidatorException {
        registerAccountValidator.validate(medicSignupRequest);

        if (Boolean.TRUE.equals(userRepository.existsByEmail(medicSignupRequest.getEmail()))) {
            throw new ValidatorException("Account already exists");
        }

        Medic medic = AuthenticationDTOBuilder.fromSignupRequest(medicSignupRequest);
        String password = medicSignupRequest.getPassword();

        medic.setPassword(encoder.encode(password));
        medic.setDaysOff(25);
        userRepository.save(medic);
        medic.setPassword(password);
        emailService.sendPassword(medic);
        return medic;
    }

    @Override
    public User registerUser(SignupRequest signUpRequest) throws ValidatorException {
        registerAccountValidator.validate(signUpRequest);
        if (Boolean.TRUE.equals(userRepository.existsByEmail(signUpRequest.getEmail()))) {

            throw new ValidatorException("Account already exists");
        }

        User user = AuthenticationDTOBuilder.fromSignupRequest(signUpRequest);
        user.setPassword(encoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);

        return userRepository.save(user);
    }

    @Override
    public JwtResponse signIn(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getEmail(), roles);
    }

    @Override
    public ResponseEntity<?> refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getEmail());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in the database!"));
    }

    @Override
    public ResponseEntity<?> signOut() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = userDetails.getId();

        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(userId);

    }

    public String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    @Override
    public String generatePasswordResetToken(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {

            throw new RuntimeException("User not found");
        }

        if (user.getId() != null ){
            refreshTokenService.deleteByUserId(user.getId());
        }
        String token = jwtUtils.generateRandomToken();

        String resetLink = "http://localhost:3000/reset-password/" + token;

        emailService.sendPasswordResetLink(user.getEmail(), resetLink);

        Instant expiryDate = Instant.now().plus(Duration.ofHours(1));

        RefreshToken resetToken = new RefreshToken();
        resetToken.setUser(user);
        resetToken.setToken(token);
        resetToken.setExpiryDate(expiryDate);

        refreshTokenRepository.save(resetToken);

        refreshTokenRepository.save(resetToken);

        return token;
    }

    @Override
    public Boolean resetPasswordWithToken(String token, String newPassword) throws ValidatorException {

        RefreshToken refreshToken = refreshTokenService.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException(token, "Token not found"));

        refreshToken = refreshTokenService.verifyExpiration(refreshToken);

        User user = refreshToken.getUser();
        registerAccountValidator.validatePassword(newPassword);
        user.setPassword(encoder.encode(newPassword));

        userRepository.save(user);

        refreshTokenService.deleteByUserId(user.getId());

        return true;
    }
}
