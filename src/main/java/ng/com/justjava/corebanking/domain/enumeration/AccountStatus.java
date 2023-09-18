package ng.com.justjava.corebanking.domain.enumeration;

public enum AccountStatus {

    ACTIVE("ACTIVE"), INACTIVE("INACTIVE"), SUSPENDED("SUSPENDED"), DEBIT_ON_HOLD("DEBIT_ON_HOLD");

    AccountStatus(String name) {
    }
}
