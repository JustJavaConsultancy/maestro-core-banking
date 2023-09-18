package ng.com.justjava.corebanking.domain.enumeration;

public enum PolarisVulteAuthType {
    BANK_ACCOUNT("bank.account"),
    BVN("bvn");

    private final String name;

    PolarisVulteAuthType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
