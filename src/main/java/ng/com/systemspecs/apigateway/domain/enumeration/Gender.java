package ng.com.systemspecs.apigateway.domain.enumeration;

/**
 * The Gender enumeration.
 */
public enum Gender {
    FEMALE("F"), MALE("M");

    private final String name;

    Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
