package fortech.team2.services.impl;

import fortech.team2.model.User;
import fortech.team2.model.dto.request.ChangeAccountDetailsRequest;
import fortech.team2.model.enums.UserRole;
import fortech.team2.persistence.UserRepository;
import fortech.team2.persistence.exceptions.RepositoryException;
import fortech.team2.services.UserService;
import fortech.team2.validation.AccountRequestValidator;
import fortech.team2.validation.UserValidator;
import fortech.team2.validation.utils.ValidatorException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.function.Supplier;

@Service
public class UserServiceImpl implements UserService {

    /**
     * You could also declare these autowired fields as 'final'
     */
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AccountRequestValidator accountRequestValidator;

    @Override
    public User getUserByEmail(String email) throws RepositoryException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            /**
             * You should extract this message as a constant in a separated class named 'Constants' in 'utils' package , as public static final
             * ex: public static final String NO_USER_WITH_EMAIL = "User with email %s does not exist";
             * throw new RepositoryException(String.format(NO_USER_WITH_EMAIL, email));
             */
            throw new RepositoryException("User with email " + email + " does not exist");
        }
        return user;
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Iterable<User> getAllCustomers() {
        return userRepository.findAllByRole(UserRole.ROLE_CUSTOMER);
    }

    @Override
    public void updateUser(User owner) throws ValidatorException {
        userValidator.validate(owner);
        userRepository.save(owner);
    }

    @Override
    public User getUserById(Integer id) throws RepositoryException {
        /**
         * See the example above about String constants
         */
        Supplier<RepositoryException> e = () -> new RepositoryException("User with id " + id + " does not exist");
        return userRepository.findById(id).orElseThrow(e);
    }

    public void saveAvatar(Integer userId, MultipartFile file) throws IOException {
        /**
         * See the example above about String constants
         */
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Convert MultipartFile to byte[]
        byte[] imageData = file.getBytes();

        // Set the avatar field for the user
        user.setAvatar(imageData);

        // Save the user entity back to the database
        userRepository.save(user);
    }

    @Override
    public byte[] getAvatarByUserId(Integer userId) throws RepositoryException {
        /**
         * See the example above about String constants
         */
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user.getAvatar();
    }

    @Override
    public void updateAccountInformation(ChangeAccountDetailsRequest accountData) throws ValidatorException, RepositoryException {
        /**
         * See the example above about String constants
         */
        User user = userRepository.findById(accountData.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        // Check if the current password is correct
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), accountData.getCurrentPassword()));
        accountRequestValidator.validate(accountData); // passwords mathc?
        if (accountData.getNewPassword() != null) {
            user.setPassword(encoder.encode(accountData.getNewPassword()));
        }
        userRepository.save(user);
    }

}
