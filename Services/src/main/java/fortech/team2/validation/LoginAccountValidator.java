package fortech.team2.validation;

import fortech.team2.model.dto.request.LoginRequest;
import fortech.team2.validation.utils.ValidatorException;
import org.springframework.stereotype.Component;

@Component
public class LoginAccountValidator extends AccountValidator {
    public void validate(LoginRequest loginRequest) throws ValidatorException {
        validateEmail(loginRequest.getEmail());
        validatePassword(loginRequest.getPassword());
    }
}
