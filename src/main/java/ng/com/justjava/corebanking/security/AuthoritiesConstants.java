package ng.com.justjava.corebanking.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String SUPER_ADMIN = "ROLE_SUPER_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String ANALYST = "ROLE_ANALYST";

    public static final String OPERATIONS = "ROLE_OPERATIONS";

    public static final String SUPPORT = "ROLE_SUPPORT";

    public static final String AUDIT = "ROLE_AUDIT";


    public static final String POUCHII_MERCHANT = "ROLE_MERCHANT";


    //use format ROLE_{SCHEMENAME}_ADMIN
    public static final String IBILE_ADMIN = "ROLE_IBILE_ADMIN";
    public static final String HUMAN_MANAGER_ADMIN = "ROLE_HUMANMANAGER_ADMIN";
    public static final String WYNK_ADMIN = "ROLE_WYNK_ADMIN";
    public static final String NAVSA_ADMIN = "ROLE_NAVSA_ADMIN";
    public static final String FUNDACAUSE_ADMIN = "ROLE_FUNDACAUSE_ADMIN";
    public static final String MCPHERSON_ADMIN = "ROLE_MCPHERSON_ADMIN";
    public static final String MCPHERSON_FINANCIAL_ADMIN = "ROLE_MCPHERSON_FINANCIAL_ADMIN";
    public static final String PAYMASTA_ADMIN = "ROLE_PAYMASTA_ADMIN";
    public static final String POLARIS_ADMIN = "ROLE_POLARIS_ADMIN";
    public static final String WRAGBY_ADMIN = "ROLE_WRAGBY_ADMIN";

    private AuthoritiesConstants() {
    }
}
