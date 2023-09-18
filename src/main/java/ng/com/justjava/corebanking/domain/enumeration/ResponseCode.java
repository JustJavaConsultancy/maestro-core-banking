package ng.com.justjava.corebanking.domain.enumeration;

public enum ResponseCode {
    SUCCESS("00"), ERROR("99");

    private final String code;

    ResponseCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
