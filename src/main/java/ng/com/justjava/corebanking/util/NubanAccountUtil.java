package ng.com.justjava.corebanking.util;

import org.apache.commons.lang3.StringUtils;

public class NubanAccountUtil {

    static String EMPTY="";
    static String OFI_NUBAN_SEED = "373373373373373"; // this is a constant
    static  String DMB_NUBAN_SEED = "373373373373";

    public static String generateDMBNuban(String bankCode, String customerNo) {

        String bankcode = "000" + bankCode;
        int calfig = 0;
        try {
            //check digit
            String nubanSerialNo = bankcode + customerNo;
            for (int ex = 0; ex < DMB_NUBAN_SEED.length(); ++ex) {
                calfig += Integer.parseInt(EMPTY + DMB_NUBAN_SEED.charAt(ex)) * Integer.parseInt(EMPTY + nubanSerialNo.charAt(ex));
            }
            calfig %= 10;
            calfig = 10 - calfig;
            if (calfig == 10) {
                calfig = 0;
            }
            return customerNo +  calfig;
        } catch (Exception var7) {
        }

        return null;

    }

    public static String generateOFINuban(String bankCode, String customerNo) {

        String bankcode = "9" + bankCode;
        int calfig = 0;
        try {
            //check digit
            String nubanSerialNo = bankcode + customerNo;
            for (int ex = 0; ex < OFI_NUBAN_SEED.length(); ++ex) {
                calfig += Integer.parseInt(EMPTY + OFI_NUBAN_SEED.charAt(ex)) * Integer.parseInt(EMPTY + nubanSerialNo.charAt(ex));
            }
            calfig %= 10;
            calfig = 10 - calfig;
            return customerNo + calfig;
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return null;

    }


    public static boolean isNubanAccount(String bankcode, String accountno, boolean checkForOfi) {

        String NUBAN_SEED = DMB_NUBAN_SEED;
        if (checkForOfi) {
            NUBAN_SEED = OFI_NUBAN_SEED;
        }
        String cipher = EMPTY;
        byte check = 0;
        int calfig = 0;
        boolean isNuban = false;
        if (accountno.length() == 10) {
            try {
                cipher = bankcode + accountno.substring(0, accountno.length() - 1);
                for (int ex = 0; ex < NUBAN_SEED.length(); ++ex) {
                    calfig += Integer.parseInt(EMPTY + NUBAN_SEED.charAt(ex)) * Integer.parseInt(EMPTY + cipher.charAt(ex));
                }
                calfig %= 10;
                calfig = 10 - calfig;
                if (!checkForOfi) {
                    if (calfig == 10) {
                        calfig = 0;
                    }
                }
                if ((calfig + EMPTY).equals(accountno.substring(accountno.length() - 1))) {
                    check = 1;
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }
        } else {
            check = 0;
        }

        return check == 1;
    }

    public static boolean isNubanAccountOFI(String bankCode, String accountno) {
        byte check = 0;
        String bankcode = "000" + bankCode;
        int calfig = 0;
        try {
            //check digit
            String
            cipher = bankcode + accountno.substring(0, accountno.length() - 1);
            for (int ex = 0; ex < DMB_NUBAN_SEED.length(); ++ex) {
                calfig += Integer.parseInt(EMPTY + DMB_NUBAN_SEED.charAt(ex)) * Integer.parseInt(EMPTY + cipher.charAt(ex));
            }
            calfig %= 10;
            calfig = 10 - calfig;
            if (calfig == 10) {
                calfig = 0;
            }
            if ((calfig + EMPTY).equals(accountno.substring(accountno.length() - 1))) {
                check = 1;
            }
        } catch (Exception var7) {
        }

        return check == 1;

    }


    public static void main(String[] args) {
        String response = "<?xml version='1.0' encoding='UTF-8'?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><ns2:verifyFingerWithDataResponse xmlns:ns2=\"http://IdentitySearch.nimc/\"><return/></ns2:verifyFingerWithDataResponse></S:Body></S:Envelope>";
        if (StringUtils.substringBetween(response, "<ns2:verifyFingerWithDataResponse",
            "ns2:verifyFingerWithDataResponse").equals("")) {
            System.out.println("Kindly use another finger");
        }
    }
}
