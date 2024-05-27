package fortech.team2.persistence;

import fortech.team2.model.UserPreference;
import fortech.team2.model.enums.PatientSex;
import fortech.team2.model.enums.PatientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Integer> {
    Iterable<UserPreference> getUserPreferencesByCustomer_Id(Integer customerId);

    UserPreference getUserPreferenceByNameAndSexAndTypeAndBreedAndColourAndAgeAndFirstNameAndLastNameAndEmailAndPhoneAndAddress(
            String name, PatientSex sex, PatientType type, String breed, String colour, Integer age,
            String firstName, String lastName, String email, String phone, String address);

}
