package fortech.team2.validation;

import fortech.team2.model.dto.UserDTO;
import fortech.team2.model.dto.UserPreferenceDTO;
import fortech.team2.model.enums.PatientSex;
import fortech.team2.model.enums.PatientType;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.UserService;
import fortech.team2.validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserPreferenceValidator {
    @Autowired
    UserService userService;

    public void validate(UserPreferenceDTO userPreferenceDTO) throws ValidatorException {
        // CUSTOMER
        //validateCustomer(userPreferenceDTO.getCustomer());
        validateCustomerId(userPreferenceDTO.getCustomerId());

        // PATIENT
        validateName(userPreferenceDTO.getName());
        validateType(String.valueOf(userPreferenceDTO.getType()));
        validateBreed(userPreferenceDTO.getBreed());
        validateSex(userPreferenceDTO.getSex());
        try {
            validateAge(userPreferenceDTO.getAge());
        } catch (Exception e) {
            throw new ValidatorException("Age must be a number");
        }
    }

    private void validateCustomer(UserDTO customer) throws ValidatorException {
        validateCustomerName(customer.getFirstName(), customer.getLastName());
        validateCustomerEmail(customer.getEmail());
        validateCustomerPhone(customer.getPhone());
        validateCustomerAddress(customer.getAddress());
        validateCustomerId(customer.getId());
    }

    private void validateCustomerName(String firstName, String lastName) throws ValidatorException {
        if (firstName == null || firstName.isEmpty()) {
            throw new ValidatorException("Name cannot be empty");
        }
        if (!firstName.matches("^[A-Za-z -]+$")) {
            throw new ValidatorException("First name can't contain digits!");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new ValidatorException("Name cannot be empty");
        }
        if (!lastName.matches("^[A-Za-z -]+$")) {
            throw new ValidatorException("Last name can't contain digits!");
        }
    }

    private void validateCustomerEmail(String email) throws ValidatorException {
        if (email == null || email.isEmpty()) {
            throw new ValidatorException("Email cannot be empty");
        }
        final Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[A-Za-z0-9._%+-]+@[a-z.-]+\\.(com|ro)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);

        if (!matcher.find()) {
            throw new ValidatorException("Invalid email format");
        }
    }

    private void validateCustomerPhone(String phoneNumber) throws ValidatorException {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new ValidatorException("Phone number cannot be empty");
        }

        if (phoneNumber.length() != 10) {
            throw new ValidatorException("Invalid phone number length");
        }

        final Pattern VALID_TELEPHONE_NUMBER_REGEX = Pattern.compile("^0(2|7)[0-9]+$");
        Matcher matcher = VALID_TELEPHONE_NUMBER_REGEX.matcher(phoneNumber);

        if (!matcher.find()) {
            throw new ValidatorException("Invalid phone number format");
        }
    }

    private void validateCustomerAddress(String address) throws ValidatorException {
        if (address == null) {
            throw new ValidatorException("Id cannot be empty");
        }
    }

    private void validateCustomerId(Integer id) throws ValidatorException {
        if (id == null) {
            throw new ValidatorException("Id cannot be empty!");
        }
        try {
            userService.getUserById(id);
        } catch (RepositoryException e) {
            throw new ValidatorException("User id must exist!");
        }

    }

    // PATIENT

    private void validateName(String name) throws ValidatorException {
        if (name == null || name.isEmpty()) {
            throw new ValidatorException("Name cannot be empty");
        }
        if (!name.matches("^[A-Za-z -]+$")) {
            throw new ValidatorException("Name can't contain digits!");
        }
    }

    private void validateAge(Integer age) throws ValidatorException {
        if (age == null) {
            return;
        }
        if (age > 1000) {
            throw new ValidatorException("You are not immortal, relax!");
        }
    }

    private void validateType(String type) throws ValidatorException {
        if (type == null || type.isEmpty()) {
            throw new ValidatorException("Type cannot be empty");
        }
        if (!type.chars().allMatch(Character::isLetter)) {
            throw new ValidatorException("Type can't contain digits");
        }
        try {
            PatientType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidatorException("Invalid value for type");
        }
    }

    private void validateColor(String color) throws ValidatorException {
        if (color == null) {
            return;
        }
        if (!color.chars().allMatch(Character::isLetter)) {
            throw new ValidatorException("Color can't contain digits");
        }
    }

    private void validateBreed(String breed) throws ValidatorException {
        if (breed == null) {
            return;
        }
        if (!breed.matches("^[A-Za-z -]+$")) {
            throw new ValidatorException("Breed can't contain digits!");
        }
    }

    private void validateSex(String sex) throws ValidatorException {
        if (sex == null || sex.isEmpty()) {
            throw new ValidatorException("Sex cannot be empty");
        }
        if (!sex.chars().allMatch(Character::isLetter)) {
            throw new ValidatorException("Sex can't contain digits");
        }
        try {
            PatientSex.valueOf(sex.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidatorException("Invalid value for sex");
        }
    }
}
