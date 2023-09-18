package ng.com.justjava.corebanking.service.ussd;

import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.service.BankService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.BankDTO;
import ng.com.justjava.corebanking.service.dto.FundDTO;
import ng.com.justjava.corebanking.service.dto.PaymentResponseDTO;
import ng.com.justjava.corebanking.util.Utility;
import ng.com.systemspecs.remitarits.accountenquiry.AccountEnquiryResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;
import java.util.List;

public class SendMoneyToBank {

    private final ServiceResponse response = new ServiceResponse();
    private final BankService bankService;
    private final WalletAccountService walletAccountService;
    private List<WalletAccount> accounts;
    private final Profile profile;
    private final String sessionID;
    private final HttpSession session;
    private final PasswordEncoder passwordEncoder;
    private final Utility utility;
    private String msgType;

    public SendMoneyToBank(String requestString, Profile profile, BankService bankService,
                           WalletAccountService walletAccountService, HttpSession session, String sessionID,
                           PasswordEncoder passwordEncoder, Utility utility) {
        this.bankService = bankService;
        this.profile = profile;
        this.walletAccountService = walletAccountService;
        this.session = session;
        this.sessionID = sessionID;
        this.passwordEncoder = passwordEncoder;
		this.utility = utility;
		step1(requestString);
	}

