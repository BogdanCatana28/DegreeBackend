package controllers;

import model.User;
import model.dto.UserDTO;
import model.dto.builders.UserDTOBuilder;
import model.dto.request.ChangeAccountDetailsRequest;
import persistence.exceptions.RepositoryException;
import services.UserService;
import validation.utils.ValidatorException;
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

            return ResponseEntity.ok("Avatar uploaded successfully");
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload avatar: " + e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('MEDIC') or hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<String> update(@RequestBody ChangeAccountDetailsRequest accountData) {
        try {
            userService.updateAccountInformation(accountData);

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

            byte[] avatar = userService.getAvatarByUserId(id);

            if (avatar == null || avatar.length == 0) {

                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User has no avatar");
            }

            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(avatar);
        } catch (RepositoryException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
