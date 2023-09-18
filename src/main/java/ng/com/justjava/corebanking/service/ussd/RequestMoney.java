package ng.com.justjava.corebanking.service.ussd;

import io.jsonwebtoken.lang.Collections;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.FundDTO;
import ng.com.justjava.corebanking.service.dto.PaymentResponseDTO;
import ng.com.justjava.corebanking.util.Utility;

import java.util.List;

public class RequestMoney {

    private ServiceResponse response;
    private final WalletAccountService walletAccountService;
    private final Profile profile;
    private final Utility utility;

    public RequestMoney(String requestString, WalletAccountService walletAccountService, Profile profile, Utility utility) {
        this.walletAccountService = walletAccountService;
        this.profile = profile;
        this.utility = utility;
        step1(requestString);
    }

    private ServiceResponse step1(String requestString) {

		System.out.println("Step 1==" + requestString);
		response = new ServiceResponse();
		if (requestString.split("#").length == 2) {
			response.setContent("Enter Wallet ID");
			response.setMsgType("1");
		} else {
			step2(requestString);
		}

		return response;
	}

	private ServiceResponse step2(String requestString) {
		System.out.println("step2 requestString.split(\"#\").length..."+requestString.split("#").length);

		response = new ServiceResponse();
		if (requestString.split("#").length == 3) {
			WalletAccount account=walletAccountService.findOneByAccountNumber(requestString.split("#")[2]);

            if(account==null) {
				response.setContent("Unknown Account Entered");
				response.setMsgType("2");
			}
			String content = "Wallet Verified Successfully -"+account.getAccountOwner().getFullName()+"\n\n"
					+ " Enter Amount \n";
			response.setContent(content);
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
            String requesterWallet = "";
            List<WalletAccount> accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
            if (!Collections.isEmpty(accounts)) {
                requesterWallet = accounts.get(0).getAccountNumber();
            }
            String[] entries = requestString.split("#");

            FundDTO requestMoneyDTO = new FundDTO();
            requestMoneyDTO.setAccountNumber(requesterWallet);
            Double amount = Double.valueOf(entries[3]);
            requestMoneyDTO.setAmount(amount);
            requestMoneyDTO.setBeneficiaryName(profile.getFullName());
            requestMoneyDTO.setChannel("INVOICE");
            requestMoneyDTO.setDestBankCode("ABC");
            requestMoneyDTO.setNarration("N " + entries[3] + " Requested from " + entries[2] + " by " + profile.getFullName());
            requestMoneyDTO.setPhoneNumber(null);
            requestMoneyDTO.setSourceAccountNumber(entries[2]);
            requestMoneyDTO.setSourceBankCode("ABC");
            requestMoneyDTO.setSpecificChannel("USSD");
            requestMoneyDTO.setTransRef(utility.getUniqueTransRef());
            PaymentResponseDTO paymentResponse = walletAccountService.requestMoney(requestMoneyDTO);

			System.out.println(" The Response here ==="+paymentResponse.toString());
            if (paymentResponse.getError()) {
                response.setContent(paymentResponse.getMessage());
            } else if (paymentResponse.getCode().equalsIgnoreCase("00")) {
                response.setContent("Your request for " + utility.formatMoney(amount) + " has been sent");
            } else {
                response.setContent("Request Money Failed");
            }
			response.setMsgType("2");
		}
		return response;
	}

	public ServiceResponse getResponse() {
		return response;
	}
}
