package ng.com.justjava.corebanking.service.dto.stp;

public class ExtendedConstants {
    final public static String SYSTEM_ACCOUNT = "SYSTEM_ACCOUNT";
    final public static String ANONYMOUS_USER = "ANONYMOUS_USER";

    public enum ResponseStatus {
        API_SUCCESS_STATUS("success", "Request processed successfully"),
        API_FAIL_STATUS("fail", "Failed processing request"),
        API_ERROR_STATUS("error", "Error processing request");
        private String code;
        private String description;

        ResponseStatus(String code, String description) {
            this.code = code;
            this.description = description;

        }

        public static ResponseStatus getEnumByResponseCode(String value) {
            if (value == null)
                throw new IllegalArgumentException();
            for (ResponseStatus v : values())
                if (value.equalsIgnoreCase(v.getCode())) return v;
            throw new IllegalArgumentException();
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public enum ResponseCode {
        SUCCESS("00", "Approved or completed successfully"),
        PENDING_CASHOUT_SUCCESS("001", "Approved.Transaction sent to bank"),
        FAILED("99", "Unsuccessful transaction"),
        UNKNOWN("01", "Status unknown, please wait for settlement report"),
        INVALID_ACCOUNT("07", "Invalid account"),
        REQUEST_PROCESSING("09", "Status unknown, please wait for settlement report"),
        INVALID_TRANSACTION("12", "Invalid transaction"),

        INVALID_BANK_CODE("16", "Unknown Bank Code"),
        NO_ACTION("21", "No action is taken"),
        IN_SUFFICIENT_FUNDS("51", "Insufficient funds"),
        ACCOUNT_BLOCK("69", "Unsuccessful Account/Amount block"),

        LIMIT_EXCEEDED("61", "Transfer limit Exceeded"),
        DAILY_LIMIT_EXCEEDED("61", "Daily limit Exceeded"),
        BALANCE_LIMIT_EXCEEDED("61", "Deposit/Balance limit Exceeded"),
        ACCOUNT_UNBLOCK("70", "Unsuccessful Account/Amount unblock"),
        DUPLICATE_REQUEST("94", "Duplicate transaction"),
        SYSTEM_MALFUNCTION("96", "System malfunction"),
        TIMEOUT("97", "Timeout waiting for a response from destination"),
        ACCOUNT_ALREADY_EXITS("98", "Account already exists"),
        ACCOUNT_ALREADY_IN_USE("105", "Account already in use"),
        INVALID_SOURCE_ACCOUNT("99", "Invalid Source Account"),
        INVALID_BENEFICIARY_ACCOUNT("100", "Invalid Beneficiary Account"),
        SOURCE_ACCOUNT_BLOCK("101", "Unsuccessful Source Account/Amount block"),
        BENEFICIARY_ACCOUNT_BLOCK("102", "Unsuccessful Beneficiary Account/Amount block"),
        DEBIT_OK("103", "Debit ok"),
        INVALID_DATE_FORMAT("104", "Invalid Date format"),
        CUSTOMER_ACCOUNT_RECORD_MISMATCH("105", "Customer account mis match"),
        FRAUD_DETECTED("106", "Transaction Not Permitted"),
        INVALID_SOURCE_ACCOUNT_BANK_CODE("107", "Invalid Source Account Bank Code"),
        INVALID_BENEFICIARY_ACCOUNT_BANK_CODE("108", "Invalid Beneficiary Account Bank Code"),
        ACCOUNT_SIGNATURE_MISMATCH("109", "Account signature mismatch"),
        ACCOUNT_SCHEME_CLOSED("110", "Scheme is closed"),
        BANK_NOT_ONLINE("111", "Bank not online"),
        CREDIT_AMOUNT_ZERO_AFTER_FEES("112", "Credit amount will be zero after fees"),
        CASH_OUT_NOT_ALLOWED("113", "Cashout not allowed"),
        ACCOUNT_LOCK_ERROR("114", "Could not aquire lock on account"),
        REQUEST_VALIDATION_FAILED("115", "Could not validate the request"),
        INVALID_CUSTOMER_ID("116", "Invalid customer Id"),
        CUSTOMER_ACCOUNT_NOT_FOUND("117", "Account not found"),
        INVALID_OTP("118", "Invalid Otp!"),
        INVALID_ACCOUNT_STATUS("119", "Invalid account status!"),
        INVALID_AMOUNT("120", "Invalid amount!");

        private String code;
        private String description;

        ResponseCode(String code, String description) {
            this.code = code;
            this.description = description;

        }

        public static ResponseCode getEnumByResponseCode(String value) {
            if (value == null)
                throw new IllegalArgumentException();
            for (ResponseCode v : values())
                if (value.equalsIgnoreCase(v.getCode())) return v;
            throw new IllegalArgumentException();
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }


    }

}
