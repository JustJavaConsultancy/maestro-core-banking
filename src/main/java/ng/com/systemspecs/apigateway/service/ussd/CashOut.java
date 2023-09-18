package ng.com.systemspecs.apigateway.service.ussd;

import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.WalletAccount;
import ng.com.systemspecs.apigateway.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.dto.FundDTO;
import ng.com.systemspecs.apigateway.service.dto.PaymentResponseDTO;
import ng.com.systemspecs.apigateway.util.Utility;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;
import java.util.List;

public class CashOut {
    private final WalletAccountService walletAccountService;
    private final Profile profile;
    private final HttpSession session;
    private final PasswordEncoder passwordEncoder;
    private final Utility utility;
    private final String sessionID;
    private ServiceResponse response;

    public CashOut(String requestString,
                   WalletAccountService walletAccountService, Profile profile,
                   HttpSession session, PasswordEncoder passwordEncoder, String sessionID, Utility utility) {
        this.walletAccountService = walletAccountService;
        this.profile = profile;
        this.session = session;
        this.passwordEncoder = passwordEncoder;
        this.sessionID = sessionID;
        this.utility = utility;

        step1(requestString);
    }

    private ServiceResponse step1(String requestString) {

        System.out.println("Step 1== " + requestString);

        response = new ServiceResponse();
        if (requestString.split("#").length == 2) {
            String ussdContent = "Select service type : \n" +
                "1. At Agent Location\r\n";
//                + "2. At ATM\r\n" ;

            response.setContent(ussdContent);
            response.setMsgType("1");
        } else {
            step2(requestString);
        }

        return response;
    }

    private ServiceResponse step2(String requestString) {

        System.out.println("Step 2 ==" + requestString);

        if (requestString.split("#").length == 3) {
            response.setContent("Enter Amount");
            response.setMsgType("1");
        } else {
            step3(requestString);
        }

        return response;
    }

    private ServiceResponse step3(String requestString) {
        System.out.println("Step 3==" + requestString);
        if (requestString.split("#").length == 4) {

            try {
                Double.parseDouble(requestString.split("#")[3]);
            } catch (Exception e) {
                e.printStackTrace();
                response.setContent("Invalid amount entered!");
                response.setMsgType("2");
            }

            String option = requestString.split("#")[2];

            if ("1".equalsIgnoreCase(option)) {
                response.setContent("Enter Agent ID");
                response.setMsgType("1");
            }
        } else {
            step4(requestString);
        }
        return response;
    }

