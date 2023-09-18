package ng.com.systemspecs.apigateway.domain.enumeration;

public enum UserStatus {

    FLAG_CUSTOMER("FLAG_CUSTOMER"),
    DEACTIVATE_CUSTOMER("DEACTIVATE_CUSTOMER"),
    DEACTIVATE_CUSTOMER_USER("DEACTIVATE_CUSTOMER_USER"),
    DEACTIVATE_CUSTOMER_PIN("DEACTIVATE_CUSTOMER_PIN"),
    UPDATE_CUSTOMER("UPDATE_CUSTOMER"),
    OK("OK");


    private final String name;

    UserStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "UserStatus{" +
            "name='" + name + '\'' +
            '}';
    }
}
