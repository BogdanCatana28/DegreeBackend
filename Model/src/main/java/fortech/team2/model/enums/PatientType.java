package fortech.team2.model.enums;

public enum PatientType {
    DOG,
    CAT,
    HORSE,
    COW,
    PIG,
    BIRD,
    REPTILE,
    AMPHIBIAN,
    FISH,
    RODENT,
    OTHER;

    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}
