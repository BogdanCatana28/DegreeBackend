package fortech.team2.model.dto.builders;

import fortech.team2.model.User;
import fortech.team2.model.dto.UserDTO;

import java.util.ArrayList;

public class UserDTOBuilder {
    private UserDTOBuilder() {
    }

    /**
     * Simply name 'toDTO'
     */
    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .id(user.getId())
                .phone(user.getPhone())
                .build();
    }

    /**
     * Simply name 'toEntity'
     */
    public static User fromUserDTO(UserDTO userDTO) {
        return User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .phone(userDTO.getPhone())
                .address(userDTO.getAddress())
                .email(userDTO.getEmail())
                .id(userDTO.getId())
                .build();
    }

    public static Iterable<UserDTO> toUserDTOList(Iterable<User> users) {
        ArrayList<UserDTO> userDTOS = new ArrayList<>();

        for (User user : users) {
            userDTOS.add(toUserDTO(user));
        }

        return userDTOS;
    }
}
