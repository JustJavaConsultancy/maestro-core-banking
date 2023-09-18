package ng.com.systemspecs.apigateway.service.ussd;

import com.fasterxml.jackson.core.JsonProcessingException;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.systemspecs.apigateway.service.BankService;
import ng.com.systemspecs.apigateway.service.BillerService;
import ng.com.systemspecs.apigateway.service.BillerTransactionService;
import ng.com.systemspecs.apigateway.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.dto.CustomFieldDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.UtilDTO;
import ng.com.systemspecs.apigateway.service.dto.ValidateMeterResponseDTO;
import ng.com.systemspecs.apigateway.util.Utility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BillsPayment {

    private final BankService bankService;
    private final WalletAccountService walletAccountService;
    private final BillerTransactionService billerTransactionService;
    private final BillerService billerService;
    private final Utility utility;
    private final Profile profile;
    private final String sessionID;
    private final HttpSession session;
    private final PasswordEncoder passwordEncoder;
    private ServiceResponse response;
    private List<WalletAccount> accounts;

    private StringBuilder content = new StringBuilder();

    public BillsPayment(String requestString, Profile profile, BankService bankService, BillerTransactionService billerTransactionService,
                        BillerService billerService, Utility utility, WalletAccountService walletAccountService, HttpSession session, String sessionID,
                        PasswordEncoder passwordEncoder) {
        this.bankService = bankService;
        this.profile = profile;
        this.walletAccountService = walletAccountService;
        this.session = session;
        this.sessionID = sessionID;
        this.passwordEncoder = passwordEncoder;
        this.billerTransactionService = billerTransactionService;
        this.utility = utility;
        this.billerService = billerService;

        step1(requestString);
    }

    private ServiceResponse step1(String requestString) {

        System.out.println("Step 1==" + requestString);
        response = new ServiceResponse();
        if (requestString.split("#").length == 2) {
            String ussdContent = "Select service type : \n" +
                "1. Electricity\r\n" +
                "2. Cable TV\r\n";
            response.setContent(ussdContent);
            response.setMsgType("1");
        } else {
            step2(requestString);
        }

        return response;
    }

    private ServiceResponse step2(String requestString) {
        System.out.println("Step 2==" + requestString);
        if (requestString.split("#").length == 3) {

            String[] userOption = requestString.split("#");
            String option = userOption[2];
            System.out.println("option ==> " + option);

            if ("1".equalsIgnoreCase(option.substring(0, 1))) {
                List<Biller> powerBillers = (List<Biller>) session.getServletContext().getAttribute(sessionID+"_powerBillers" );
               if (powerBillers == null || powerBillers.isEmpty()) {
                   powerBillers = billerService.getPowerBillers();
               }

                String pageNumber = option.substring(1);
                System.out.println("pageNumber ===> " + pageNumber);
                int pgNo = pageNumber.length();

                System.out.println("pgNo ==> " + pgNo);

                content = new StringBuilder();
                int count = pgNo * 3 + 1;
                for (int i = count; i < count + 3 && i <= powerBillers.size(); i++){
                    if (i == 1)
                        content.append("Select Your DISCO: \n");
                    Biller biller = powerBillers.get(i - 1);
                    String billerName = biller.getBiller();
                    StringBuilder billerNameString = new StringBuilder();
                    String[] billerNameArray = billerName.split(" ");
                    billerNameString
                        .append(billerNameArray[0])
                        .append(" ")
                        .append(billerNameArray[1]);

                    System.out.println("Billername ==> " + billerName);
                    content.append(" ").append(i).append(". ").append(billerNameString.toString()).append("\n");
                }

                if (pgNo != 0) {
                    content.append("98. Back\n");
                }

                if (pgNo != (powerBillers.size() / 3) -1 && powerBillers.size() > 3)
                    content.append("99. Next\n");

                response.setContent(content.toString());
                response.setMsgType("1");

                session.getServletContext().setAttribute(sessionID+"_powerBillers", powerBillers );

            } else if ("2".equalsIgnoreCase(option.substring(0, 1))) {
                List<Biller> tvBillers = billerService.getTvBillers();

                content = new StringBuilder("Select Service Provider: \n");
                int count = 1;
                for (Biller biller : tvBillers) {
                    content.append(" ").append(count).append(". ").append(biller.getBiller()).append("\n");
                    count = count + 1;
                }
                response.setContent(content.toString());
                response.setMsgType("1");

                session.getServletContext().setAttribute(sessionID+"_tvBillers", tvBillers );

            }
        } else {
            step3(requestString);
        }
        return response;
    }

    private ServiceResponse step3(String requestString) {
        // *7000#2#500#1
        System.out.println("step3  sendmoneytobank requestString.split(\"#\").length..."
            + requestString.split("#").length + " and the requestString itself ===" + requestString);
        if (requestString.split("#").length == 4) {
            String lastResponse = requestString.split("#")[2].substring(0, 1);
            String option = requestString.split("#")[3];

            if ("1".equalsIgnoreCase(lastResponse)) {
                int powerSelectedIndex = Integer.parseInt(option)-1;
                List<Biller> powerBillers = (List<Biller>) session.getServletContext().getAttribute(sessionID + "_powerBillers");
                List<BillerPlatform> powerBillerServices = billerService.getDataPlans(powerBillers.get(powerSelectedIndex).getBillerID());

                content = new StringBuilder("Select service option: \n");
                int count = 1;
                for (BillerPlatform bill : powerBillerServices) {
                    content.append(" ").append(count).append(". ").append(bill.getBillerPlatform()).append("\n");
                    count = count + 1;
                }
                response.setContent(content.toString());
                response.setMsgType("1");

                session.getServletContext().setAttribute(sessionID+"_powerBillerServices", powerBillerServices);
                session.getServletContext().removeAttribute(sessionID+"_powerBillers");

            } else if ("2".equalsIgnoreCase(lastResponse)) {

                int tvSelectedIndex = Integer.parseInt(option)-1;

                List<BillerPlatform> tvBillerServices  = (List<BillerPlatform>) session.getServletContext().getAttribute(sessionID+"_tvBillerServices");
                if (tvBillerServices == null || tvBillerServices.isEmpty()) {
                    List<Biller> tvBillers = (List<Biller>) session.getServletContext().getAttribute(sessionID + "_tvBillers");
                    tvBillerServices = billerService.getDataPlans(tvBillers.get(tvSelectedIndex).getBillerID());
                }

                String pageNumber = option.substring(1);
                int pgNo = pageNumber.length();
                System.out.println("pgNo ====> " + pgNo);
                int count = pgNo * 4 + 1;

                content = new StringBuilder();

                for (int i = count; i < count + 4 && i <= tvBillerServices.size(); i++) {
                    if (i == 1){
                        content.append("Select Bouquet: \n");
                    }
                    BillerPlatform billerPlatform = tvBillerServices.get(i - 1);
                    content.append(i).append(". ")
                        .append(billerPlatform.getBillerPlatform())
                        .append(" (")
                        .append(billerPlatform.getFixedAmount())
                        .append(")")
                        .append("\n");
                }

                if (pgNo != 0) {
                    content.append("98. Back\n");
                }

                if (pgNo != tvBillerServices.size() / 4 && tvBillerServices.size() > 4)
                    content.append("99. Next\n");

                response.setContent(content.toString());
                response.setMsgType("1");

                session.getServletContext().setAttribute(sessionID+"_tvBillerServices", tvBillerServices);
                session.getServletContext().removeAttribute(sessionID+"_tvBillers");

            }
        } else {
            step4(requestString);
        }
        return response;
    }

    private ServiceResponse step4(String requestString) {
        System.out.println("step4 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 5) {
            String lastResponse = requestString.split("#")[2].substring(0, 1);
            String option = requestString.split("#")[4];
            System.out.println("Selected service option no ===> " + option);
            if ("1".equalsIgnoreCase(lastResponse)) {
                response.setContent("Enter Meter Number");
                response.setMsgType("1");

            } else if ("2".equalsIgnoreCase(lastResponse)) {
                response.setContent("Enter Smartcard Number");
                response.setMsgType("1");
            }
        } else {
            step5(requestString);
        }
        return response;

    }

    private ServiceResponse step5(String requestString) {
        // *7000#2#500#1
        System.out.println("step5 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 6) {
            String lastResponse = requestString.split("#")[2].substring(0, 1);

            String[] entries = requestString.split("#");
            List<BillerPlatform> billerPlatforms = new ArrayList<>();
            String searchKeyword = "";

            if ("1".equalsIgnoreCase(lastResponse)) {
                billerPlatforms = (List<BillerPlatform>) session.getServletContext().getAttribute(sessionID + "_powerBillerServices");
                searchKeyword = "meter";
            }else {
                billerPlatforms = (List<BillerPlatform>) session.getServletContext().getAttribute(sessionID + "_tvBillerServices");
                searchKeyword = "smartcard";

            }
            int selectedServiceIndex = Integer.parseInt(entries[4])-1;
            System.out.println("selectedServiceIndex ===> " + selectedServiceIndex );
            BillerPlatform billerPlatform = billerPlatforms.get(selectedServiceIndex);
            String serviceTypeId = String.valueOf(billerPlatform.getBillerplatformID());
            System.out.println("serviceTypeId ==>" +serviceTypeId);

            List<BillerCustomFieldOption> customFields = billerService.getCustomFields(serviceTypeId);
            Set<BillerServiceOption> customFieldOptions = customFields.get(0).getBillerServiceOptions();
            List<CustomFieldDTO> customFieldList = new ArrayList<>();

            System.out.println("customFieldOptions ==>" +customFieldOptions);

            if (customFieldOptions != null) {
                for (BillerServiceOption serviceOption : customFieldOptions) {
                    if (serviceOption.getName().toLowerCase().contains(searchKeyword)) {
                        long serviceOptionId = serviceOption.getServiceOptionId();
                        CustomFieldDTO customFieldDTO = new CustomFieldDTO(String.valueOf(serviceOptionId), entries[5]);
                        customFieldList.add(customFieldDTO);
                    }
                }
            }

            UtilDTO utilDTO = new UtilDTO();
            utilDTO.setServiceId(serviceTypeId);
            utilDTO.setPhoneNumber(profile.getPhoneNumber());
            utilDTO.setCustomField(customFieldList);
            utilDTO.setAmount(1.0);

            if ("2".equalsIgnoreCase(lastResponse)) {
                utilDTO.setAmount(billerPlatform.getFixedAmount());
            }

            try {
                GenericResponseDTO genericResponseDTO = billerTransactionService.validateMeter(utilDTO, session);
                if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                    session.getServletContext().setAttribute(sessionID + "_utilDataDTO", utilDTO);
                    ValidateMeterResponseDTO data = (ValidateMeterResponseDTO) genericResponseDTO.getData();
                    if ("1".equalsIgnoreCase(lastResponse)) {
                        content = new StringBuilder("Meter Successfully Verified\n");
                        content
                            .append("Name: ")
                            .append(data.getName())
                            .append("\n")
                            .append("Address : " )
                            .append(data.getAddress())
                         .append("\n");

                        if (!StringUtils.isEmpty(data.getAmountDue())){
                            content.append("Amount Due : ")
                                .append(data.getAmountDue())
                                .append("\n");
                            session.getServletContext().setAttribute(sessionID+"_amountDue", data.getAmountDue());

                        }
                        content.append("Enter Amount");

                        response.setContent(content.toString());
                        response.setMsgType("1");
                    } else if ("2".equalsIgnoreCase(lastResponse)) {
                        content = new StringBuilder("Smartcard Successfully Verified\n ");
                        content.append("Name: ")
                            .append(data.getName())
                            .append("\n")
                            .append("Address : " )
                            .append(data.getAddress())
                            .append("\n");
                        accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
                        System.out.println(" After fetching the accounts==" + accounts);

                        session.getServletContext().setAttribute(sessionID + "_accounts", accounts);

                        if (accounts != null) {
                            if (accounts.size() > 1) {
                                content.append(getListOfWalletContent());
                                response.setContent(content.toString());
                                response.setMsgType("1");
                            } else {
                                String request = requestString + "#" + accounts.get(0).getAccountNumber();
                                session.getServletContext().setAttribute(sessionID, request);
                                response.setContent(content.toString());
                                step6(request);
                            }
                        }
                    }
                }else {
                    response.setContent(genericResponseDTO.getMessage());
                    response.setMsgType("2");
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        } else {
            step6(requestString);
        }
        return response;
    }

    private ServiceResponse step6(String requestString) {
        // *7000#2#500#1
        System.out.println("step6 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 7) {
            String lastResponse = requestString.split("#")[2].substring(0, 1);

            if ("1".equalsIgnoreCase(lastResponse)) {

                String amount = requestString.split("#")[6];
                try {
                    double amountDouble = Double.parseDouble(amount);
                    String amountDueStr = (String) session.getServletContext().getAttribute(sessionID + "_amountDue");
                    try {
                        double amountDue = Double.parseDouble(amountDueStr);
                        if (amountDouble < amountDue){
                            response.setContent("Amount [" +utility.formatMoney(amountDouble)+"] is less than RRR Amount Due [" + utility.formatMoney(amountDue) + "]");
                            response.setMsgType("2");
                        }
                    }catch (Exception e){
                        response.setContent("Invalid amount due : " + amountDueStr );
                        response.setMsgType("2");
                    }
                }catch (Exception e){
                    response.setContent("Invalid amount entered : " + amount );
                    response.setMsgType("2");
                }

                accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
                System.out.println(" After fetching the accounts==" + accounts);

                session.getServletContext().setAttribute(sessionID + "_accounts", accounts);

                if (accounts != null) {
                    if (accounts.size() > 1) {
                        content.append(getListOfWalletContent());
                        response.setContent(content.toString());
                        response.setMsgType("1");
                    } else {
                        String request = requestString + "#" + accounts.get(0).getAccountNumber();
                        session.getServletContext().setAttribute(sessionID, request);
                        step7(request);
                    }
                }
            } else if ("2".equalsIgnoreCase(lastResponse)) {
                UtilDTO utilDTO = (UtilDTO) session.getServletContext().getAttribute(sessionID + "_utilDataDTO");

                if (response.getContent() != null) {
                    content.append(response.getContent());
                }
                content.append("You are about to make a purchase ")
                    .append(utility.formatMoney(utilDTO.getAmount()))
                    .append(".\n")
                    .append("Enter Your Pin");

                response.setContent(content.toString());
                response.setMsgType("1");
            }
        } else {
            step7(requestString);
        }
        return response;
    }

    private ServiceResponse step7(String requestString) {
        // *7000#2#500#1
        System.out.println("step7 requestString.split(\"#\").length..." + requestString.split("#").length);

        if (requestString.split("#").length == 8) {
            String lastResponse = requestString.split("#")[2].substring(0, 1);

            if ("1".equalsIgnoreCase(lastResponse)) {
                String amount = requestString.split("#")[6];
                content.append("You are about to make a purchase ")
                    .append(utility.formatMoney(Double.parseDouble(amount)))
                    .append(".\n")
                    .append("Enter Your Pin");
                response.setContent(content.toString());
                response.setMsgType("1");

            } else if ("2".equalsIgnoreCase(lastResponse)) {
                String pin = requestString.split("#")[7];
                if (!passwordEncoder.matches(pin, profile.getPin())) {
                    response.setContent("Invalid Pin");
                    response.setContent("2");
                    requestString = requestString.replace("#" + pin, "");
                    System.out.println(" The requestString after failed pin entered===" + requestString);
                    session.getServletContext().setAttribute(sessionID, requestString);
                }else {
                    System.out.println(" Finally=========================================" + requestString);

                    String[] entries = requestString.split("#");
                    for (String entry : entries) {
                        System.out.println(" The Entries===" + entry);
                    }

                    String sourceAccountnumber;
                    double amount;
                    accounts = (List<WalletAccount>) session.getServletContext().getAttribute(sessionID + "_accounts");

                    List<BillerPlatform> tvBillerServices = (List<BillerPlatform>) session.getServletContext().getAttribute(sessionID+"_tvBillerServices");
                    int selectedServiceIndex = Integer.parseInt(entries[4])-1;
                    BillerPlatform billerPlatform = tvBillerServices.get(selectedServiceIndex);

                    if (accounts.size() > 1) {
                        sourceAccountnumber = accounts.get(Integer.parseInt(entries[6]) - 1).getAccountNumber();
                    } else {
                        sourceAccountnumber = entries[6];
                    }

                    amount = billerPlatform.getFixedAmount();

                    ServiceResponse serviceResponse = payBills(sourceAccountnumber, amount);
                    session.getServletContext().removeAttribute(sessionID+"_tvBillerServices");
                    return serviceResponse;
                }
            }
        }  else {
            step8(requestString);
        }
        return response;
    }

    private ServiceResponse step8(String requestString) {
        // *7000#2#500#1
        System.out.println("step7 requestString.split(\"#\").length..." + requestString.split("#").length);

        System.out.println(" The final ");
        if (requestString.split("#").length == 9) {
            String lastResponse = requestString.split("#")[2].substring(0, 1);
            if ("1".equalsIgnoreCase(lastResponse)) {
                String pin = requestString.split("#")[8];
                if (!passwordEncoder.matches(pin, profile.getPin())) {
                    response.setContent("Invalid Pin");
                    requestString = requestString.replace("#" + pin, "");
                    System.out.println(" The requestString after failed pin entered===" + requestString);
                    session.getServletContext().setAttribute(sessionID, requestString);
                } else {
                    System.out.println(" Finally=========================================" + requestString);

                    String[] entries = requestString.split("#");
                    for (String entry : entries) {
                        System.out.println(" The Entries===" + entry);

                    }

                    accounts = (List<WalletAccount>) session.getServletContext().getAttribute(sessionID + "_accounts");

                    String sourceAccountnumber;
                    double amount;

                    if (accounts.size() > 1) {
                        sourceAccountnumber = accounts.get(Integer.parseInt(entries[7]) - 1).getAccountNumber();
                    } else {
                        sourceAccountnumber = entries[7];
                    }

                    amount = Double.parseDouble(entries[6]);

                    ServiceResponse serviceResponse = payBills(sourceAccountnumber, amount);
                    session.getServletContext().removeAttribute(sessionID+"_powerBillerServices");
                    return serviceResponse;
                }
            }
        }
        return response;
    }

    private ServiceResponse payBills(String sourceAccountNumber, double amount) {

        UtilDTO utilDTO = (UtilDTO) session.getServletContext().getAttribute(sessionID + "_utilDataDTO");

        utilDTO.setTransRef(utility.getUniqueTransRef());
        utilDTO.setNarration("Ussd/Bills payment");
        utilDTO.setSourceAccountNo(sourceAccountNumber);
        utilDTO.setAmount(amount);

        GenericResponseDTO genericResponseDTO = billerTransactionService.payForUtils(utilDTO);

        if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
            response.setContent("Your payment of " + utility.formatMoney(utilDTO.getAmount()) + " for bill payment is successful. Thank you");
        } else {
            response.setContent(genericResponseDTO.getMessage());
        }
        response.setMsgType("2");

        session.getServletContext().removeAttribute(sessionID + "_accounts");
        return response;
    }

    public ServiceResponse getResponse() {
        return response;
    }


    private String getListOfWalletContent() {
        int count = 1;
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("Select Your Wallet to Debit: \n");
        for (WalletAccount walletAccount : accounts) {
            contentBuilder
                .append(" ")
                .append(count).append(". ")
                .append(walletAccount.getAccountNumber())
                .append(" (")
                .append(utility.formatMoney(Double.valueOf(walletAccount.getActualBalance())).replaceFirst("NGN", "N"))
                .append(")\n");

            count = count + 1;
        }
        return contentBuilder.toString();
    }

}
