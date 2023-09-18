package ng.com.systemspecs.apigateway.service.ussd;

import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.WalletAccount;
import ng.com.systemspecs.apigateway.service.BankService;
import ng.com.systemspecs.apigateway.service.CoralPayService;
import ng.com.systemspecs.apigateway.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.systemspecs.apigateway.util.Utility;
import ng.com.systemspecs.remitarits.accountenquiry.AccountEnquiryResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpSession;
import java.util.List;

public class FundWallet {

    private final Profile profile;
    private final HttpSession session;
    private final WalletAccountService walletAccountService;
    private final Utility utility;
    private final String sessionID;
    private final BankService bankService;
    private final CoralPayService coralPayService;
    private List<WalletAccount> accounts;
    private final StringBuilder fundWallet = new StringBuilder("You are about to fund your wallet");
    private ServiceResponse response;

    public FundWallet(String requestString, Profile profile, HttpSession session, WalletAccountService walletAccountService,
                      String sessionID, Utility utility, BankService bankService, CoralPayService coralPayService) {
        this.profile = profile;
        this.session = session;
        this.walletAccountService = walletAccountService;
        this.sessionID = sessionID;
        this.utility = utility;
        this.bankService = bankService;
        this.coralPayService = coralPayService;
        firstStep(requestString);
    }

    private ServiceResponse firstStep(String requestString) {

        System.out.println("Step 1==" + requestString.split("#").length + requestString);
        response = new ServiceResponse();
        if (requestString.split("#").length == 2) {
            String ussdContent = "Select Channel : \n" +
            	"1. Bank USSD\r\n" +
                "2. Other Funding Channels\r\n";

            response.setContent(ussdContent);
            response.setMsgType("1");
        } else {
        	secondStepUserOption(requestString);
        }

        return response;
    }

    private ServiceResponse secondStepUserOption(String requestString) {
    	response = new ServiceResponse();
    	if(requestString.split("#").length == 3) {
    		 String[] userOption = requestString.split("#");
             String option = userOption[2];
             System.out.println("option ==> " + option);
             if ("2".equalsIgnoreCase(option.substring(0, 1))) {
            	 step1(requestString);
             }else if ("1".equalsIgnoreCase(option.substring(0, 1))) {
            	 step1USSD(requestString);
             }
    	}else {
   		 String[] userOption = requestString.split("#");
         String option = userOption[2];
         if ("2".equalsIgnoreCase(option.substring(0, 1))) {
        	 step2(requestString);
         }else if ("1".equalsIgnoreCase(option.substring(0, 1))) {
        	 step1USSD(requestString);
         }

        }
    	return response;
    }

    private ServiceResponse step1USSD(String requestString) {
        System.out.println("Step 1 ussd==" + requestString);
        System.out.println("The length is ussd " + requestString.split("#").length);
        response = new ServiceResponse();
        if (requestString.split("#").length == 3) {
            response.setContent(fundWallet + "\r\nEnter Amount"); //Amount is entries[3]
            response.setMsgType("1");
        } else {
            System.out.println("going to step2ussd of fund wallet requestString.split(\"#\").length..." + requestString.split("#").length + requestString);
            step2USSD(requestString);
        }

        return response;
    }

