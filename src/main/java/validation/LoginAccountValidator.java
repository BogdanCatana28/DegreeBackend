package validation;

import model.dto.request.LoginRequest;
import validation.utils.ValidatorException;
import org.springframework.stereotype.Component;

@Component
public class LoginAccountValidator extends AccountValidator {
    public void validate(LoginRequest loginRequest) throws ValidatorException {
        validateEmail(loginRequest.getEmail());
        validatePassword(loginRequest.getPassword());
    }
}
