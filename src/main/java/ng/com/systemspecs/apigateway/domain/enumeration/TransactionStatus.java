package ng.com.systemspecs.apigateway.domain.enumeration;

public enum TransactionStatus {


    START("START"),
    PROCESSING("PROCESSING"),
    INCOMPLETE("INCOMPLETE"),
    COMPLETED("COMPLETED"),
    REJECTED("REJECTED"),
    OK("OK"),
    SUSPENDED("SUSPENDED"),
    INTRANSIT("INTRANSIT"),
    REVERSED("REVERSED");

    private final String name;

    TransactionStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TransactionStatus{" +
            "name='" + name + '\'' +
            '}';
    }
}
