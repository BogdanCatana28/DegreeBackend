package services.impl;

import model.UserPreference;
import model.dto.UserPreferenceDTO;
import model.dto.builders.UserPreferenceDTOBuilder;
import persistence.UserPreferenceRepository;
import persistence.exceptions.RepositoryException;
import services.UserPreferenceService;
import services.UserService;
import validation.UserPreferenceValidator;
import validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPreferenceServiceImpl implements UserPreferenceService {

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserPreferenceValidator userPreferenceValidator;

    @Override
    public UserPreference addUserPreference(UserPreferenceDTO preferenceDTO) throws ValidatorException, RepositoryException {
        userPreferenceValidator.validate(preferenceDTO);

        if (checkIfUserPreferenceAlreadyExists(preferenceDTO)) {
            throw new RepositoryException("User preference already exists!");
        }

        UserPreference preference = UserPreferenceDTOBuilder.fromUserPreferenceDTO(preferenceDTO);
        preference.setCustomer(userService.getUserById(preferenceDTO.getCustomerId()));

        return userPreferenceRepository.save(preference);
    }

    @Override
    public Iterable<UserPreference> getUserPreferencesByUserId(Integer userId) {
        return userPreferenceRepository.getUserPreferencesByCustomer_Id(userId);
    }

    @Override
    public Boolean checkIfUserPreferenceAlreadyExists(UserPreferenceDTO preferenceDTO) throws RepositoryException {
        try {
            userPreferenceValidator.validate(preferenceDTO);
        } catch (ValidatorException e) {
            return false;
        }

        UserPreference preference = UserPreferenceDTOBuilder.fromUserPreferenceDTO(preferenceDTO);
        preference.setCustomer(userService.getUserById(preferenceDTO.getCustomerId()));

        preference = userPreferenceRepository.getUserPreferenceByNameAndSexAndTypeAndBreedAndColourAndAgeAndFirstNameAndLastNameAndEmailAndPhoneAndAddress(
                preference.getName(), preference.getSex(), preference.getType(), preference.getBreed(),
                preference.getColour(), preference.getAge(), preference.getFirstName(), preference.getLastName(),
                preference.getEmail(), preference.getPhone(), preference.getAddress()
        );
        return preference != null;
    }

    @Override
    public UserPreference getUserPreferenceById(Integer id) throws RepositoryException {
        return userPreferenceRepository.findById(id).orElseThrow(() -> new RepositoryException("Preference not found"));
    }

    @Override
    public UserPreference updateUserPreference(UserPreferenceDTO userPreferenceDTO) throws ValidatorException, RepositoryException {
        userPreferenceValidator.validate(userPreferenceDTO);
        UserPreference preference = UserPreferenceDTOBuilder.fromUserPreferenceDTO(userPreferenceDTO);
        preference.setCustomer(userService.getUserById(userPreferenceDTO.getCustomerId()));
        return userPreferenceRepository.save(preference);
    }

    @Override
    public void deleteUserPreference(Integer id) throws RepositoryException {
        userPreferenceRepository.findById(id).orElseThrow(() -> new RepositoryException("Preference not found"));
        userPreferenceRepository.deleteById(id);
    }
}