	private ServiceResponse step1(String requestString) {

		System.out.println("Step 1==" + requestString);
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
			response.setContent("Enter Bank Account Number");
			response.setMsgType("1");
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
            String lastResponse = requestString.split("#")[3];
            String pageNumber = lastResponse.substring(10);
            int pgNo = pageNumber.length();
            System.out.println(" lastResponse====" + lastResponse + " pages=====" + pgNo);
            StringBuilder content = new StringBuilder("Select Bank: \n");
            Pageable page = PageRequest.of(0, 40);
            String accountNumber = StringUtils.substring(lastResponse, 0, 10);
            List<BankDTO> banks = bankService.findAllRegularBanks(page).toList();

            List<BankDTO> validBanks = bankService.getValidBanks(banks, accountNumber);
            session.getServletContext().setAttribute("validBanks", validBanks);

            int count = pgNo * 4 + 1;

            for (int i = count, validBanksSize = validBanks.size(); i < count + 4 && i <= validBanksSize; i++) {
                BankDTO bank = validBanks.get(i - 1);
                content.append(" ").append(i).append(". ").append(bank.getBankName()).append("\n");
            }

            if (pgNo != 0) {
                content.append("98. Back\n");
            }

            if (pgNo != (validBanks.size() / 4) - 1 && validBanks.size() > 4) {
                content.append("99. Next\n");
            }
            response.setContent(content.toString());
            msgType = "1";
        } else {
			step4(requestString);
		}
		return response;
	}

	private ServiceResponse step4(String requestString) {
		System.out.println("step4 requestString.split(\"#\").length..." + requestString.split("#").length);

        if (requestString.split("#").length == 5) {

            accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
            session.getServletContext().setAttribute(sessionID + "_accounts", accounts);

            StringBuilder content = new StringBuilder();

            if (accounts != null) {

                String accountNumber = StringUtils.substring(requestString.split("#")[3], 0, 10);
                Integer bankSelectedIndex = Integer.parseInt(requestString.split("#")[4]) - 1;

                List<BankDTO> validBanks = (List<BankDTO>) session.getServletContext().getAttribute("validBanks");

                BankDTO selectedBank = validBanks.get(bankSelectedIndex);
                //List<BankDTO> selectedBank = bankService.findAllRegularBanks(PageRequest.of(bankSelectedIndex, 1)).toList();

                String bankCode = (selectedBank.getBankCode());
                System.out.println(" Value for Account Verification bankCode==" + bankCode + " accountNumber==" + accountNumber);

                AccountEnquiryResponse bankVerification = bankService.verifyBankAccount(bankCode, accountNumber);
				if(bankVerification.getStatus().equalsIgnoreCase("failed")) {
					content = new StringBuilder(bankVerification.getData().getResponseDescription());
					String first = "#"+requestString.split("#")[3];
					String second = "#"+requestString.split("#")[4];
					requestString = requestString.replace(first, "");
					requestString = requestString.replace(second, "");
					session.getServletContext().setAttribute(sessionID, requestString);
					response.setContent(content.toString());
					msgType = "2";
					response.setMsgType(msgType);
					return response;

                }else if(bankVerification.getData()!=null) {

					content = new StringBuilder("\"Account Successfully Verified "+
							bankVerification.getData().getAccountName() + "\n");

					session.getServletContext().setAttribute(sessionID+"_beneficiaryName", bankVerification.getData().getAccountName());
				}

				if (accounts.size() > 1) {
					content = content.append("Select Your Wallet to Debit: \n");
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
					msgType = "1";

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
			String content="Enter Your Pin";
			response.setContent(content);
			response.setMsgType("1");
		} else {
			step6(requestString);
		}
		return response;
	}

	private ServiceResponse step6(String requestString) {
        System.out.println(" The final \nstep6 requestString.split(\"#\").length..." + requestString.split("#").length);

        if (requestString.split("#").length == 7) {
            String[] entries = requestString.split("#");

            String pin = entries[6];
            if (!passwordEncoder.matches(pin, profile.getPin())) {

                response.setContent("Invalid Pin");
                requestString = requestString.replace("#" + pin, "");
                System.out.println(" The requestString after failed pin entered===" + requestString);
                session.getServletContext().setAttribute(sessionID, requestString);

            } else {
                accounts = (List<WalletAccount>) session.getServletContext().getAttribute(sessionID + "_accounts");

                System.out.println(" Finally=========================================" + requestString);
                int bankSelectedIndex = Integer.parseInt(entries[4]) - 1;

                List<BankDTO> validBanks = (List<BankDTO>) session.getServletContext().getAttribute("validBanks");
                BankDTO selectedBank = validBanks.get(bankSelectedIndex);

                FundDTO fundDTO = new FundDTO();
                String accountNumber = entries[3];
                accountNumber = StringUtils.substring(accountNumber, 0, 10);//accountNumber.substring(accountNumber.length() - 9);
                fundDTO.setAccountNumber(accountNumber);
                Double amount = Double.valueOf(entries[2]);
                fundDTO.setAmount(amount);
                String beneficiaryName = String.valueOf(session.getServletContext().getAttribute(sessionID + "_beneficiaryName"));
                fundDTO.setBeneficiaryName(beneficiaryName);
                session.getServletContext().removeAttribute(sessionID + "_beneficiaryName");
                fundDTO.setChannel("wallettobank");
                fundDTO.setDestBankCode(selectedBank.getBankCode());
                fundDTO.setNarration("USSD/Payment");
                fundDTO.setPin(pin);
                fundDTO.setTransRef(utility.getUniqueTransRef());

                session.getServletContext().removeAttribute("validBanks");

                String sourceAcctNO;
                if (accounts.size() > 1) {
                    sourceAcctNO = accounts.get(Integer.parseInt(entries[5]) - 1).getAccountNumber();
                } else {
                    sourceAcctNO = entries[5];
                }

                fundDTO.setSourceAccountNumber(sourceAcctNO);
                System.out.println(" Final FundDTO constructed==" + fundDTO.toString());

                PaymentResponseDTO responseDTO = walletAccountService.sendMoney(fundDTO);

                if (responseDTO.getError()) {
                    response.setContent(responseDTO.getMessage());
                } else {
                    String content = "Your transfer of " + utility.formatMoney(amount) + " to \"" + beneficiaryName + " - " + accountNumber + "\" is successful. Thank You.";
                    response.setContent(content);
                }

                session.getServletContext().removeAttribute(sessionID + "_accounts");

                response.setMsgType("2");

            }
		}
		return response;
	}

	public ServiceResponse getResponse() {
		return response;
	}
}