    private ServiceResponse step4(String requestString) {
        // *7000#2#500#1
        System.out.println("step4 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 5) {

            String option = requestString.split("#")[2];
            StringBuilder content = new StringBuilder();

            if ("1".equalsIgnoreCase(option)) {

                String agentId = requestString.split("#")[4];

                List<WalletAccount> walletAccounts = walletAccountService.findByAccountOwnerPhoneNumber(utility.formatPhoneNumber(agentId));

                if (walletAccounts == null || walletAccounts.isEmpty()) {
                    response.setContent("Wallet Not Exist");
                    response.setMsgType("2");
                    return response;
                } else {
                    WalletAccount destinationAccount = walletAccounts.get(0); //todo check

                    session.getServletContext().setAttribute(sessionID + "_destinationAccount", destinationAccount);

                    if (destinationAccount.getAccountOwner() != null) {
                        System.out.println(" The wallet sent for send money to wallet===="
                            + "" + agentId + "  destinationAccount===" + destinationAccount);

                        content = new StringBuilder("Account Verified Successfully-" +
                            (destinationAccount.getAccountOwner() != null ? destinationAccount.getAccountOwner().getFullName() :
                                destinationAccount.getAccountName()) + "\n\n");
                    }
                }
            }

            List<WalletAccount> accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
            session.getServletContext().setAttribute(sessionID + "_accounts", accounts);
            if (accounts != null) {
                if (accounts.size() > 1) {
                    content.append("Select Your Wallet to Debit: \n");
                    int count = 1;
                    for (WalletAccount walletAccount : accounts) {
                        content.append(" ")
                            .append(count).append(". ")
                            .append(walletAccount.getAccountNumber())
                            .append(" (")
                            .append(utility.formatMoney(Double.valueOf(walletAccount.getActualBalance())).replaceFirst("NGN", "N"))
                            .append(")\n");
                        count = count + 1;
                    }
                    response.setContent(content.toString());
                } else {
                    String request = requestString + "#" + accounts.get(0).getAccountNumber();
                    session.getServletContext().setAttribute(sessionID, request);
                    step5(request);
                }
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
            double amount = Double.parseDouble(requestString.split("#")[3]);

            String content = "You are about to withdraw " + utility.formatMoney(amount) + "\n";
            response.setContent(content + "Enter Pin to Confirm");
            response.setMsgType("1");
        } else {
            step6(requestString);
        }
        return response;
    }

    private ServiceResponse step6(String requestString) {
        System.out.println("step6 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 7) {
            String[] entries = requestString.split("#");

            String pin = entries[6];
            if (!passwordEncoder.matches(pin, profile.getPin())) {
                response.setContent("Invalid Pin, Please try again");
                requestString = requestString.replace("#" + pin, "");
                System.out.println(" The requestString after failed pin entered===" + requestString);
                session.getServletContext().setAttribute(sessionID, requestString);
            } else {
//                List<WalletAccount> accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
                List<WalletAccount> accounts = (List<WalletAccount>) session.getServletContext().getAttribute(sessionID + "_accounts");
                System.out.println(" Finally=========================================" + requestString);
//				Integer bankSelectedIndex = Integer.parseInt(entries[4])-1;
                //List<BankDTO> selectedBank = bankService.findAllRegularBanks(PageRequest.of(bankSelectedIndex, 1)).toList();
                FundDTO fundDTO = new FundDTO();

                String option = requestString.split("#")[2];

                if ("1".equalsIgnoreCase(option)) {
                    WalletAccount destinationAccount = (WalletAccount) session.getServletContext().getAttribute(sessionID + "_destinationAccount");

                    fundDTO.setAccountNumber(destinationAccount.getAccountNumber());
                    fundDTO.setChannel("wallettowallet");
                    String beneficiary = destinationAccount.getAccountOwner() != null ? destinationAccount.getAccountOwner().getFullName() : destinationAccount.getAccountName();
                    fundDTO.setBeneficiaryName(beneficiary);
                    fundDTO.setDestBankCode("ABC");
                    fundDTO.setNarration("USSD/Cashout at agent");

                } else if ("2".equalsIgnoreCase(option)) {
                    WalletAccount destinationAccount = (WalletAccount) session.getServletContext().getAttribute(sessionID + "_destinationAccount");

                    fundDTO.setAccountNumber(destinationAccount.getAccountNumber());
                    fundDTO.setChannel("walletToBank");
                    String beneficiary = destinationAccount.getAccountOwner() != null ? destinationAccount.getAccountOwner().getFullName() : destinationAccount.getAccountName();
                    fundDTO.setBeneficiaryName(beneficiary);
                    fundDTO.setDestBankCode("ABC");
                    fundDTO.setNarration("USSD/Cashout at ATM");

                }
                Double amount = Double.valueOf(entries[3]);
                fundDTO.setAmount(amount);
                fundDTO.setPin(pin);
                fundDTO.setTransRef(utility.getUniqueTransRef());

                String sourceAcctNO;
                if (accounts.size() > 1) {
                    sourceAcctNO = accounts.get(Integer.valueOf(entries[5]) - 1).getAccountNumber();
                } else {
                    sourceAcctNO = entries[5];
                }
                fundDTO.setSourceAccountNumber(sourceAcctNO);

                System.out.println(" My FundDTO constructed==" + fundDTO.toString());

                PaymentResponseDTO responseDTO = walletAccountService.sendMoney(fundDTO);

                if (!responseDTO.getError()) {
                    response.setContent("Please collect your cash from the Agent. Thank You.");
                } else {
                    response.setContent("Transfer failed");
                }
                response.setMsgType("2");
            }
        }
        session.getServletContext().removeAttribute(sessionID + "_accounts");
        session.getServletContext().removeAttribute(sessionID + "_destinationAccount");

        return response;
    }

    public ServiceResponse getResponse() {
        return response;
    }
}
