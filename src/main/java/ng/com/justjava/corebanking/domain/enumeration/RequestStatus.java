package ng.com.justjava.corebanking.domain.enumeration;

public enum RequestStatus {

    AWAITING_APPROVAL("AWAITING_APPROVAL"), APPROVED("APPROVED"), REJECT("REJECT");

    private final String name;

    RequestStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "RequestStatus{" +
            "name='" + name + '\'' +
            '}';
    }
}
