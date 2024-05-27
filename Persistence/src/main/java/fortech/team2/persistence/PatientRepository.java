package fortech.team2.persistence;

import fortech.team2.model.Patient;
import fortech.team2.model.enums.PatientSex;
import fortech.team2.model.enums.PatientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Patient findByNameAndSexAndTypeAndBreedAndColorAndBirthDateAndWeight(
            String name, PatientSex gender, String type,
            String breed, String color, LocalDate birthDate, Float weight
    );

    List<Patient> findByNameContainingIgnoreCase(String name);

    Patient findByNameAndTypeAndBreedAndSexAndOwner_Email(String patientName, PatientType patientType, String patientBreed, PatientSex patientSex, String ownerEmail);

    @Query("SELECT DISTINCT p.type FROM Patient p")
    List<String> findDistinctTypes();

    @Query("SELECT DISTINCT p.breed FROM Patient p")
    List<String> findDistinctBreeds();

    @Query("SELECT DISTINCT p.sex FROM Patient p")
    List<String> findDistinctSexes();

    @Query("SELECT DISTINCT p.color FROM Patient p")
    List<String> findDistinctColors();

    List<Patient> findByType(PatientType type);

    List<Patient> findByBreedIgnoreCase(String breed);

    List<Patient> findBySex(PatientSex sex);

    List<Patient> findByColorIgnoreCase(String color);

    List<Patient> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);

    List<Patient> findByWeightBetween(Float minWeight, Float maxWeight);
}
