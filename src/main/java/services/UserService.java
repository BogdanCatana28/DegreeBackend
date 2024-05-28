package services;

import model.User;
import model.dto.request.ChangeAccountDetailsRequest;
import persistence.exceptions.RepositoryException;
import validation.utils.ValidatorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    User getUserByEmail(String email) throws RepositoryException;

    Iterable<User> getAllUsers();

    Iterable<User> getAllCustomers();

    void updateUser(User owner) throws ValidatorException;

    User getUserById(Integer id) throws RepositoryException;

    void saveAvatar(Integer userId, MultipartFile file) throws IOException;

    byte[] getAvatarByUserId(Integer userId) throws RepositoryException;

    void updateAccountInformation(ChangeAccountDetailsRequest accountData) throws ValidatorException, RepositoryException;
}
