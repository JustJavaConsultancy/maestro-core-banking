package ng.com.justjava.corebanking.web.rest.remitastp;

import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.stp.TransactionType;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.stp.*;
import ng.com.systemspecs.apigateway.service.dto.stp.*;
import ng.com.justjava.corebanking.web.rest.errors.remitastp.InvalidWalletOperatorException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TopUpPaymentNotifcationResource {

    private final Logger log = LoggerFactory.getLogger(TopUpPaymentNotifcationResource.class);
    String mirrorAccount = "0039123456";
    String defaultBankCode = "011";
    @Autowired
    WalletAccountService walletAccountService;
    @Autowired
    private TransactionController transactionController;
    @Autowired
    private ProfileService profileService;

    //   @PostMapping(value = "ext/topup/notification",consumes = MediaType.ALL_VALUE)
    public ResponseEntity processPaymentNotification(@RequestBody TopPaymentNotificationRequest notification) {
        log.info("Topup Payment notification received for {}", notification);
        try {
            WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(notification.getWalletId());
            TransactionDto topupRequest = new TransactionDto();
            topupRequest.setReferenceId(notification.getPaymentReference());
            topupRequest.setAmount(notification.getAmount());
            topupRequest.setChannel(notification.getPaymentChannel());
            topupRequest.setCurrency(Currency.NGN);
            if (walletAccount != null) {
                String accountNumber = walletAccount.getAccountNumber();
                topupRequest.setDestinationAccount(accountNumber);
                topupRequest.setDestinationAccountBankCode(defaultBankCode);
                topupRequest.setDestinationAccountName(walletAccount.getAccountName());
                topupRequest.setDestinationNarration(StringUtils.defaultIfEmpty(notification.getNarration(), "Wallet Top Up"));
                topupRequest.setSourceAccount(mirrorAccount);
                topupRequest.setSourceAccountName(defaultBankCode + " Mirror Account");
                topupRequest.setSourceNarration(StringUtils.defaultIfEmpty(notification.getNarration(), "Wallet Top Up"));
                topupRequest.setTransactionType(TransactionType.WALLET_TOPUP); //Todo change
                topupRequest.setTransactionAuthSignature("TP" + System.nanoTime());
                topupRequest.setTransactionAuthId(Hex.encodeHexString(String.format("API/%s/%s/%s/%s", notification.getPaymentReference(), notification.getBankCode(), accountNumber, System.nanoTime()).getBytes()));
                DefaultApiResponse topupResponse = transactionController.SingleTransfer(topupRequest);
                Map<String, String> dataMap = new HashMap<>();
                if (topupResponse.getData() != null) {
                    TransactionResponseDto topupAccount = (TransactionResponseDto) topupResponse.getData();
                    dataMap.put("processCode", topupAccount.getProcessCode());
                    dataMap.put("processMessage", topupAccount.getProcessMessage());
                }
                if (topupResponse.getStatus().equals(ExtendedConstants.ResponseStatus.API_SUCCESS_STATUS.getCode())) {
                } else {
                    return ResponseEntity.ok("OK");
                }
            } else {
                return ResponseEntity.ok("OK");
            }
        } catch (InvalidWalletOperatorException ie) {
            ie.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("OK");
    }

}