    private ServiceResponse step2USSD(String requestString) {
        System.out.println("step2ussd2 of fund wallet requestString.split(\"#\").length..." + requestString.split("#").length + requestString);
        if (requestString.split("#").length == 4) {

            List<WalletAccount> accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());

            session.getServletContext().setAttribute(sessionID + "_accounts", accounts);

            if (accounts != null) {
                String content = "";
                String[] entries = requestString.split("#");
                String Amount = entries[3];
                System.out.println(" The Amount as Supplied ===" + Amount);
                if (accounts.size() > 1) {
                    content = "Select Wallet to fund: \n"; //sourceAcctNo is entries[4]
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
                    response.setContent(content);
                    // String msgType = "1";
                } else {
                    String request = requestString + "#" + accounts.get(0).getAccountNumber();
                    session.getServletContext().setAttribute(sessionID, request);
                    System.out.println(" I am formally running the else now..." + request);
                    step3USSD(request);
                }
            }
        } else {

            step3USSD(requestString);
        }
        return response;
    }

    private ServiceResponse step3USSD(String requestString) {
        System.out.println("Step 2==" + requestString);
        if (requestString.split("#").length == 5) {
            response.setContent("Enter Bank Account Number"); //Bank Acct Number is entries[5]
            response.setMsgType("1");
        } else {
            step4USSD(requestString);
        }
        return response;
    }


    private ServiceResponse step4USSD(String requestString) {
        // *7000#2#500#1
        System.out.println("step2ussd2  sendmoneytobank requestString.split(\"#\").length..."
            + requestString.split("#").length + " and the requestString itself ===" + requestString);

        if (requestString.split("#").length == 6) {
            String lastResponse = requestString.split("#")[5];
            String pageNumber = lastResponse.substring(10);
            int pgNo = pageNumber.length();
            System.out.println(" lastResponse====" + lastResponse + " pages=====" + pgNo);
            StringBuilder content = new StringBuilder("Select Bank: \n"); //Bank selected index entries[7]
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
            response.setMsgType("1");
        } else {
            step5USSD(requestString);
        }
        return response;
    }


    private ServiceResponse step5USSD(String requestString) {

        System.out.println("Step 5 ussd==" + requestString);
        System.out.println("The lenght is ussd " + requestString.split("#").length);

        if (requestString.split("#").length == 7) {

            response = new ServiceResponse();

            String[] entries = requestString.split("#");

            String amountStr = requestString.split("#")[3];
            accounts = (List<WalletAccount>) session.getServletContext().getAttribute(sessionID + "_accounts");

            String sourceAcctNO;
            if (accounts.size() > 1) {
                sourceAcctNO = accounts.get(Integer.parseInt(entries[4]) - 1).getAccountNumber();
            } else {
                sourceAcctNO = entries[4];
            }

            System.out.println("Source acc No ===> " + sourceAcctNO);
            double amount = 0;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (Exception e) {
                response.setContent("Invalid amountStr");
                response.setMsgType("2");
                return response;
            }

            WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(sourceAcctNO);
            if (walletAccount == null) {
                response.setContent("Selected Wallet Account Is Invalid ");
                response.setMsgType("2");
                return response;
            }

            StringBuilder content = new StringBuilder();

            String accountNumber = StringUtils.substring(requestString.split("#")[5], 0, 10);

            System.out.println("Bank accountNumber ==> " + accountNumber);

            Integer index = Integer.parseInt(requestString.split("#")[6]);
            System.out.println("Index ===> " + index);
            Integer bankSelectedIndex = index - 1;
            System.out.println("selectedBankIndex ==> " + bankSelectedIndex);

            List<BankDTO> validBanks = (List<BankDTO>) session.getServletContext().getAttribute("validBanks");

            BankDTO selectedBank = validBanks.get(bankSelectedIndex);
            //List<BankDTO> selectedBank = bankService.findAllRegularBanks(PageRequest.of(bankSelectedIndex, 1)).toList();

            String bankCode = (selectedBank.getBankCode());
            System.out.println(" Value for Account Verification bankCode==" + bankCode + " accountNumber==" + accountNumber);

            AccountEnquiryResponse bankVerification = bankService.verifyBankAccount(bankCode, accountNumber);
            if (bankVerification.getStatus().equalsIgnoreCase("failed")) {
                content = new StringBuilder(bankVerification.getData().getResponseDescription());
                String first = "#" + requestString.split("#")[3];
                String second = "#" + requestString.split("#")[4];
                requestString = requestString.replace(first, "");
                requestString = requestString.replace(second, "");
                session.getServletContext().setAttribute(sessionID, requestString);
                response.setContent(content.toString());
                response.setMsgType("2");
                return response;

            } else if (bankVerification.getData() != null) {

                content = new StringBuilder("\"Account Successfully Verified " +
                    bankVerification.getData().getAccountName() + "\n");


                //Todo invoke reference
                InvokeReferenceRequestDTO invokeReferenceRequestDTO = new InvokeReferenceRequestDTO();
                InvokeReferenceRequestDetailsDTO invokeReferenceRequestDetailsDTO = new InvokeReferenceRequestDetailsDTO();
                invokeReferenceRequestDetailsDTO.setTraceID(sourceAcctNO);
                invokeReferenceRequestDetailsDTO.setAmount(amount);

                RequestHeaderDTO requestHeaderDTO = new RequestHeaderDTO();
                invokeReferenceRequestDTO.setRequestDetails(invokeReferenceRequestDetailsDTO);
                invokeReferenceRequestDTO.setRequestHeader(requestHeaderDTO);

                String transactionReference = "";

                InvokeReferenceResponseDTO invokeReferenceResponseDTO = coralPayService.callInvokeReference(invokeReferenceRequestDTO);
                if (invokeReferenceResponseDTO != null) {
                    CgatePaymentNotificationResponseDTO responseHeader = invokeReferenceResponseDTO.getResponseHeader();
                    if (responseHeader != null) {
                        if ("00".equalsIgnoreCase(responseHeader.getResponseCode())) {
                            InvokeReferenceResponseDetailsDTO responseDetails = invokeReferenceResponseDTO.getResponseDetails();
                            if (responseDetails != null) {
                                transactionReference = responseDetails.getReference();
                            }
                        }
                    }
                } else {
                    response.setContent("Error generating transaction reference");
                    response.setMsgType("2");
                    return response;
                }

                session.getServletContext().removeAttribute(sessionID + "_accounts");

                String shortCode = selectedBank.getShortCode();
                if (StringUtils.isEmpty(shortCode)) {
                    response.setContent("Bank is not supported");
                    response.setMsgType("2");
                    return response;
                }

                String message = "";
                if (!StringUtils.isEmpty(transactionReference)) {
                    message = "You initiated a Fund Wallet transaction via Bank USSD\n" +
                        "Please dial the code below to continue:\n\n\n" +
                        "*" +
                        shortCode +
                        "*000*" + transactionReference + "#" +
                        "\n\n\nThis code is only valid for 5 minutes.";
                    walletAccountService.sendUSSDFundWalletLink(message, profile);

                    response.setContent(content.append("Please check your inbox to continue").toString());
                    response.setMsgType("2");
                    return response;
                }

                response.setContent("Error processing...");
                response.setMsgType("2");
                return response;
            }
        }
//        else {
//            System.out.println("going to step4 ussd of fund wallet requestString.split(\"#\").length..." + requestString.split("#").length + requestString);
//            step4USSD(requestString);
//		}

        return response;
    }
    /*private ServiceResponse step4USSD(String requestString) {
		System.out.println("step4  sendmoneytobank requestString.split(\"#\").length..."
				+ requestString.split("#").length + " and the requestString itself ===" + requestString);

		if (requestString.split("#").length == 7) {
            String lastResponse = requestString.split("#")[5];
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
          //  msgType = "1";
        } else {
			step5USSD(requestString);
		}
		return response;
    }

    private ServiceResponse step5USSD(String requestString) {
		System.out.println("step5ussd requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 8) {
    		String[] entries = requestString.split("#");
    		String walletNumber = "";
            accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
            session.getServletContext().setAttribute(sessionID + "_accounts", accounts);
            StringBuilder content = new StringBuilder();
            if (accounts != null) {
        		List<WalletAccount> accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
        		if(entries[4].length()==10) {
        			walletNumber = entries[4];
        			//payRRRDTO.setSourceAccountNo(entries[5]);
        		}else {
        			walletNumber = (accounts.get(Integer.parseInt(entries[4])-1).getAccountNumber());
        		}

                String accountNumber = StringUtils.substring(requestString.split("#")[5], 0, 10);
                Integer bankSelectedIndex = Integer.parseInt(requestString.split("#")[6]) - 1;

                @SuppressWarnings("unchecked")
				List<BankDTO> validBanks = (List<BankDTO>) session.getServletContext().getAttribute("validBanks");
                BankDTO selectedBank = validBanks.get(bankSelectedIndex);

                String amount = entries[3].trim();
                String bankCode = (selectedBank.getBankCode());
                String[][] banksUSSD = {{"044","050","214","070","011","058","030","082","221","215","033","215", "032", "035","057"},
                		                {"901","326","329","770","894","737","745","7111","909","822","919","7799","826","945","966"}};
                AccountEnquiryResponse bankVerification = bankService.verifyBankAccount(bankCode, accountNumber);
				if(bankVerification.getStatus().equalsIgnoreCase("failed")) {
                    content = new StringBuilder(bankVerification.getData().getResponseDescription());
                    String first = "#" + requestString.split("#")[3];
                    String second = "#" + requestString.split("#")[4];
                    requestString = requestString.replace(first, "");
                    requestString = requestString.replace(second, "");
                    session.getServletContext().setAttribute(sessionID, requestString);
                    response.setContent(content.toString());
                    return response;
                }else if(bankVerification.getData()!=null) {
					content = new StringBuilder("\"Account Successfully Verified "+
							bankVerification.getData().getAccountName() + "\n");
				}
				content = content.append("Please check your inbox to continue");
				response.setContent(content.toString());
				String bankUSSD = "";
                for(int x =0; x<banksUSSD[0].length; x++) {
               	 System.out.println("Value for Account Verification bankCode==" + bankCode + " banksUSSD==" + banksUSSD[1][x]);
               	 System.out.println(banksUSSD[0][x]+"me here oooh");
	               	if(bankCode.equalsIgnoreCase(banksUSSD[0][x])) {
	               		bankUSSD =  banksUSSD[1][x];
	               	    System.out.println(banksUSSD[1][x]+"me here oooh");
	               		break;
	               	}
               }
               String messageToSend ="*" + bankUSSD + "*" + "000" + "*"+"7000" + "+" + walletNumber+"+"+amount+"#";
               System.out.println(" Value for Account Verification bankCode==" + bankCode + " accountNumber==" + accountNumber + "You are sending=="+messageToSend);
       		   String message = "You initiated a fund wallet transaction via USSD\r\nPlease click on the link below to continue\r\n" + "tel:"+messageToSend;
	    	   walletAccountService.sendUSSDFundWalletLink(message, profile);
	    	   response.setMsgType("2");

            }
		}
		return response;
    }*/

	private ServiceResponse step1(String requestString) {
		System.out.println("Step 1==" + requestString);
		System.out.println("The lenght is "+ requestString.split("#").length);
		response = new ServiceResponse();
		if (requestString.split("#").length == 3) {
			response.setContent(fundWallet + "\r\nEnter Amount");
			response.setMsgType("1");
		} else {
			System.out.println("going to step2 of fund wallet requestString.split(\"#\").length..."+requestString.split("#").length + requestString);
			step2(requestString);
		}

		return response;
	}

	private ServiceResponse step2(String requestString) {
		// *7000#2#500#1
		System.out.println("step2 of fund wallet requestString.split(\"#\").length..."+requestString.split("#").length + requestString);
		if (requestString.split("#").length == 4) {
			List<WalletAccount> accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
			if (accounts != null) {
				String content = "";
				String[] entries = requestString.split("#");
				String Amount = entries[3];
				System.out.println(" The Amount as Supplied ==="+Amount);
				if (accounts.size() > 1) {
					content = "Select Wallet to fund: \n";
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
                    response.setContent(content);
                   // String msgType = "1";
				}else {
                    String request = requestString + "#" + accounts.get(0).getAccountNumber();
                    session.getServletContext().setAttribute(sessionID, request);
                    System.out.println(" I am formally running the else now..." + request);
                    step3(request);
                }
			}
		}else {
			step3(requestString);
		}
		return response;
	}

    public ServiceResponse step3(String requestString) {
		String[] entries = requestString.split("#");
		String accountNumber;
		int x = 0;
		for (String entry : entries) {

            System.out.println(" The Entries===" + entry + "The position" + x);
            x += 1;
        }
		List<WalletAccount> accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
		if(entries[4].length()==10) {
			 accountNumber = entries[4];
			//payRRRDTO.setSourceAccountNo(entries[5]);
		}else {
			 accountNumber = (accounts.get(Integer.parseInt(entries[4])-1).getAccountNumber());
		}

        response.setContent("To continue, please follow the instructions in the Text message sent to you.");
        response.setMsgType("2");
		String firstName, lastName, email, phone, walletNumber, link, amount;
		firstName = profile.getUser().getFirstName();
		lastName = profile.getUser().getLastName();
		walletNumber = accountNumber;
		phone = sessionID;
		amount = entries[3].trim();
		try {
			email = profile.getUser().getEmail();
			if(email.isEmpty()) {
				email = "support@remita.net";
			}
		}catch(Exception e) {
			email = "support@remita.net";
		}
		link = "https://remitaweb.herokuapp.com/fund/" + phone.trim() + "/" + email.trim() + "/"
		+ amount + "/" + walletNumber + "/" + firstName.trim() + "/" + lastName.trim();
		String message = "You initiated a fund wallet transaction via USSD \r\nPlease click on the link below to continue\r\n"
				+ link;
		walletAccountService.sendUSSDFundWalletLink(message, profile);

        return response;
	}

    public ServiceResponse getResponse() {
		return response;
	}
}
