package services.impl;

import model.User;
import model.dto.request.ChangeAccountDetailsRequest;
import model.enums.UserRole;
import persistence.UserRepository;
import persistence.exceptions.RepositoryException;
import services.UserService;
import validation.AccountRequestValidator;
import validation.UserValidator;
import validation.utils.ValidatorException;
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

        Supplier<RepositoryException> e = () -> new RepositoryException("User with id " + id + " does not exist");
        return userRepository.findById(id).orElseThrow(e);
    }

    public void saveAvatar(Integer userId, MultipartFile file) throws IOException {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        byte[] imageData = file.getBytes();
        user.setAvatar(imageData);
        userRepository.save(user);
    }

    @Override
    public byte[] getAvatarByUserId(Integer userId) throws RepositoryException {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user.getAvatar();
    }

    @Override
    public void updateAccountInformation(ChangeAccountDetailsRequest accountData) throws ValidatorException, RepositoryException {

        User user = userRepository.findById(accountData.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), accountData.getCurrentPassword()));
        accountRequestValidator.validate(accountData);
        if (accountData.getNewPassword() != null) {
            user.setPassword(encoder.encode(accountData.getNewPassword()));
        }
        userRepository.save(user);
    }
}
