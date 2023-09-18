package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.domain.AccessItem;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

/**
 * Service Interface for managing {@link AccessItem}.
 */
public interface CashConnectService {

    ResponseEntity<GenericResponseDTO> getBvn(String bvnNumber, HttpSession session);

    ResponseEntity<GenericResponseDTO> validateBVN(ValidateBVNDTO validateBVNDTO);

    ResponseEntity<GenericResponseDTO> CreateNewAccount(CashConnectAccountRequestDTO requestDTO);

    ResponseEntity<GenericResponseDTO> upgradeAccountKyc(UpgradeTierKycDTO requestDTO);

    ResponseEntity<GenericResponseDTO> upgradeAccountTier(UpgradeTierDTO requestDTO);

    ResponseEntity<GenericResponseDTO> getAccountStatement(String accountNumber, String fromDate, String toDate);

    ResponseEntity<GenericResponseDTO> retrieveAccountNumber(String TransactionTrackingRef);

    ResponseEntity<GenericResponseDTO> getAccountSummary(String accountNumber);

    ResponseEntity<GenericResponseDTO> getAccountInfo(String accountNumber);

    ResponseEntity<GenericResponseDTO> getBankList();

    ResponseEntity<GenericResponseDTO> fundIntra(FundIntraDTO fundIntraDTO);

    ResponseEntity<GenericResponseDTO> fundInterBank(FundIntraDTO fundIntraDTO);

    ResponseEntity<GenericResponseDTO> getFundTransferStatus(String transactionTrackingRef);

    ResponseEntity<GenericResponseDTO> getLoanOffers(LoanOfferDTO loanOfferDTO);

    ResponseEntity<GenericResponseDTO> acceptLoanOffer(AcceptLoanDTO acceptLoanDTO);

    ResponseEntity<GenericResponseDTO> loanPaymentNotification(LoanPaymentNotificationDTO notificationDTO);

    ResponseEntity<GenericResponseDTO> getLoanStatus(String LoanId, String accountNumber, String PhoneNo);

    ResponseEntity<GenericResponseDTO> getCreditReport(String bvn);

    ResponseEntity<GenericResponseDTO> getCreditReportByPhone(String PhoneNo);

    ResponseEntity<GenericResponseDTO> transactionPaymentNotification(CashConnectWebHookDTO notificationDTO);

    ResponseEntity<GenericResponseDTO> registerWebhook(RegisterWebHookDTO registerWebHook);

    GenericResponseDTO sendMoneyToBank(FundDTO fundDTO/*, Double intraFee, Double vatFee*/);

    GenericResponseDTO balanceCashConnectAccount(FundDTO fundDTO, double charges);

    ResponseEntity<SimpleResponseDTO> interbankCallBack(InterBankCallBackDTO interBankCallBackDTO);

    GenericResponseDTO intraTransfer(String sourceAccount, String destinationAccount, String destinationAccountName,
                                     double amount, String narration);

}
