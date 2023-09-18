package ng.com.justjava.corebanking.service.ussd;

import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.WalletExternalDTO;
import ng.com.justjava.corebanking.util.Utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreateWalletUssd {

    private final WalletAccountService walletAccountService;
    private final Utility utility;
    private final String phoneNumber;
    private ServiceResponse response;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");


    public CreateWalletUssd(String requestString, Utility utility,
                            WalletAccountService walletAccountService,
                            String phoneNumber) {
        this.walletAccountService = walletAccountService;
        this.utility = utility;
        this.phoneNumber = phoneNumber;

        step1(requestString);
    }


    private ServiceResponse step1(String requestString) {
        System.out.println("Step 1==" + requestString);
        response = new ServiceResponse();
        if (requestString.split("#").length == 1) {
            String content = "Enter firstname";
            String welcomeString = "Welcome to Systemspecs wallet.\nYou are about to create a Tier 1 Wallet\n";
            response.setContent(welcomeString + content);
            response.setMsgType("1");
        } else {
            step2(requestString);
        }

        return response;
    }

    private ServiceResponse step2(String requestString) {
        System.out.println("Step 2==" + requestString);
        if (requestString.split("#").length == 2) {
            response.setContent("Enter lastname");
            response.setMsgType("1");
        } else {
            step3(requestString);
        }
        return response;
    }

    private ServiceResponse step3(String requestString) {
        System.out.println("step3  createWallet requestString.split(\"#\").length..."
            + requestString.split("#").length + " and the requestString itself ===" + requestString);
        if (requestString.split("#").length == 3) {
            response.setContent("Enter date of birth (ddMMyyyy)");
            response.setMsgType("1");
        } else {
            step4(requestString);
        }
        return response;
    }

    private ServiceResponse step4(String requestString) {
        System.out.println("step4 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 4) {
            String option = requestString.split("#")[3];

            try {
                LocalDate.parse(option.trim(), formatter);
                String genderString = "Select gender : \n" +
                    "1. Male\r\n" +
                    "2. Female\r\n";
                response.setContent(genderString);
                response.setMsgType("1");
            } catch (Exception e) {
                e.printStackTrace();
                response.setContent("Invalid date format (ddMMyyyy)");
                response.setMsgType("2");
            }

        } else {
            step5(requestString);
        }
        return response;
    }

    private ServiceResponse step5(String requestString) {
        System.out.println("step5 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 5) {
            String option = requestString.split("#")[4];
            if (option.equalsIgnoreCase("1") || option.equalsIgnoreCase("2")) {
                response.setContent("Create your 4 digits pin(xxxx)");
                response.setMsgType("1");
            } else {
                response.setContent("Invalid Input");
                response.setMsgType("2");
            }

        } else {
            step6(requestString);
        }

        return response;
    }

    private ServiceResponse step6(String requestString) {
        System.out.println("step6 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 6) {
            String content = "Confirm your 4 digits pin(xxxx)";
            response.setContent(content);
            response.setMsgType("1");
        } else {
            step7(requestString);
            System.out.println("Calling step 7");

        }
        return response;
    }


    private ServiceResponse step7(String requestString) {

        System.out.println("The final\nstep7 requestString.split(\"#\").length..." + requestString.split("#").length);

        if (requestString.split("#").length == 7) {
            String[] entries = requestString.split("#");

            String phoneNumber = utility.formatPhoneNumber(this.phoneNumber);
            String firstName = entries[1].trim();
            String lastName = entries[2].trim();
            String dateOfBirth = entries[3].trim();
            String genderCode = entries[4].trim();
            String createPin = entries[5].trim();
            String confirmPin = entries[6].trim();

            LocalDate parseDateOfBirth = LocalDate.parse(dateOfBirth, formatter);

            if (!createPin.equalsIgnoreCase(confirmPin)) {
                response.setContent("Pin mismatch");
            } else {

                String genderName = "";
                if (genderCode.equalsIgnoreCase("2")) {
                    genderName = "FEMALE";
                } else if (genderCode.equalsIgnoreCase("1")) {
                    genderName = "MALE";
                } else {
                    response.setContent("Invalid gender selected");
                    response.setMsgType("2");
                    return response;
                }

                WalletExternalDTO walletExternalDTO = new WalletExternalDTO();
                walletExternalDTO.setAccountName(firstName);
                walletExternalDTO.setDateOfBirth(parseDateOfBirth);
                walletExternalDTO.setGender(genderName);
                walletExternalDTO.setFirstName(firstName);
                walletExternalDTO.setLastName(lastName);
                walletExternalDTO.setOpeningBalance(0.0);
                walletExternalDTO.setPassword("Default1234)(");
                walletExternalDTO.setPhoneNumber(phoneNumber);
                walletExternalDTO.setPin(createPin);

                String result = walletAccountService.createWalletExternalForUssd(walletExternalDTO);
                response.setContent(result);
            }

            response.setMsgType("2");

        }
        return response;
    }

    public ServiceResponse getResponse() {
        return response;
    }


    /*private static String getFormattedDateString(String option) {
        String day = option.substring(0, 2);
        String month = option.substring(2, 4);
        String year = option.substring(4,8);

        return day +
            "/" + month + "/" + year;
    }

    public static void main(String[] args) {
        String option = "13092020";
        System.out.println(getFormattedDateString(option));
    }*/


}
