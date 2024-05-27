package fortech.team2.validation;

import fortech.team2.model.User;
import fortech.team2.validation.utils.ValidatorException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator extends AccountValidator {
    public void validate(User user) throws ValidatorException {
        validateEmail(user.getEmail());
        validateName(user.getFirstName());
        validateName(user.getLastName());
        validatePhoneNumber(user.getPhone());
    }
}
