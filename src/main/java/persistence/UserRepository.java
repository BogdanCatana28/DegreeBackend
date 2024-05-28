package persistence;

import model.User;
import model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    Iterable<User> findAllByRole(UserRole role);

    Boolean existsByEmail(String email);
}
