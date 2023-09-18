package ng.com.justjava.corebanking.domain.enumeration;

public enum SpecificChannel {

    BUY_AIRTIME("buyAirtime"),
    //    BUY_AIRTIME_ITEX("buyAirtimeItex"),
    BUY_AIRTIME_ITEX_MTN("MTNVTU"),
    BUY_AIRTIME_ITEX_GLO("GLOVTU"),
    BUY_AIRTIME_ITEX_AIRTEL("AIRTELVTU"),
    BUY_AIRTIME_ITEX_9MOBILE("9MOBILEVTU"),
    BUY_DATA_ITEX_MTN("MTNDATA"),
    BUY_DATA_ITEX_GLO("GLODATA"),
    BUY_DATA_ITEX_AIRTEL("AIRTELDATA"),
    BUY_DATA_ITEX_9MOBILE("9MOBILEDATA"),
    //    BUY_DATA_ITEX("buyDataItex"),
    BUY_DATA("buyData"),
    PAY_RRR("payRRR"),
    PAY_BILLER("payBiller"),
    PAY_BILLS("payUtils"),
    SEND_MONEY("sendMoney"),
    SEND_MONEY_TO_BANKS("sendMoneyToBanks"),
    HM_PAYROLL("HmPayroll"),
    HM_PAYROLL_INTRA("HmPayrollIntra"),
    SEND_MONEY_HMPAYROLL("sendMoneyHM"),
    SEND_MONEY_INTRA("sendMoneyInter"),
    REQUEST_MONEY("RequestMoney"),
    CASH_OUT("cashOut"),
    STP("stp"),
    OTHER("other"),
    PAY_ELECTRICITY("payElectricity"),
    PAY_IKEDC_ELECTRICITY_ITEX("IKEDC Disco"),
    PAY_AEDC_ELECTRICITY_ITEX("AEDC Disco"),
    PAY_PHEDC_ELECTRICITY_ITEX("PHEDC Disco"),
    PAY_EEDC_ELECTRICITY_ITEX("EEDC Disco"),
    PAY_IBEDC_ELECTRICITY_ITEX("IBEDC Disco"),
    PAY_EKEDC_ELECTRICITY_ITEX("EKEDC Disco"),
    PAY_KEDCO_ELECTRICITY_ITEX("KEDCO Disco"),
    PAY_WITH_REMITA("PayWithRemita"),
    PAY_WITH_USSD("USSD"),
    FUND_WALLET("fundWallet"),
    THIRD_PARTY("thirdParty"),
    LIBERTY("liberty"),
    CORAL_PAY("CoralPay"),
    FUND_WALLET_INTRA("fundWalletIntra"),
    IBILE_PAY("Ibile"),
    PAY_CABLE_TV_ITEX("PayCableTvItex"),
    STARTIMES_TV("PayStartimesItex"),
    PAY_INTERNET_ITEX("PayInternetItex"),
    FUND_WALLET_CASHCONNECT("fundWallet Cashconnect"),
    FUND_WALLET_POLARIS("fundWallet Polaris"),
    INSURANCE("InsuranceLoan"),
    POLARIS_CARD_REQUEST("Card Request Polaris"),

    //WALLENCY SCHOOLS APP CHANNELS
    WALLENCY_SCHOOLS_FEES_PAYMENT("payFees"),
    WALLENCY_SCHOOLS_FEES_PAYMENT_MCU("payFees MCU"),

    //PAYMASTA
    PAYMASTA_WALLET_TO_WALLET("payMasta Intra"),
    WYNK_WALLET_TO_WALLET("wynk Intra"),
    FORGE_WALLET_TO_WALLET("forge Intra"),
    WRAGBY_WALLET_TO_WALLET("wragby Intra");


    private final String name;

    SpecificChannel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "SpecificChannel{" +
            "name='" + name + '\'' +
            '}';
    }
}
