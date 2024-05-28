package validation;

import model.dto.request.SignupRequest;
import validation.utils.ValidatorException;
import org.springframework.stereotype.Component;

@Component
public class RegisterAccountValidator extends AccountValidator {
    public void validate(SignupRequest signupRequest) throws ValidatorException {
        validateName(signupRequest.getFirstName());
        validateName(signupRequest.getLastName());
        validateEmail(signupRequest.getEmail());
        validatePassword(signupRequest.getPassword());
        validatePhoneNumber(signupRequest.getPhone());
    }
}
