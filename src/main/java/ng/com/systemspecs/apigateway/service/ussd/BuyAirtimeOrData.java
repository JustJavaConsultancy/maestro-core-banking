package ng.com.systemspecs.apigateway.service.ussd;

import ng.com.systemspecs.apigateway.domain.BillerPlatform;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.WalletAccount;
import ng.com.systemspecs.apigateway.service.BillerService;
import ng.com.systemspecs.apigateway.service.BillerTransactionService;
import ng.com.systemspecs.apigateway.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.dto.BuyAirtimeDTO;
import ng.com.systemspecs.apigateway.service.dto.BuyDataDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.util.Utility;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuyAirtimeOrData {

    private final WalletAccountService walletAccountService;
    private final BillerTransactionService billerTransactionService;
    private final BillerService billerService;
    private final Utility utility;
    private final Profile profile;
    private final String sessionID;
    private final HttpSession session;
    private final String networkProvider;
    private final PasswordEncoder passwordEncoder;
    private ServiceResponse response;
    private List<WalletAccount> accounts;
    private Map<Integer, String> providerNames;
    private Map<String, String> providerBillerIds;


    private final String selectNetworkProvider = "Select network provider : \n" +
        "1. MTN\r\n" +
        "2. AIRTEL\r\n" +
        "3. 9-Mobile\r\n" +
        "4. GLO\r\n";


    public BuyAirtimeOrData(String requestString, Profile profile, BillerTransactionService billerTransactionService,
                            BillerService billerService, Utility utility, WalletAccountService walletAccountService, HttpSession session, String sessionID,
                            PasswordEncoder passwordEncoder, String networkProvider) {
        this.profile = profile;
        this.walletAccountService = walletAccountService;
        this.session = session;
        this.sessionID = sessionID;
        this.passwordEncoder = passwordEncoder;
        this.billerTransactionService = billerTransactionService;
        this.utility = utility;
        this.billerService = billerService;
        this.networkProvider = networkProvider;

        buildProvidersCode();
        buildBillerServiceTypeId();

        step1(requestString);
    }

    private void buildBillerServiceTypeId() {
        providerBillerIds = new HashMap<>();
        providerBillerIds.put("MTN", "C0000263754");
        providerBillerIds.put("AIRTEL", "C0000264103");
        providerBillerIds.put("GLO", "C0000264101");
        providerBillerIds.put("9-Mobile", "C0000264104");
        providerBillerIds.put("AIRTIME RECHARGE MOBIFIN", "C0000285025");
    }

    private void buildProvidersCode() {
        providerNames = new HashMap<>();
        providerNames.put(1, "MTN");
        providerNames.put(2, "AIRTEL");
        providerNames.put(3, "9-Mobile");
        providerNames.put(4, "GLO");
    }


    private ServiceResponse step1(String requestString) {

        System.out.println("Step 1== " + requestString);
        response = new ServiceResponse();
        if (requestString.split("#").length == 2) {
            String ussdContent = "Select service type : \n" +
                "1. Airtime for self\r\n" +
                "2. Airtime for others\r\n" +
                "3. Data for Self\r\n" +
                "4. Data for others\r\n";
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

            String option = requestString.split("#")[2];

            if ("1".equalsIgnoreCase(option) || "2".equalsIgnoreCase(option)) {
                response.setContent("Enter amount");
                response.setMsgType("1");
            } else if ("3".equalsIgnoreCase(option)) {

                String networkId = getNetworkId();
                if (networkId == null) {
                    response.setContent("BAD NETWORK");
                    response.setMsgType("2");
                }

                String request = requestString + "#" + networkId;
                session.getServletContext().setAttribute(sessionID, request);
                step3(request);

            } else if ("4".equalsIgnoreCase(option)) {
                response.setContent("Enter phone number");
                response.setMsgType("1");
            }
        } else {
            step3(requestString);
        }
        return response;
    }

    private ServiceResponse step3(String requestString) {
        System.out.println("step3  sendmoneytobank requestString.split(\"#\").length..."
            + requestString.split("#").length + " and the requestString itself ===" + requestString);
        if (requestString.split("#").length == 4) {
            String content = "";
            String lastResponse = requestString.split("#")[2];
            String option = requestString.split("#")[3];

            if ("1".equalsIgnoreCase(lastResponse)) {

                String networkId = getNetworkId();

                if (networkId == null) {
                    response.setContent("BAD NETWORK");
                    response.setMsgType("2");
                }

                String request = requestString + "#" + networkId;
                session.getServletContext().setAttribute(sessionID, request);
                step4(request);

            } else if ("2".equalsIgnoreCase(lastResponse)) {
                response.setContent("Enter phone number");
                response.setMsgType("1");
            } else if ("3".equalsIgnoreCase(lastResponse)) {
                content = "Select data plan : \n";
                if ("1".equalsIgnoreCase(option) || "2".equalsIgnoreCase(option) || "3".equalsIgnoreCase(option) || "4".equalsIgnoreCase(option)) {
                    String providerName = providerNames.get(Integer.parseInt(option));
                    String providerId = providerBillerIds.get(providerName);
                    content = getListOfDataProviderContent(content, providerName, providerId);

                    response.setContent(content);
                    response.setMsgType("1");
                }

            } else if ("4".equalsIgnoreCase(lastResponse)) {
                response.setContent(selectNetworkProvider);
                response.setMsgType("1");
            }
        } else {
            step4(requestString);
        }
        return response;
    }

    private ServiceResponse step4(String requestString) {
        System.out.println("step4 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 5) {
            String content = "";
            String lastResponse = requestString.split("#")[2];
            String option = requestString.split("#")[4];

            if ("1".equalsIgnoreCase(lastResponse) || "3".equalsIgnoreCase(lastResponse)) {

                content = "Select Your Wallet to Debit: \n";

                accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());

                session.getServletContext().setAttribute(sessionID + "_accounts", accounts);

                if (accounts != null) {
                    if (accounts.size() > 1) {
                        content = getListOfWalletContent(content);
                        response.setContent(content);
                        response.setMsgType("1");
                    } else {
                        String request = requestString + "#" + accounts.get(0).getAccountNumber();
                        session.getServletContext().setAttribute(sessionID, request);
                        step5(request);
                    }
                }

            } else if ("2".equalsIgnoreCase(lastResponse)) {
                response.setContent(selectNetworkProvider);
                response.setMsgType("1");
            } else if ("4".equalsIgnoreCase(lastResponse)) {
                content = "Select data plan : \n";
                if ("1".equalsIgnoreCase(option) || "2".equalsIgnoreCase(option) || "3".equalsIgnoreCase(option) || "4".equalsIgnoreCase(option)) {
                    String providerName = providerNames.get(Integer.parseInt(option));
                    String providerId = providerBillerIds.get(providerName);

                    content = getListOfDataProviderContent(content, providerName, providerId);

                    response.setContent(content);
                    response.setMsgType("1");
                }
            }

        } else {
            step5(requestString);
        }
        return response;

    }

    private ServiceResponse step5(String requestString) {
        System.out.println("step5 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 6) {

            String lastResponse = requestString.split("#")[2];

            if ("1".equalsIgnoreCase(lastResponse)) {
                String[] entries = requestString.split("#");

//                accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
                accounts = (List<WalletAccount>) session.getServletContext().getAttribute(sessionID + "_accounts");

                System.out.println(" Finally=========================================" + requestString);
                String phoneNumber, amount, sourceAcctNO;
                if (accounts.size() > 1) {
                    sourceAcctNO = accounts.get(Integer.parseInt(entries[5]) - 1).getAccountNumber();
                } else {
                    sourceAcctNO = entries[5];
                }
                phoneNumber = profile.getPhoneNumber();
                amount = entries[3];
                String networkProvider = entries[4];

                if ("1".equalsIgnoreCase(networkProvider) || "2".equalsIgnoreCase(networkProvider)
                    || "3".equalsIgnoreCase(networkProvider) || "4".equalsIgnoreCase(networkProvider)) {
                    String providerName = providerNames.get(Integer.parseInt(networkProvider));
                    String providerId = providerBillerIds.get(providerName);
                    String response = buildAndBuyAirtime(phoneNumber, amount, sourceAcctNO, providerName, providerId, true);
                    session.getServletContext().removeAttribute(sessionID + "_accounts");
                    this.response.setContent(response);
                    this.response.setMsgType("2");
                }
            } else if ("2".equalsIgnoreCase(lastResponse) || "4".equalsIgnoreCase(lastResponse)) {

                String content = "Select Your Wallet to Debit: \n";

                accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());

                session.getServletContext().setAttribute(sessionID + "_accounts", accounts);

                if (accounts != null) {
                    if (accounts.size() > 1) {
                        content = getListOfWalletContent(content);
                        response.setContent(content);
                        response.setMsgType("1");
                    } else {
                        String request = requestString + "#" + accounts.get(0).getAccountNumber();
                        session.getServletContext().setAttribute(sessionID, request);
                        step6(request);
                    }
                }
            } else if ("3".equalsIgnoreCase(lastResponse)) {
                String[] entries = requestString.split("#");

//                accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
                accounts = (List<WalletAccount>) session.getServletContext().getAttribute(sessionID + "_accounts");

                System.out.println(" Finally=========================================" + requestString);

                String phoneNumber, sourceAcctNO;
                if (accounts.size() > 1) {
                    sourceAcctNO = accounts.get(Integer.parseInt(entries[5]) - 1).getAccountNumber();
                } else {
                    sourceAcctNO = entries[5];
                }
                phoneNumber = profile.getPhoneNumber();
                String networkProvider = entries[3];
                String dataPlanNumber = entries[4];

                String response = buildAndBuyDataUssd(phoneNumber, sourceAcctNO, networkProvider, dataPlanNumber, true);
                this.response.setContent(response);
                this.response.setMsgType("2");
            }

        } else {
            step6(requestString);
        }
        return response;
    }

    private ServiceResponse step6(String requestString) {
        System.out.println("step6 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 7) {
            String content = "Enter Your Pin";
            response.setContent(content);
            response.setMsgType("1");
        } else {
            step7(requestString);
        }
        return response;
    }


    private ServiceResponse step7(String requestString) {
        System.out.println("The final \nstep7 requestString.split(\"#\").length..." + requestString.split("#").length);

        if (requestString.split("#").length == 8) {
            String lastResponse = requestString.split("#")[2];

            String[] entries = requestString.split("#");

            String pin = entries[7];
            if (!passwordEncoder.matches(pin, profile.getPin())) {
                response.setContent("Invalid Pin");
                requestString = requestString.replace("#" + pin, "");
                System.out.println(" The requestString after failed pin entered===" + requestString);
                session.getServletContext().setAttribute(sessionID, requestString);
            } else if ("2".equalsIgnoreCase(lastResponse)) {
//                accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
                accounts = (List<WalletAccount>) session.getServletContext().getAttribute(sessionID + "_accounts");

                System.out.println(" Finally=========================================" + requestString);

                String phoneNumber, amount, sourceAcctNO;
                if (accounts.size() > 1) {
                    sourceAcctNO = accounts.get(Integer.parseInt(entries[6]) - 1).getAccountNumber();
                } else {
                    sourceAcctNO = entries[6];
                }
                phoneNumber = entries[4];
                amount = entries[3];
                String networkProvider = entries[5];

                if ("1".equalsIgnoreCase(networkProvider) || "2".equalsIgnoreCase(networkProvider)
                    || "3".equalsIgnoreCase(networkProvider) || "4".equalsIgnoreCase(networkProvider)) {
                    String providerName = providerNames.get(Integer.parseInt(networkProvider));
                    String providerId = providerBillerIds.get(providerName);
                    String response = buildAndBuyAirtime(phoneNumber, amount, sourceAcctNO, providerName, providerId, false);
                    this.response.setContent(response);
                    this.response.setMsgType("2");
                }


            } else if ("4".equalsIgnoreCase(lastResponse)) {
//                accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
                accounts = (List<WalletAccount>) session.getServletContext().getAttribute(sessionID + "_accounts");

                System.out.println(" Finally=========================================" + requestString);

                String phoneNumber, sourceAcctNO;
                if (accounts.size() > 1) {
                    sourceAcctNO = accounts.get(Integer.parseInt(entries[6]) - 1).getAccountNumber();
                } else {
                    sourceAcctNO = entries[6];
                }
                phoneNumber = entries[3];
                String networkProvider = entries[4];
                String dataPlanNumber = entries[5];

                String response = buildAndBuyDataUssd(phoneNumber, sourceAcctNO, networkProvider, dataPlanNumber, false);
                this.response.setContent(response);
                this.response.setMsgType("2");
            }
        }
        return response;
    }

    public ServiceResponse getResponse() {
        return response;
    }

    private String getListOfWalletContent(String content) {
        int count = 1;
        StringBuilder contentBuilder = new StringBuilder(content);
        for (WalletAccount walletAccount : accounts) {
            contentBuilder.append(" ")
                .append(count).append(". ")
                .append(walletAccount.getAccountNumber())
                .append(" (")
                .append(utility.formatMoney(Double.valueOf(walletAccount.getActualBalance())).replaceFirst("NGN", "N"))
                .append(")\n");
            count = count + 1;
        }
        content = contentBuilder.toString();
        return content;
    }

    private String getListOfDataProviderContent(String content, String providerName, String providerId) {
        List<BillerPlatform> dataPlans = billerService.getDataPlans(providerId);

        session.getServletContext().setAttribute(sessionID + "_dataPlans", dataPlans);

        if (dataPlans != null) {

            int dataPlanSize = dataPlans.size();
            if ("GLO".equalsIgnoreCase(providerName) && dataPlanSize > 2) {
                dataPlans.remove(0);
                dataPlans.remove(1);
            } else if ("MTN".equalsIgnoreCase(providerName) && dataPlanSize > 2) {
                dataPlans.remove(0);
            } else if ("9-Mobile".equalsIgnoreCase(providerName) && dataPlanSize > 2) {
                dataPlans.remove(dataPlanSize - 1);
            }

            int count = 1;
            StringBuilder contentBuilder = new StringBuilder(content);
            for (BillerPlatform dataPlan : dataPlans) {
                contentBuilder.append(" ").append(count).append(". ").append(dataPlan.getBillerPlatform()).append("(N").append(dataPlan.getFixedAmount()).append(") \n");
                count = count + 1;
            }
            content = contentBuilder.toString();
        }
        return content;
    }

    private String buyAirtimeUssd(BuyAirtimeDTO buyAirtimeDTO, boolean isSelf) {
        GenericResponseDTO genericResponseDTO = billerTransactionService.buyAirtime(buyAirtimeDTO);
        if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
            if (isSelf) {
                return "Your " + utility.formatMoney(buyAirtimeDTO.getAmount()) + "  Airtime purchase is successful. Thank You";
            } else {
                return "Your " + utility.formatMoney(buyAirtimeDTO.getAmount()) + "  Airtime purchase for "
                    + buyAirtimeDTO.getPhoneNumber() + " is successful. Thank you";
            }
        }

        return "airtime purchase failed!";
    }

    private String buyDataUssd(BuyDataDTO buyDataDTO, boolean isSelf) {
        GenericResponseDTO genericResponseDTO = billerTransactionService.buyData(buyDataDTO);

        if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
            if (isSelf) {
                return "Your " + utility.formatMoney(buyDataDTO.getAmount()) + "  Data purchase is successful. Thank You";
            } else {
                return "Your " + utility.formatMoney(buyDataDTO.getAmount()) + "  Data purchase for "
                    + buyDataDTO.getPhoneNumber() + " is successful. Thank You";
            }
        }

        return "Data purchase failed!";
    }

    private BuyAirtimeDTO buildBuyAirtimeDTO(String serviceTypeId, String phoneNumber, double amount, String sourceAccountnNo) {
        BuyAirtimeDTO buyAirtimeDTO = new BuyAirtimeDTO();
        buyAirtimeDTO.setServiceId(serviceTypeId);
        buyAirtimeDTO.setPhoneNumber(phoneNumber);
        buyAirtimeDTO.setAmount(amount);
        buyAirtimeDTO.setNarration("USSD/Airtime purchase");
        buyAirtimeDTO.setSourceAccountNo(sourceAccountnNo);
        buyAirtimeDTO.setTransRef(utility.getUniqueTransRef());
        return buyAirtimeDTO;
    }

    private BuyDataDTO buildBuyDataDTO(String dataPlanId, String phoneNumber, double amount, String sourceAccountnNo) {
        BuyDataDTO buyDataDTO = new BuyDataDTO();
        buyDataDTO.setAmount(amount);
        buyDataDTO.setDataPlanId(dataPlanId);
        buyDataDTO.setNarration("USSD/Data purchase");
        buyDataDTO.setPhoneNumber(phoneNumber);
        buyDataDTO.setSourceAccountNo(sourceAccountnNo);
        buyDataDTO.setTransRef(utility.getUniqueTransRef());
        return buyDataDTO;
    }

    private String buildAndBuyAirtime(String phoneNumber, String amount, String sourceAcctNO, String providerName, String providerId, boolean isSelf) {
        session.getServletContext().removeAttribute(sessionID + "_accounts");
        String serviceTypeId;
        if ("AIRTEL".equalsIgnoreCase(providerName)) {
            providerId = providerBillerIds.get("AIRTIME RECHARGE MOBIFIN");
        }
        List<BillerPlatform> billerPlatforms = billerService.getDataPlans(providerId);
        BillerPlatform billerPlatform = null;
        if (billerPlatforms != null && !billerPlatforms.isEmpty()) {
            if ("GLO".equalsIgnoreCase(providerName) || "MTN".equalsIgnoreCase(providerName) || "AIRTEL".equalsIgnoreCase(providerName)) {
                billerPlatform = billerPlatforms.get(0);
            } else if ("9-Mobile".equalsIgnoreCase(providerName)) {
                billerPlatform = billerPlatforms.get(billerPlatforms.size() - 1);
            }
        }
        if (billerPlatform != null) {
            serviceTypeId = String.valueOf(billerPlatform.getBillerplatformID());
            BuyAirtimeDTO buyAirtimeDTO = buildBuyAirtimeDTO(serviceTypeId, phoneNumber, Double.parseDouble(amount), sourceAcctNO);
            return buyAirtimeUssd(buyAirtimeDTO, isSelf);
        }

        return "failed!";
    }

    private String buildAndBuyDataUssd(String phoneNumber, String sourceAcctNO, String networkProvider, String dataPlanNumber, boolean isSelf) {
        String dataPlanId;

        session.getServletContext().removeAttribute(sessionID + "_accounts");

        if ("1".equalsIgnoreCase(networkProvider) || "2".equalsIgnoreCase(networkProvider) ||
            "3".equalsIgnoreCase(networkProvider) || "4".equalsIgnoreCase(networkProvider)) {
            String providerName = providerNames.get(Integer.parseInt(networkProvider));
            String providerId = providerBillerIds.get(providerName);

//            List<BillerPlatform> dataPlans = billerService.getDataPlans(providerId);
            List<BillerPlatform> dataPlans = (List<BillerPlatform>) session.getServletContext().getAttribute(sessionID + "_dataPlans");

            if (dataPlans != null) {

                int dataPlanSize = dataPlans.size();
                if ("GLO".equalsIgnoreCase(providerName) && dataPlanSize > 2) {
                    dataPlans.remove(0);
                    dataPlans.remove(1);
                } else if ("MTN".equalsIgnoreCase(providerName) && dataPlanSize > 2) {
                    dataPlans.remove(0);
                } else if ("9-Mobile".equalsIgnoreCase(providerName) && dataPlanSize > 2) {
                    dataPlans.remove(dataPlanSize - 1);
                }
                BillerPlatform billerPlatform = dataPlans.get(Integer.parseInt(dataPlanNumber) - 1);
                dataPlanId = String.valueOf(billerPlatform.getBillerplatformID());
                double amount = billerPlatform.getFixedAmount();

                BuyDataDTO buyDataDTO = buildBuyDataDTO(dataPlanId, phoneNumber, amount, sourceAcctNO);

                session.getServletContext().removeAttribute(sessionID + "_dataPlans");

                return buyDataUssd(buyDataDTO, isSelf);
            }
        }

        return "failed!";
    }

    private String getNetworkId() {
        String networkId = null;

        if (networkProvider.toLowerCase().contains("mtn")) {
            networkId = "1";
        } else if (networkProvider.toLowerCase().contains("airtel")) {
            networkId = "2";
        } else if (networkProvider.toLowerCase().contains("mobile")) {
            networkId = "3";
        } else if (networkProvider.toLowerCase().contains("glo")) {
            networkId = "4";
        }
        return networkId;
    }

}
