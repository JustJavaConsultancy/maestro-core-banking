package ng.com.justjava.corebanking.service.ussd;

import ng.com.justjava.corebanking.domain.JournalLine;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.service.JournalLineService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.UserService;
import ng.com.justjava.corebanking.service.dto.LostPinDTO;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Settings {
    private ServiceResponse response;
    private String ussdContent;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final Profile profile;
    private final HttpSession session;
    private final JournalLineService journalLineService;
    private final ProfileService profileService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");

    public Settings(String requestString, Profile profile, JournalLineService journalLineService, HttpSession session,
                    UserService userService, PasswordEncoder passwordEncoder, ProfileService profileService) {
        this.profile = profile;
        this.session = session;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.journalLineService = journalLineService;
        this.profileService = profileService;
        step1(requestString);
    }

    private ServiceResponse step1(String requestString) {

        System.out.println("Step 1==" + requestString);
        ussdContent = "Settings : \n" +
            "1. Change Pin\n" +
            "2. Forgot Pin\n";
        response = new ServiceResponse();
        if (requestString.split("#").length == 2) {
            response.setContent(ussdContent);
            response.setMsgType("1");
        } else {
            step2(requestString);
        }
        return response;
    }

    private ServiceResponse step2(String requestString) {
        // *7000#2#500#1
        System.out.println("Settings Step 2==" + requestString);
        response = new ServiceResponse();
        String message = "Enter Old Pin";
        if (requestString.split("#").length == 3) {
            String[] userOption = requestString.split("#");

            String option = userOption[2];
            if ("2".equalsIgnoreCase(option))
                message = "Enter Date of Birth (ddMMyyyy)";

            response.setContent(message);
            response.setMsgType("1");
        } else {
            step3(requestString);
        }

        return response;
    }

    private ServiceResponse step3(String requestString) {
        System.out.println("Settings Step 3==" + requestString);
        response = new ServiceResponse();
        if (requestString.split("#").length == 4) {
            String[] userOption = requestString.split("#");

            String option = userOption[2];
            String message = "Enter New Pin";
            if ("2".equalsIgnoreCase(option)) {
                try {
                    String dateOfBirth = userOption[3];
                    LocalDate.parse(dateOfBirth, formatter);
                    message = "Enter Last transaction amount";
                } catch (Exception e) {
                    System.out.println(e);
                    response.setContent("Invalid date format (ddMMyyyy)");
                    response.setMsgType("2");
                }
            } else if ("1".equalsIgnoreCase(option)) {
                String encryptedPin = profile.getPin();
                String currentPin = userOption[3];
                System.out.println("Current pin ===" + currentPin);

                if (!passwordEncoder.matches(currentPin, encryptedPin)) {
                    response.setContent("Incorrect Old pin");
                    response.setMsgType("2");
                    return response;
                }

            }

            response.setContent(message);
            response.setMsgType("1");
        } else {
            step4(requestString);
        }

        return response;
    }

    private ServiceResponse step4(String requestString) {
        // *7000#2#500#1
        System.out.println("Settings Step 4==" + requestString);
        response = new ServiceResponse();
        if (requestString.split("#").length == 5) {
            String[] userOption = requestString.split("#");
            String option = userOption[2];
            String message = "Confirm Pin";
            if ("2".equalsIgnoreCase(option)) {
                String lastTransactionAmount = userOption[4];
                try {
                    Double.parseDouble(lastTransactionAmount);
                    message = "Enter Date of Last transaction";
                } catch (Exception e) {
                    response.setContent("Invalid input amount");
                    response.setMsgType("2");
                    return response;
                }
            }

            response.setContent(message);
            response.setMsgType("1");
        } else {
            step5(requestString);
        }

        return response;
    }

    private ServiceResponse step5(String requestString) {
        System.out.println("step5 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 6) {
            String[] userOption = requestString.split("#");
            String option = userOption[2];

            String message = "PIN change successful";

            if ("2".equalsIgnoreCase(option)) {
                String dateOfLastTransaction = userOption[5];
                LocalDate dateOfLastTrans = LocalDate.now();
                try {
                    dateOfLastTrans = LocalDate.parse(dateOfLastTransaction, formatter);

                } catch (Exception e) {
                    System.out.println(e);
                    response.setContent("Invalid date format (ddMMyyyy)");
                    response.setMsgType("2");
                }


                String dateOfBirth = userOption[3];
                String lastTransactionAmount = userOption[4];

                LocalDate date = LocalDate.parse(dateOfBirth, formatter);
                double amount = Double.parseDouble(lastTransactionAmount);

                List<JournalLine> journalLines = journalLineService.findByWalletAccountAccountOwnerPhoneNumberOrderByDate(profile.getPhoneNumber(), session);
                Double journalLineAmount;
                LocalDate journalLineDate;
                if (!journalLines.isEmpty()){
                    JournalLine journalLine = journalLines.get(0);
                    journalLineAmount = journalLine.getAmount();
                    LocalDateTime transactionDate = journalLine.getTransactionDate();
                    journalLineDate = transactionDate.toLocalDate();
                }else {
                    journalLineAmount = amount;
                    journalLineDate = dateOfLastTrans;
                }

                System.out.println(profile.getDateOfBirth() + " ==== " + date);
                System.out.println(amount + " ==== " + journalLineAmount);
                System.out.println(dateOfLastTrans + " ==== " + journalLineDate);

                if (profile.getDateOfBirth().equals(date) ||
                    amount == journalLineAmount ||
                    dateOfLastTrans.equals(journalLineDate)) {

                    response.setContent("Enter your 4 digits pin(xxxx)");
                    response.setMsgType("1");
                } else {
                    response.setContent("Details mismatch");
                    response.setMsgType("2");
                }
                return response;
            } else if ("1".equalsIgnoreCase(option)) {

                String currentPin = userOption[3];
                String encryptedPin = profile.getPin();

                System.out.println(" The pin here ");
                if (!passwordEncoder.matches(currentPin, encryptedPin)) {
                    response.setContent("Incorrect Old pin");
                    response.setMsgType("2");
                    return response;
                }

                String newPin = userOption[4];
                String confirmPin = userOption[5];
                if (!newPin.equalsIgnoreCase(confirmPin)) {
                    response.setContent("New pin missmatch");
                    response.setMsgType("2");
                    return response;
                }

                boolean result = userService.changePin(profile.getPhoneNumber(), currentPin, newPin);
                if (!result) {
                    message = "failed";
                }
                response.setContent(message);
                response.setMsgType("2");
                return response;
            }
        } else {
            step6(requestString);
        }
        return response;
    }

    private ServiceResponse step6(String requestString) {
        System.out.println("step6 requestString.split(\"#\").length..."+requestString.split("#").length);
        if (requestString.split("#").length == 7) {
            String[] userOption = requestString.split("#");
            String message = "Confirm Pin";
            response.setContent(message);
            response.setMsgType("1");
        }else {
            step7(requestString);
        }
        return response;
    }

    private ServiceResponse step7(String requestString) {
        System.out.println("step7 requestString.split(\"#\").length..."+requestString.split("#").length);
        if (requestString.split("#").length == 8) {
            String[] userOption = requestString.split("#");
            String pin = userOption[6];
            String confirmPin = userOption[7];
            String message = "Pin reset successful";

            if (!pin.equalsIgnoreCase(confirmPin)) {
                message = "Pin mismatch";
            }
            LostPinDTO lostPinDTO = new LostPinDTO();
            lostPinDTO.setNewPin(pin);
            lostPinDTO.setPhoneNumber(profile.getPhoneNumber());
            profileService.updatePin(lostPinDTO);

            response.setContent(message);
            response.setMsgType("2");
        }
        return response;
    }

    public ServiceResponse getResponse() {
		return response;
	}

}
