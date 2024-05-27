package fortech.team2.validation;

import fortech.team2.model.dto.request.ChangeAccountDetailsRequest;
import fortech.team2.validation.utils.ValidatorException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AccountRequestValidator {
    public void validate(ChangeAccountDetailsRequest data) throws ValidatorException {
        validateName(data.getFirstName());
        validateName(data.getLastName());
        validateEmail(data.getEmail());
        validatePhone(data.getPhone());
        validatePassword(data.getNewPassword());
        validatePassword(data.getCurrentPassword());
    }

    // if received data is null, ignore it
    private boolean validateNotNull(Object value) {
        return value != null;
    }

    private void validatePassword(String password) throws ValidatorException {
        if (validateNotNull(password)) {
            if (password.length() < 8) {
                throw new ValidatorException("Password must have at least 8 characters");
            }
        }
    }

    private void validatePhone(String phoneNumber) throws ValidatorException {
        if (validateNotNull(phoneNumber)) {
            if (phoneNumber.length() != 10) {
                throw new ValidatorException("Invalid phone number length");
            }

            final Pattern VALID_TELEPHONE_NUMBER_REGEX = Pattern.compile("^0(2|7)[0-9]+$");
            Matcher matcher = VALID_TELEPHONE_NUMBER_REGEX.matcher(phoneNumber);

            if (!matcher.find()) {
                throw new ValidatorException("Invalid phone number format");
            }
        }
    }

    private void validateEmail(String email) throws ValidatorException {
        if (validateNotNull(email)) {
            final Pattern VALID_EMAIL_ADDRESS_REGEX =
                    Pattern.compile("^[A-Za-z0-9._%+-]+@[a-z.-]+\\.(com|ro)$", Pattern.CASE_INSENSITIVE);
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);

            if (!matcher.find()) {
                throw new ValidatorException("Invalid email format");
            }
        }

    }
    
    private void validateName(String name) throws ValidatorException {
        if (validateNotNull(name)) {
            if (!name.matches("^[A-Za-z -]+$")) {
                throw new ValidatorException("Name can't contain digits!");
            }
        }

    }

}
