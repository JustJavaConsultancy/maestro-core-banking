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

public class SendMoneyToWallet {
    private ServiceResponse response;
    private final WalletAccountService walletAccountService;
    private final Profile profile;
    private final HttpSession session;
    private final PasswordEncoder passwordEncoder;
    private final Utility utility;
    private final String sessionID;

    public SendMoneyToWallet(String requestString,
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

		System.out.println("Step 1==" + requestString);
		response = new ServiceResponse();
		if (requestString.split("#").length == 2) {
			response.setContent("Enter Amount");
			response.setMsgType("1");
		} else {
			step2(requestString);
		}

		return response;
	}

	private ServiceResponse step2(String requestString) {
		System.out.println("Step 2==" + requestString);
		if (requestString.split("#").length == 3) {
			response.setContent("Enter Beneficiary Wallet Number");
			response.setMsgType("1");
		} else {
			step3(requestString);
		}
		return response;
	}

	private ServiceResponse step3(String requestString) {
		// *7000#2#500#1
		System.out.println("step3 requestString.split(\"#\").length..."+requestString.split("#").length);
		if (requestString.split("#").length == 4) {
            String accountNumber = requestString.split("#")[3];

            WalletAccount account = walletAccountService.findOneByAccountNumber(accountNumber); //todo check

            System.out.println(" The wallet sent for send money to wallet===="
                + "" + accountNumber + "  account===" + account);
            StringBuilder content = new StringBuilder();
            if (account == null) {
                response.setContent("Wallet Not Exist");
                response.setMsgType("2");
                return response;
            } else {
                content = new StringBuilder("Account Verified Successfully-" +
                    (account.getAccountOwner() != null ? account.getAccountOwner().getFullName() :
                        account.getAccountName()) + "\n\n");
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
                    step4(request);
                }
			}
		}else {
			step4(requestString);
		}
		return response;
	}
	private ServiceResponse step4(String requestString) {
        // *7000#2#500#1
        System.out.println("step4 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 5) {
            response.setContent("Enter Your Pin");
            response.setMsgType("1");
        } else {
            step5(requestString);
        }
        return response;
    }
	private ServiceResponse step5(String requestString) {
		System.out.println("step4 requestString.split(\"#\").length..."+requestString.split("#").length);
		if (requestString.split("#").length == 6) {
            String[] entries = requestString.split("#");

            String pin = entries[5];
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
                String accountNumber = entries[3];
                fundDTO.setAccountNumber(accountNumber);
                Double amount = Double.valueOf(entries[2]);
                fundDTO.setAmount(amount);
                fundDTO.setBeneficiaryName("Akinyemi");
                fundDTO.setChannel("wallettowallet");
                fundDTO.setDestBankCode("ABC");
				fundDTO.setNarration("USSD/Payment");
				fundDTO.setPin(pin);
				fundDTO.setTransRef(utility.getUniqueTransRef());

                String sourceAcctNO;
                if (accounts.size() > 1) {
                    sourceAcctNO = accounts.get(Integer.valueOf(entries[4]) - 1).getAccountNumber();
                } else {
                    sourceAcctNO = entries[4];
                }
                fundDTO.setSourceAccountNumber(sourceAcctNO);
                WalletAccount destinationAccount = walletAccountService.findOneByAccountNumber(accountNumber);
                PaymentResponseDTO responseDTO = walletAccountService.sendMoney(fundDTO);
                System.out.println(" My FundDTO constructed==" + fundDTO.toString());
                if (!responseDTO.getError()) {
                    Profile accountOwner = destinationAccount.getAccountOwner();
                    if(accountOwner != null) {
                        response.setContent("Your transfer of " + utility.formatMoney(amount) + " to \" "
                            + accountOwner.getFullName() + " - " + accountNumber + "\" is successful. Thank You");
                    } else {
                        response.setContent("Your transfer of " + utility.formatMoney(amount) + " to \" "
                            + destinationAccount.getAccountName() + " - " + accountNumber + "\" is successful. Thank You");
                    }
                } else {
                    response.setContent("Transfer failed");
                }
                response.setMsgType("2");
            }
        }
        session.getServletContext().removeAttribute(sessionID + "_accounts");

        return response;
    }
	public ServiceResponse getResponse() {
		return response;
	}
}
