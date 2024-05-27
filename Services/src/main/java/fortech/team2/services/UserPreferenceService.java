package fortech.team2.services;

import fortech.team2.model.UserPreference;
import fortech.team2.model.dto.UserPreferenceDTO;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.utils.ServiceException;
import fortech.team2.validation.utils.ValidatorException;

public interface UserPreferenceService {

    UserPreference addUserPreference(UserPreferenceDTO preferenceDTO) throws ValidatorException, ServiceException, RepositoryException;

    Iterable<UserPreference> getUserPreferencesByUserId(Integer userId);

    Boolean checkIfUserPreferenceAlreadyExists(UserPreferenceDTO preferenceDTO) throws RepositoryException;

    UserPreference getUserPreferenceById(Integer id) throws RepositoryException;

    UserPreference updateUserPreference(UserPreferenceDTO userPreferenceDTO) throws ValidatorException, RepositoryException;

    void deleteUserPreference(Integer id) throws RepositoryException;
}
