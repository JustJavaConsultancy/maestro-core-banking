package ng.com.justjava.corebanking.domain.enumeration;

public enum NavsaTransactionStatus {

    NEW("NEW"), PROCESSED("PROCESSED"), PROCESSING("PROCESSING");

    private final String name;

    NavsaTransactionStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
