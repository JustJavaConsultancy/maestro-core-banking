package ng.com.justjava.corebanking.service.ussd;

import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.service.BillerTransactionService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.PayRRRDTO;
import ng.com.justjava.corebanking.util.Utility;
import ng.com.systemspecs.remitabillinggateway.rrrdetails.GetRRRDetailsResponse;
import ng.com.systemspecs.remitabillinggateway.rrrdetails.GetRRRDetailsResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpSession;
import java.util.List;

public class PayRRR {
    private ServiceResponse response;
    private List<WalletAccount> accounts;
    private final Profile profile;
    private final BillerTransactionService billerTransactionService;
    private final WalletAccountService walletAccountService;
    private final HttpSession session;
    private final String sessionID;
    private final PasswordEncoder passwordEncoder;
    private final Utility utility;

    private final StringBuilder payRRRDetails = new StringBuilder("You are about to pay RRR-");
//    private final StringBuilder payRRRSuccess = new StringBuilder("You have successfully paid RRR-");

    public PayRRR(String requestString, Profile profile, BillerTransactionService billerTransactionService,
                  WalletAccountService walletAccountService, HttpSession session, String sessionID,
                  PasswordEncoder passwordEncoder, Utility utility) {
        this.profile = profile;
        this.billerTransactionService = billerTransactionService;
        this.walletAccountService = walletAccountService;
        this.session = session;
        this.sessionID = sessionID;
        this.passwordEncoder = passwordEncoder;
		this.utility=utility;
		step1(requestString);
	}

	private ServiceResponse step1(String requestString) {

		System.out.println("Step 1==" + requestString);
		response = new ServiceResponse();
		if (requestString.split("#").length == 2) {
			response.setContent("Enter RRR");
			response.setMsgType("1");
		} else {
			step2(requestString);
		}

		return response;
	}

	private ServiceResponse step2(String requestString) {
        System.out.println("step2 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 3) {

            List<WalletAccount> accounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
            session.getServletContext().setAttribute(sessionID + "_accounts", accounts);

            if (accounts != null) {
                String content = "";
                String[] entries = requestString.split("#");
                String RRR = entries[2];
                System.out.println(" The RRR as Supplied ===" + RRR);
                GetRRRDetailsResponse rrrResponse = billerTransactionService.getRRRDetails(RRR, utility.getUniqueRequestId());

                session.getServletContext().setAttribute(sessionID + "_rrrResponse", rrrResponse);

                System.out.println("The RRR details fetched====\n\n\n\n\n\n\n=" + rrrResponse.toString());
                if (!"00".equalsIgnoreCase(rrrResponse.getResponseCode())) {
                    response.setContent(rrrResponse.getResponseMsg());
                    response.setMsgType("2");
                    return response;
                } else {
                    requestString = requestString + "#" + rrrResponse.getResponseData().get(0).getAmountDue() +
                        "#" + rrrResponse.getResponseData().get(0).getDescription();
                    session.getServletContext().setAttribute(sessionID, requestString);
                }

				if (accounts.size() > 1) {
					content = "Select Your Wallet to Debit: \n";
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
                    response.setMsgType("1");
				}else {
					String request=requestString+"#"+accounts.get(0).getAccountNumber();

					session.getServletContext().setAttribute(sessionID, request);
					step3(request);
				}
			}
		}else {
			step3(requestString);
		}
		return response;
	}
	private ServiceResponse step3(String requestString) {
        System.out.println("step3 requestString.split(\"#\").length..." + requestString.split("#").length);
        if (requestString.split("#").length == 6) {

            GetRRRDetailsResponse rrrResponse = (GetRRRDetailsResponse) session.getServletContext().getAttribute(sessionID + "_rrrResponse");

            if (rrrResponse == null) {
                String RRR = requestString.split("#")[2];
                rrrResponse = billerTransactionService.getRRRDetails(RRR, utility.getUniqueRequestId());
            }

            System.out.println("The RRR details fetched 2====\n\n\n\n\n\n\n=" + rrrResponse.toString());
            String enter_your_pin = "Enter Your Pin";
            if ("00".equalsIgnoreCase(rrrResponse.getResponseCode())) {
                if (!rrrResponse.getResponseData().isEmpty() && rrrResponse.getResponseData().get(0) != null) {
                    GetRRRDetailsResponseData data = rrrResponse.getResponseData().get(0);
                    payRRRDetails.append(data.getRrr())
                        .append(": ")
                        .append(data.getRrrAmount())
                        .append(" for ")
                        .append(data.getDescription())
                        .append(". ")
                        .append(enter_your_pin);
                }
                response.setContent(payRRRDetails.toString());
                response.setMsgType("1");
            }else {
                response.setContent("Transaction failed, please try again later");
                response.setMsgType("2");
            }

		}else {
			step4(requestString);
		}
		return response;
	}
	private ServiceResponse step4(String requestString) {
		System.out.println("step4 requestString.split(\"#\").length..."+requestString.split("#").length);
		if (requestString.split("#").length == 7) {
			String[] entries = requestString.split("#");

			String pin = entries[6];
			if (!passwordEncoder.matches(pin, profile.getPin())) {
				response.setContent("Invalid Pin");
				response.setMsgType("2");
				requestString = requestString.replace("#" + pin, "");
				System.out.println(" The requestString after failed pin entered===" + requestString);
				session.getServletContext().setAttribute(sessionID, requestString);
			} else {
                accounts = (List<WalletAccount>) session.getServletContext().getAttribute(sessionID + "_accounts");
                System.out.println(" Finally=========================================" + requestString);

				PayRRRDTO payRRRDTO = new PayRRRDTO();
                payRRRDTO.setAmount(Double.valueOf(entries[3]));
                payRRRDTO.setNarration(entries[4]);
                payRRRDTO.setPin(entries[6]);
                payRRRDTO.setRrr(entries[2]);
                if (entries[5].length() == 10) {
                    payRRRDTO.setSourceAccountNo(entries[5]);
                } else {
                    payRRRDTO.setSourceAccountNo(accounts.get(Integer.parseInt(entries[5]) - 1).getAccountNumber());
                }
                payRRRDTO.setTransRef(utility.getUniqueTransRef());

                session.getServletContext().removeAttribute(sessionID + "_accounts");

                GenericResponseDTO responseDTO = billerTransactionService.payRRR(payRRRDTO);
                if (HttpStatus.OK.equals(responseDTO.getStatus())){
                    response.setContent("RRR- " + payRRRDTO.getRrr() + " for " + utility.formatMoney(payRRRDTO.getAmount()) + " to \"Biller\" is paid. Thank You");
                }else {
                    response.setContent(responseDTO.getMessage());
                }
                response.setMsgType("2");

            }

		}
		return response;
	}
	public ServiceResponse getResponse() {
		return response;
	}
}
