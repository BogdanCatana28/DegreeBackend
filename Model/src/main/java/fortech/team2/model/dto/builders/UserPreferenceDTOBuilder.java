package fortech.team2.model.dto.builders;

import fortech.team2.model.UserPreference;
import fortech.team2.model.dto.UserPreferenceDTO;
import fortech.team2.model.enums.PatientSex;
import fortech.team2.model.enums.PatientType;

public class UserPreferenceDTOBuilder {
    public UserPreferenceDTOBuilder() {
    }

    /**
     * Simply name 'toEntity'
     */
    public static UserPreference fromUserPreferenceDTO(UserPreferenceDTO preferenceDTO) {
        return UserPreference.builder()
                .id(preferenceDTO.getId())
                .firstName(preferenceDTO.getFirstName())
                .lastName(preferenceDTO.getLastName())
                .email(preferenceDTO.getEmail())
                .address(preferenceDTO.getAddress())
                .phone(preferenceDTO.getPhone())
                .breed(preferenceDTO.getBreed())
                .colour(preferenceDTO.getColour())
                .name(preferenceDTO.getName())
                .sex(PatientSex.valueOf(preferenceDTO.getSex().trim().toUpperCase()))
                .age(preferenceDTO.getAge())
                .type(PatientType.valueOf(preferenceDTO.getType().trim().toUpperCase()))
                .build();
    }

    /**
     * Simply name 'toDTO'
     */
    public static UserPreferenceDTO toUserPreferenceDTO(UserPreference preference) {
        return UserPreferenceDTO.builder()
                .id(preference.getId())
                .customerId(preference.getCustomer().getId())
                .firstName(preference.getFirstName())
                .lastName(preference.getLastName())
                .email(preference.getEmail())
                .address(preference.getAddress())
                .phone(preference.getPhone())
                .name(preference.getName())
                .colour(preference.getColour())
                .sex(preference.getSex().toString())
                .type(preference.getType().toString())
                .breed(preference.getBreed())
                .age(preference.getAge())
                .build();
    }
}
