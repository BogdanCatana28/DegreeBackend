package model.dto.builders;



import model.User;
import model.dto.UserDTO;

import java.util.ArrayList;

public class UserDTOBuilder {
    private UserDTOBuilder() {
    }

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
