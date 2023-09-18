package ng.com.justjava.corebanking.service.ussd;

import ng.com.justjava.corebanking.domain.JournalLine;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.service.JournalLineService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.InvoiceDTO;
import ng.com.justjava.corebanking.service.dto.PaymentResponseDTO;
import ng.com.justjava.corebanking.util.Utility;

import javax.servlet.http.HttpSession;
import java.util.List;

public class ApproveRequestMoney {
    private ServiceResponse response;
    private final JournalLineService journalLineService;
    private final Profile profile;
    private final HttpSession session;
    private final String sessionID;
    private final WalletAccountService walletAccountService;
    private final Utility utility;

    public ApproveRequestMoney(String requestString, JournalLineService journalLineService,
                               Profile profile, WalletAccountService walletAccountService, HttpSession session, String sessionID, Utility utility) {
        this.journalLineService = journalLineService;
        this.profile = profile;
        this.session = session;
        this.sessionID = sessionID;
        this.utility = utility;
        this.walletAccountService = walletAccountService;
        this.session.setAttribute("username", profile.getPhoneNumber());
        step1(requestString);
    }

	private ServiceResponse step1(String requestString) {

		System.out.println("Step 1==" + requestString);
		response = new ServiceResponse();
		if (requestString.split("#").length == 2) {
			List<JournalLine> invoices=journalLineService.findCustomerInvoiceByPhoneNumber(profile.getPhoneNumber());
			String content = "Select Request To Approve/Reject \n\n";
			int count = 1;
			for (JournalLine invoice : invoices) {
				System.out.println(" Invoice==="+invoice);
				content = content+count+". "+invoice.getJounal().getDisplayMemo()+"\n\n";
				count=count+1;
			}
			response.setContent(content);
			response.setMsgType("1");
			return response;
		} else {
			step2(requestString);
		}

		return response;
	}

	private ServiceResponse step2(String requestString) {

		if (requestString.split("#").length == 3) {
            System.out.println("step2 requestString.split(\"#\").length..." + requestString.split("#").length);
            System.out.println("Approve Request Money Step 2==" + requestString);

            String[] entries = requestString.split("#");
            int count = 0;
            for (String entry : entries) {
                System.out.println("step2 entry in ApproveRequest=====\n" + entry + " " + entry);
                count = count + 1;
            }
            Integer transactionSelected = Integer.parseInt(entries[2]) - 1;
            List<JournalLine> invoices = journalLineService.
                findCustomerInvoiceByPhoneNumber(profile.getPhoneNumber());

            JournalLine journalLine = invoices.get(transactionSelected);

            requestString = requestString + "#" + journalLine.getTransactionRef();
            String content = "Select \n\n 1. to Approve \n\n 2. to Reject";
            session.getServletContext().setAttribute(sessionID, requestString);
            response.setContent(content);
            response.setMsgType("1");
		} else {
			step3(requestString);
		}

		return response;
	}
	private ServiceResponse step3(String requestString) {

		if (requestString.split("#").length == 5) {
			System.out.println("step3 requestString.split(\"#\").length..."+requestString.split("#").length);
            String[] entries = requestString.split("#");

            InvoiceDTO invoice = new InvoiceDTO();
            String action = "approve";
            if ("2".equalsIgnoreCase(entries[4])) {
                action = "reject";
            }
            invoice.setAction(action);
            invoice.setReference(entries[3]);
            PaymentResponseDTO paymentResponse = walletAccountService.treatInvoice(invoice);
            String message = paymentResponse.getMessage();
            response.setContent(message);
            response.setMsgType("2");
        }
		return response;
	}

	public ServiceResponse getResponse() {
		return response;
	}
}
