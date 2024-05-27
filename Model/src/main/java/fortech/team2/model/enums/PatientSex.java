package fortech.team2.model.enums;

public enum PatientSex {
    FEMALE,
    MALE,
    OTHER,
    UNKNOWN;
    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}
