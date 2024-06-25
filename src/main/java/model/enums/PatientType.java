package model.enums;

public enum PatientType {
    DOG,
    CAT,
    BIRD,
    REPTILE,
    AMPHIBIAN,
    FISH,
    RODENT,
    EXOTIC_MAMMAL;

    @Override
    public String toString() {
        return this.name().charAt(0) + this.name().substring(1).toLowerCase().replace('_', ' ');
    }
}
