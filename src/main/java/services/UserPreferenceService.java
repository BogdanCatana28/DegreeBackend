package services;

import model.UserPreference;
import model.dto.UserPreferenceDTO;
import persistence.exceptions.RepositoryException;
import services.utils.ServiceException;
import validation.utils.ValidatorException;

public interface UserPreferenceService {

    UserPreference addUserPreference(UserPreferenceDTO preferenceDTO) throws ValidatorException, ServiceException, RepositoryException;

    Iterable<UserPreference> getUserPreferencesByUserId(Integer userId);

    Boolean checkIfUserPreferenceAlreadyExists(UserPreferenceDTO preferenceDTO) throws RepositoryException;

    UserPreference getUserPreferenceById(Integer id) throws RepositoryException;

    UserPreference updateUserPreference(UserPreferenceDTO userPreferenceDTO) throws ValidatorException, RepositoryException;

    void deleteUserPreference(Integer id) throws RepositoryException;
}
