package fortech.team2.restcontrollers;

import fortech.team2.model.User;
import fortech.team2.model.dto.UserDTO;
import fortech.team2.model.dto.builders.UserDTOBuilder;
import fortech.team2.model.dto.request.ChangeAccountDetailsRequest;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.UserService;
import fortech.team2.validation.utils.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('MEDIC')")
    public Iterable<UserDTO> getAllCustomers() {
        Iterable<User> users = userService.getAllCustomers();
        /**
         * You cannot call a builder method in a controller method. Do it in service !
         */
        return UserDTOBuilder.toUserDTOList(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(UserDTOBuilder.toUserDTO(userService.getUserById(id)));
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @PostMapping("/{userId}/avatar")
    public ResponseEntity<String> uploadAvatar(@PathVariable Integer userId, @RequestParam("file") MultipartFile file) {
        try {
            userService.saveAvatar(userId, file);
            /**
             * You should extract this message as a constant in a separated class named 'Constants' in 'utils' package , as public static final
             */
            return ResponseEntity.ok("Avatar uploaded successfully");
        } catch (Exception e) {
            /**
             * You should extract this message as a constant in a separated class named 'Constants' in 'utils' package , as public static final
             */
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload avatar: " + e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('MEDIC') or hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<String> update(@RequestBody ChangeAccountDetailsRequest accountData) {
        try {
            userService.updateAccountInformation(accountData);
            /**
             * You should extract this message as a constant in a separated class named 'Constants' in 'utils' package , as public static final
             */
            return ResponseEntity.ok("Data updated successfully");
        } catch (ValidatorException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/{id}/get-avatar")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Integer id) {
        try {
            /**
             * Do not leave this logic to be made by controller. Do it in service
             */
            byte[] avatar = userService.getAvatarByUserId(id);

            if (avatar == null || avatar.length == 0) {
                /**
                 * You should extract this message as a constant in a separated class named 'Constants' in 'utils' package , as public static final
                 */
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User has no avatar");
            }

            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(avatar);
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

}
