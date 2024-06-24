package controllers;

import model.UserPreference;
import model.dto.UserPreferenceDTO;
import model.dto.builders.UserPreferenceDTOBuilder;
import persistence.exceptions.RepositoryException;
import services.UserPreferenceService;
import services.utils.ServiceException;
import validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/user-preferences")
public class UserPreferenceController {

    @Autowired
    private UserPreferenceService userPreferenceService;

    @PostMapping()
    @PreAuthorize("hasRole('MEDIC') OR hasRole('CUSTOMER') OR hasRole('ADMIN')")
    public ResponseEntity<UserPreferenceDTO> addUserPreference(@RequestBody UserPreferenceDTO preferenceDTO) {
        try {
            UserPreference preference = userPreferenceService.addUserPreference(preferenceDTO);

            UserPreferenceDTO preferenceDTOResponse = UserPreferenceDTOBuilder.toUserPreferenceDTO(preference);
            return ResponseEntity.ok(preferenceDTOResponse);
        } catch (ValidatorException | ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('MEDIC') OR hasRole('CUSTOMER') OR hasRole('ADMIN')")
    public ResponseEntity<UserPreferenceDTO> getUserPreferenceById(@PathVariable Integer id) {
        try {
            UserPreference preference = userPreferenceService.getUserPreferenceById(id);

            return ResponseEntity.ok(UserPreferenceDTOBuilder.toUserPreferenceDTO(preference));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('MEDIC') OR hasRole('CUSTOMER') OR hasRole('ADMIN')")
    public ResponseEntity<UserPreferenceDTO> updateUserPreference(@RequestBody UserPreferenceDTO userPreferenceDTO) {
        try {
            UserPreference preference = userPreferenceService.updateUserPreference(userPreferenceDTO);

            return ResponseEntity.ok(UserPreferenceDTOBuilder.toUserPreferenceDTO(preference));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('MEDIC') OR hasRole('CUSTOMER') OR hasRole('ADMIN')")
    public ResponseEntity<UserPreferenceDTO> deleteUserPreference(@PathVariable Integer id) {
        try {
            userPreferenceService.deleteUserPreference(id);
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        return ResponseEntity.ok().build();
    }


    @GetMapping("/customer/{id}")
    @PreAuthorize("hasRole('MEDIC') OR hasRole('CUSTOMER') OR hasRole('ADMIN')")
    public ResponseEntity<Iterable<UserPreferenceDTO>> getUserPreferencesByUserId(@PathVariable Integer id) {
        Iterable<UserPreference> preferences = userPreferenceService.getUserPreferencesByUserId(id);

        List<UserPreferenceDTO> preferenceDTOS = new ArrayList<>();
        preferences.forEach((preference) -> {
            preferenceDTOS.add(UserPreferenceDTOBuilder.toUserPreferenceDTO(preference));
        });
        return ResponseEntity.ok(preferenceDTOS);
    }

    @GetMapping("/exists")
    @PreAuthorize("hasRole('MEDIC') OR hasRole('CUSTOMER') OR hasRole('ADMIN')")
    public ResponseEntity<Boolean> checkIfUserPreferenceAlreadyExists(@RequestBody UserPreferenceDTO preferenceDTO) throws ValidatorException {

        Boolean response = null;
        try {
            response = userPreferenceService.checkIfUserPreferenceAlreadyExists(preferenceDTO);
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

        return ResponseEntity.ok(response);
    }
}
