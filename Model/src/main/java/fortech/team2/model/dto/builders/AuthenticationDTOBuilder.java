package fortech.team2.model.dto.builders;

import fortech.team2.model.Medic;
import fortech.team2.model.User;
import fortech.team2.model.dto.request.SignupMedicRequest;
import fortech.team2.model.dto.request.SignupRequest;
import fortech.team2.model.enums.UserRole;

public class AuthenticationDTOBuilder {

    /**
     * Use @NoArgsContructor instead of this
     */
    private AuthenticationDTOBuilder() {
    }

    /**
     * Simply name 'toEntity'
     */
    public static User fromSignupRequest(SignupRequest signupRequest) {
        return User.builder()
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .address(signupRequest.getAddress())
                .phone(signupRequest.getPhone())
                .email(signupRequest.getEmail())
                /**
                 * You could extract in a separate private static method what's between paranthesis and call it directly,
                 * instead of writing entire logic here
                 * hint: select the code you want to extract -> right-click -> Refactor -> Extract Method..
                 * ex: .role(getRightRole())
                 */
                .role(signupRequest.getIsAdmin() != null && signupRequest.getIsAdmin()
                        ? UserRole.ROLE_ADMIN
                        : UserRole.ROLE_CUSTOMER)
                .build();
    }

    /**
     * Simply name 'toEntity'
     */
    public static Medic fromSignupRequest(SignupMedicRequest signupMedicRequest) {
        return Medic.builder()
                .firstName(signupMedicRequest.getFirstName())
                .lastName(signupMedicRequest.getLastName())
                .address(signupMedicRequest.getAddress())
                .phone(signupMedicRequest.getPhone())
                .email(signupMedicRequest.getEmail())
                .education(signupMedicRequest.getEducation())
                .specializations(signupMedicRequest.getSpecializations())
                .role(UserRole.ROLE_MEDIC)
                .experience(signupMedicRequest.getExperience())
                .build();
    }

    public static SignupRequest toRegisterAccountDTO(User user) {
        return SignupRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .email(user.getEmail())
                .build();
    }
}
