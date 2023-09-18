package ng.com.systemspecs.apigateway.domain.enumeration;

public enum PolarisVulteRequestType {
    GET_BALANCE("get_balance"),
    GET_STATEMENT("get_statement"),
    FUND_TRANSFER("transfer_funds"),
    DISBURSE("disburse"),
    COLLECT("Collect"),
    LOOKUP_BVN_MIN("lookup_bvn_min"),
    LOOKUP_BVN_MAX("lookup_bvn_max"),
    LOOKUP_BVN_MID("lookup_bvn_mid"),
    LOOKUP_ACCOUNT_MAX("lookup_account_max"),
    LOOKUP_ACCOUNT_MIN("lookup_account_min"),
    LOOKUP_ACCOUNT_MID("lookup_account_mid"),
    GET_ACCOUNT_MIN("get_accounts_min"),
    GET_ACCOUNT_MID("get_accounts_mid"),
    GET_ACCOUNT_MAX("get_accounts_max"),
    LOOKUP_NUBAN("lookup_nuban"),
    GET_BANKS("get_banks"),
    GET_BRANCH("list_branches"),
    OPEN_ACCOUNT("open_account"),
    OPEN_WALLET("open_wallet");

    private final String name;

    PolarisVulteRequestType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
