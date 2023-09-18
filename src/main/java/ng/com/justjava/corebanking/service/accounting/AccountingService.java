package ng.com.justjava.corebanking.service.accounting;

import ng.com.justjava.corebanking.config.AsyncConfiguration;
import ng.com.justjava.corebanking.domain.*;
import ng.com.justjava.corebanking.service.*;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.justjava.corebanking.domain.enumeration.PaymentType;
import ng.com.justjava.corebanking.domain.enumeration.SpecificChannel;
import ng.com.justjava.corebanking.domain.enumeration.TransactionStatus;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.justjava.corebanking.service.fcm.PushNotificationService;
import ng.com.justjava.corebanking.service.kafka.producer.CallbackProducer;
import ng.com.justjava.corebanking.service.mapper.WalletAccountMapper;
import ng.com.justjava.corebanking.util.Utility;
import ng.com.systemspecs.remitarits.singlepayment.SinglePayment;
import ng.com.systemspecs.remitarits.singlepayment.SinglePaymentRequest;
import ng.com.systemspecs.remitarits.singlepayment.SinglePaymentResponse;
import ng.com.systemspecs.remitarits.util.Data;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.ap.internal.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executor;

import static java.lang.System.out;

@Service
@Transactional
public class AccountingService {

    public static final String GMAIL_COM = "mabdulwasii@gmail.com";

    private final BillerTransactionService billerTransactionService;
    private final UserService userService;
    private final Utility utility;
    private final WalletAccountService walletAccountService;
    private final WalletAccountTypeService walletAccountTypeService;
    private final WalletAccountMapper walletAccountMapper;
    private final JournalService journalService;
    private final JournalLineService journalLineService;
    private final BankService bankService;
    private final TransactionLogService transactionLogService;
    private final RITSService ritsService;
    private final SchemeService schemeService;
    private final PushNotificationService pushNotificationService;
    private final ProfileService profileService;
    private final CashConnectService cashConnectService;
    private final AsyncConfiguration asyncConfiguration;
    private final PolarisVulteService polarisVulteService;
    private final CallbackProducer callbackProducer;

    private final Logger log = LoggerFactory.getLogger(AccountingService.class);
    //Card Inst. Commission and Account to Charge Accts
    @Value("${app.polaris-card.mcu-commission-acct}")
    String MCU_COMMISSION_ACCT;
    @Value("${app.email.addresses.Kazeem}")
    private String Kazeem;
    @Value("${app.percentage.bonus}")
    private double bonusPercentage;
    @Value("${app.percentage.vat}")
    private double vatFeePercentage;
    @Value("${app.percentage.lendhub}")
    private double lendHubFeePercentage;
    @Value("${app.percentage.mutual-benefit-insurance}")
    private double mutualBenefitPercentage;
    @Value("${app.cashconnect.account-number}")
    private String CASHCONNECT_VIRTUAL_ACCOUNT_NUMBER;
    @Value("${app.charges.bankToWalletPercentage}")
    private double bankToWalletPercentage;
    @Value("${app.charges.biller}")
    private double billerCharges;
    @Value("${app.charges.bankToWalletShare}")
    private double bankToWalletShare;
    @Value("${app.charges.remitaWalletTobankCharge}")
    private double walletToBankCharge;
    @Value("${app.charges.ussdCharge}")
    private double ussdCharge;
    @Value("${app.charges.firstRangeCharge}")
    private double firstRangeCharge;
    @Value("${app.charges.secondRangeCharge}")
    private double secondRangeCharge;
    @Value("${app.charges.thirdRangeCharge}")
    private double thirdRangeCharge;
    @Value("${app.charges.capped}")
    private double cappedAmount;
    @Value("${app.charges.grossIncomePercentage}")
    private double grossIncomePercentage;
    @Value("${app.charges.vatPayablePercentage}")
    private double vatPayablePercentage;
    @Value("${app.charges.introducerFeePercentage}")
    private double introducerFeePercentage;
    @Value("${app.charges.partneringFeePercentage}")
    private double partneringFeePercentage;
    @Value("${app.charges.pomengranateFeePercentage}")
    private double pomengranateFeePercentage;
    @Value("${app.charges.nibbsCostOfTransaction}")
    private double nibbsCostOfTransaction;
    @Value("${app.charges.humanManagerProcessingFee}")
    private double humanManagerProcessingFee;
    @Value("${app.charges.interbankPaymentProcessingFeesWallet}")
    private double interbankPaymentProcessingFeesWallet;
    @Value("${app.charges.free}")
    private double zeroFee;
    @Value("${app.constants.dfs.biller-payment-account}")
    private String BILLER_PAYMENT_ACCOUNT;
    @Value("${app.constants.dfs.transit-account}")
    private String TRANSIT_ACCOUNT;
    @Value("${app.constants.dfs.correspondence-account}")
    private String CORRESPONDENCE_ACCOUNT;
    @Value("${app.constants.dfs.hm-correspondence-account}")
    private String HM_CORRESPONDENCE_ACCOUNT;
    @Value("${app.constants.dfs.charge-account}")
    private String POUCHII_INCOME_ACCT;
    @Value("${app.constants.dfs.vat-account}")
    private String VAT_ACCOUNT;
    @Value("${app.constants.dfs.biller-cost-of-funds}")
    private String BILLER_COST_OF_FUNDS;
    @Value("${app.constants.dfs.itex-commission-acct}")
    private String ITEX_COMMISSION_ACCT;
    @Value("${app.constants.dfs.insurance-commission-acct}")
    private String INSURANCE_COMMISSION_ACCT;
    @Value("${app.email.subjects.debit-title}")
    private String debitTitle;
    @Value("${app.email.subjects.credit-title}")
    private String creditTitle;
    @Value("${app.itex.commission.aedc}")
    private Double aedcCommission;
    @Value("${app.itex.commission.phedc}")
    private Double phedcCommission;
    @Value("${app.itex.commission.eedc}")
    private Double eedcCommission;
    @Value("${app.itex.commission.ibedc}")
    private Double ibedcCommission;
    @Value("${app.itex.commission.ekedc}")
    private Double ekedcCommission;
    @Value("${app.itex.commission.kedco}")
    private Double kedcoCommission;
    @Value("${app.itex.commission.ikedc}")
    private Double ikedcCommission;
    @Value("${app.itex.commission.internet}")
    private Double internetCommission;
    @Value("${app.itex.commission.cabletv}")
    private Double cableTvCommission;
    @Value("${app.itex.commission.startimes}")
    private Double startimesTvCommission;
    @Value("${app.itex.commission.mobile}")
    private Double mobileCommission;
    @Value("${app.itex.commission.glo}")
    private Double gloCommission;
    @Value("${app.itex.commission.airtel}")
    private Double airtelCommission;
    @Value("${app.itex.commission.mtn}")
    private Double mtnCommission;
    @Value("${app.constants.dfs.remita-income-account}")
    private String RPSL_INTERBANK_SERVICES_ACCT;
    @Value("${app.constants.dfs.cashconnect-interbank-services-acct}")
    private String CASH_CONNECT_INTERBANK_SERVICES_ACCT;
    @Value("${app.constants.dfs.coral-income-account}")
    private String CORAL_PAY_USSD_FUNDING_ACCT;
    @Value("${app.constants.dfs.lending-disbursement-income-acct}")
    private String LENDING_DISBURSEMENT_INCOME_ACCT;

    @Value("${app.constants.dfs.lending-hub-fee-acct}")
    private String LENDING_HUB_FEE_ACCT;
    @Value("${app.constants.dfs.polaris-income-account}")
    private String POLARIS_INTERBANK_SERVICES_ACCT;
    @Value("${app.constants.dfs.polaris-commission-account}")
    private String POLARIS_COMMISSION_ACCT;
    @Value("${app.constants.dfs.polaris-pomengranate-commission-account}")
    private String POLARIS_POMENGRANATE_COMMISSION_ACCT;
    @Value("${app.constants.dfs.hm-vat-payable-account}")
    private String HM_VAT_PAYABLE_ACCT;
    @Value("${app.constants.dfs.hm-processing-fee-account}")
    private String HM_PROCESSING_FEE_ACCT;
    @Value("${app.constants.dfs.introducer-account}")
    private String INTRODUCER_ACCT;
    @Value("${app.constants.dfs.rpsl-inline-acct}")
    private String RSPL_INLINE_ACCT;
    @Value("${app.constants.dfs.paymasta-comm-acct}")
    private String PAYMASTA_COMMISSIONS_ACCT;
    @Value("${app.constants.dfs.paymasta-vat-acct}")
    private String PAYMASTA_VAT_ACCT;
    @Value("${app.constants.dfs.paymasta-income-acct}")
    private String PAYMASTA_INCOME_ACCT;
    @Value("${app.constants.dfs.wynk-income-acct}")
    private String WYNK_INCOME_ACCT;
    @Value("${app.constants.dfs.forge-income-acct}")
    private String FORGE_INCOME_ACCT;
    @Value("${app.polaris-card.charges-acct}")
    private String CARD_CHARGES_ACCT;

    @Value("${app.scheme.systemspecs}")
    private String SYSTEMSPECS_SCHEME;

    @Value("${app.scheme.mcpherson}")
    private String MCPHERSON_SCHEME;

    @Value("${app.charges.humanManagerPromo}")
    private boolean HUMAN_MANAGER_PROMO;


    public AccountingService(@Lazy JournalService journalService,
                             @Lazy JournalLineService journalLineService,
                             @Lazy WalletAccountService walletAccountService,
                             BillerTransactionService billerTransactionService, WalletAccountTypeService walletAccountTypeService, BankService bankService,
                             TransactionLogService transactionLogService, @Lazy RITSService ritsService,
                             UserService userService, @Lazy Utility utility, WalletAccountMapper walletAccountMapper, SchemeService schemeService,
                             PushNotificationService pushNotificationService, ProfileService profileService, CashConnectService cashConnectService,
                             AsyncConfiguration asyncConfiguration, PolarisVulteService polarisVulteService, CallbackProducer callbackProducer) {
        this.walletAccountService = walletAccountService;
        this.journalService = journalService;
        this.journalLineService = journalLineService;
        this.billerTransactionService = billerTransactionService;
        this.walletAccountTypeService = walletAccountTypeService;
        this.bankService = bankService;
        this.transactionLogService = transactionLogService;
        this.ritsService = ritsService;
        this.userService = userService;
        this.utility = utility;
        this.walletAccountMapper = walletAccountMapper;
        this.schemeService = schemeService;
        this.pushNotificationService = pushNotificationService;
        this.profileService = profileService;
        this.cashConnectService = cashConnectService;
        this.asyncConfiguration = asyncConfiguration;
        this.polarisVulteService = polarisVulteService;
        this.callbackProducer = callbackProducer;
    }

    public String fundWallet(FundDTO fundDTO) throws Exception {
        out.println("Accounting Service FundDTO " + fundDTO);
        String response = "Success";

        if (Strings.isEmpty(fundDTO.getChannel()))
            return "Channel Cannot be empty";
        if ("INVOICE".equalsIgnoreCase(fundDTO.getChannel())
            || fundDTO.getChannel().equalsIgnoreCase("WalletToWallet")) {
            response = walletToWallet(fundDTO);
//            walletToWalletBookKeeping(fundDTO);
        } else if (fundDTO.getChannel().equalsIgnoreCase("BankToWallet")) {
            response = BankToWallet(fundDTO);
        } else if (fundDTO.getChannel().equalsIgnoreCase("WalletToBank")) {
            response = WalletToBank(fundDTO);
        } else if (fundDTO.getChannel().equalsIgnoreCase("WalletToBankReversal")) {
            response = WalletToBankReversal(fundDTO);
        } else if (fundDTO.getChannel().equalsIgnoreCase("BankToWalletReversal")) {
            response = BankToWalletReversal(fundDTO);
        } else if (fundDTO.getChannel().equalsIgnoreCase("BankToWalletIncompleteBank")) {
            response = debitWallet(fundDTO);
        } else if (fundDTO.getChannel().equalsIgnoreCase("WalletToBankIncompleteBank")) {
            response = WalletToBankIncompleteBank(fundDTO);
        } else if (fundDTO.getChannel().equalsIgnoreCase("WalletToBankIncompleteWallet")) {
            response = WalletToBankIncompleteWallet(fundDTO);
        } else if (fundDTO.getChannel().equalsIgnoreCase("WalletToWallets")) {
            response = walletToWallets(fundDTO);
        } else if (fundDTO.getChannel().equalsIgnoreCase("WalletToBanks")) {
            response = WalletToBanks(fundDTO);
        } else if (fundDTO.getChannel().equalsIgnoreCase("Card Request")) {
            response = cardRequestProcessing(fundDTO);
        } else if (fundDTO.getChannel().equalsIgnoreCase("LoanDisbursement")) {
            response = loanDisbursement(fundDTO);
        }

        return response;
    }

    public String doubleEntry(List<JournalLineDTO> lines, String memo, Double charges, TransactionStatus status, FundDTO fundDTO, Double vatFee) {
        out.println("Double Entry lines == >" + lines);

        /*if(*/
        validateLines(lines, fundDTO);/*){*/
//            throw new GenericException("Transaction failed because account not balanced");
//        }

        String channel = fundDTO.getChannel();
        Journal journal = new Journal();
        journal.setReference(fundDTO.getTransRef());
        journal.setNarration(fundDTO.getNarration());
        journal.setTransDate(LocalDateTime.now());
        journal.setChanges(charges);
        journal.setExternalRef(fundDTO.getRrr());
        journal.setTransactionStatus(status);
        journal.setUserComment(fundDTO.getShortComment());

        if ("INVOICE".equalsIgnoreCase(channel)) {
            journal.setPaymentType(PaymentType.INVOICE);
            fundDTO.setBulkTrans(false);
        } else {
            journal.setPaymentType(PaymentType.PAYMENT);
        }

        journal.setMemo(memo);
        journal = journalService.save(journal);

        for (JournalLineDTO line : lines) {
            out.println("line ====> " + line);
            JournalLine journalLine = new JournalLine();
            journalLine.setCredit(line.getCredit());
            journalLine.setDebit(line.getDebit());
            WalletAccount account = line.getAccount(); // walletAccountService.findOneByAccountNumber(line.getAccountNumber());
            out.println("account === " + account);

            if (!"INVOICE".equalsIgnoreCase(channel) && account != null) {
                account.setCurrentBalance(String.valueOf(Double.parseDouble(account.getCurrentBalance()) + journalLine.getCredit()));
                account.setCurrentBalance(String.valueOf(Double.parseDouble(account.getCurrentBalance()) - journalLine.getDebit()));

                account.setActualBalance(String.valueOf(Double.parseDouble(account.getActualBalance()) + journalLine.getCredit()));
                account.setActualBalance(String.valueOf(Double.parseDouble(account.getActualBalance()) - journalLine.getDebit()));

                account = walletAccountService.save(account);

                journalLine.setCurrentBalance(Double.valueOf(account.getActualBalance()));

                Profile accountOwner = account.getAccountOwner();

                String displayMemo = journal.getDisplayMemo().replaceAll("₦", "NGN");

                if (!StringUtils.isEmpty(fundDTO.getShortComment()) && !"null".equalsIgnoreCase(fundDTO.getShortComment())) {
                    displayMemo = displayMemo + " - " + fundDTO.getShortComment();
                }

                try {
                    sendAlertAndEmail(fundDTO, status, journal, line, account, accountOwner, displayMemo, charges, vatFee);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            journalLineService.save(journalLine, journal, account);
        }
        markTransactionReversed(fundDTO.getTransRef());
        return "successful";
    }

    private void transactionCallback(FundDTO fundDTO, String specificChannel, String channel, TransactionStatus status) {
        if (SpecificChannel.IBILE_PAY.getName().equalsIgnoreCase(specificChannel)) return;
        if (!"INVOICE".equalsIgnoreCase(channel)) {
            fundDTO.setStatus(status);
            callbackProducer.send(fundDTO);
        }
    }

    private void validateLines(List<JournalLineDTO> lines, FundDTO fundDTO) {
        out.println("=== Validating Lines ===");
        out.println("=== Total Debit ===>>>  " + lines.stream().mapToDouble(JournalLineDTO::getCredit).sum());
        out.println("=== Total Credit ===>>>  " + lines.stream().mapToDouble(JournalLineDTO::getDebit).sum());
        if (lines.stream().mapToDouble(JournalLineDTO::getCredit).sum() != lines.stream().mapToDouble(JournalLineDTO::getDebit).sum()) {
            out.println("======= VALIDATE LINES =======");
            utility.sendEmail(utility.getDoubleEntryValidationEmailMap(), fundDTO.getSpecificChannel() + "Transaction failed",
                "Double entry validation failed for this transaction {} <br/> " + fundDTO + "<br/> " +
                    "Total Debit ===>>>  " + lines.stream().mapToDouble(JournalLineDTO::getCredit).sum() +
                    "</br>Total Credit ===>>>  " + lines.stream().mapToDouble(JournalLineDTO::getDebit).sum());
//            return false;
//            throw new GenericException("Transaction failed because account not balanced");
        }
//        return true;
    }

 /*   @NotNull
    private TransactionCallBackDTO buildTransactionCallBackDTO(TransactionStatus status, FundDTO fundDTO) {
        TransactionCallBackDTO transactionCallBackDTO = new TransactionCallBackDTO();

        transactionCallBackDTO.setDestination(fundDTO.getAccountNumber());
        transactionCallBackDTO.setDestinationAccountName(fundDTO.getBeneficiaryName());
        transactionCallBackDTO.setExternalRef(fundDTO.getRrr());
        transactionCallBackDTO.setNarration(fundDTO.getNarration());
        transactionCallBackDTO.setSource(fundDTO.getSourceAccountNumber());
        transactionCallBackDTO.setSourceAccountName(fundDTO.getSourceAccountName());
        transactionCallBackDTO.setStatus(status.toString());
        transactionCallBackDTO.setReference(transactionCallBackDTO.getReference());
        transactionCallBackDTO.setTransType(fundDTO.getChannel());
        transactionCallBackDTO.setAmount(fundDTO.getAmount());
        transactionCallBackDTO.setPhoneNumber(fundDTO.getPhoneNumber());
        transactionCallBackDTO.setSpecificTransType(fundDTO.getSpecificChannel()); //TODO replace with friendly text
        return transactionCallBackDTO;
    }

    private PayFeesNotificationDTO buildNotificationDTO(FundDTO fundDTO) {
        PayFeesNotificationDTO payFeesNotificationDTO = new Gson().fromJson(fundDTO.getAgentRef(), PayFeesNotificationDTO.class);
        return payFeesNotificationDTO;
    }*/

    //TODO create a private method to convert specific channelText to user friendly text


    private Double calculateBonusPointEarned(double transactionFee, String specificChannel, WalletAccount debitAccount) {
        if (SpecificChannel.IBILE_PAY.getName().equalsIgnoreCase(specificChannel)
            || SpecificChannel.LIBERTY.getName().equalsIgnoreCase(specificChannel)) {
            return zeroFee;
        }

        if (debitAccount != null
            && debitAccount.getAccountOwner() != null
            && debitAccount.getScheme() != null
        ) {
            Scheme scheme = debitAccount.getScheme();
            if (SYSTEMSPECS_SCHEME.equalsIgnoreCase(scheme.getSchemeID())
                || MCPHERSON_SCHEME.equalsIgnoreCase(scheme.getSchemeID()))
                return transactionFee * bonusPercentage;
        }

        return zeroFee;
    }

    private void sendAlertAndEmail(FundDTO fundDTO, TransactionStatus status, Journal journal, JournalLineDTO line,
                                   WalletAccount account, Profile accountOwner, String displayMemo, Double charges, Double vatFee) {


        out.println("FundDTO = " + fundDTO + "\n status = " + status + " \n journal = " + journal + " \n Line = "
            + line + " \n Account = " + account + " \n AccountOwner = " + accountOwner + " \n DisplayMemo  = " + displayMemo
            + "\n Charges = " + charges + "\n VAT fee = " + vatFee);

        PushNotificationRequest request = new PushNotificationRequest();
        request.setChannel(account.getScheme().getScheme().toLowerCase());

        String channel = fundDTO.getChannel();
        String specificChannel = fundDTO.getSpecificChannel();

        Long accountypeID = 1L;
        if (account.getWalletAccountType() != null) {
            Long id = account.getWalletAccountType().getId();
            Optional<WalletAccountTypeDTO> one = walletAccountTypeService.findOne(id);
            if (one.isPresent()) {
                accountypeID = one.get().getAccountypeID();
            }
        }

        Long schemeId = 1L;
        Scheme scheme = new Scheme();
        if (account.getScheme() != null) {
            schemeId = account.getScheme().getId();
            scheme = schemeService.findBySchemeID(Long.toString(schemeId));
        }

        out.println("AccountTypeId ==> " + accountypeID + " : SchemeId ==>" + schemeId);

        //Credit messages
        if (line.getCredit() > 0 && accountOwner != null && 3 != schemeId && 4 != accountypeID) {

            out.println("Inside Credit notification block");

            Double amountCredited = line.getCredit();

            String accountNumber = account.getAccountNumber();
            String accountName = account.getAccountName();
            String currentBalance1 = account.getCurrentBalance();
            LocalDateTime transDateTime = journal.getTransDate();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mma");
            String transDate = transDateTime.format(dateFormatter);
            String transTime = transDateTime.format(timeFormatter);

            if (TransactionStatus.COMPLETED.equals(status)) {

                try {
                    //send credit alert

                    userService.sendCreditAlert(accountOwner.getPhoneNumber(), amountCredited, accountNumber,
                        displayMemo, Double.parseDouble(currentBalance1),
                        transDateTime);

                    //send credit email
                    if (accountOwner.getUser() != null) {

                        String email = null;

                        if (accountOwner.getUser().getEmail() != null) {
                            email = accountOwner.getUser().getEmail();
                        }


                        if ("INVOICE".equalsIgnoreCase(channel)) {

                            request.setMessage("You have successfully requested the Sum of " + utility.formatMoney(amountCredited) + " from " + fundDTO.getSourceAccountName());
                            request.setTitle("Request Money");
                            request.setToken(accountOwner.getDeviceNotificationToken());
                            request.setRecipient(accountOwner.getPhoneNumber());
                            pushNotificationService.sendPushNotificationToToken(request);
                        }

                        if ("WalletToWallet".equalsIgnoreCase(channel)) {

                            sendWalletToWalletCreditAppNotification(fundDTO, accountOwner, request, amountCredited);

                            sendWalletToWalletCreditEmail(fundDTO, amountCredited, accountNumber, accountName, transDate, transTime, email);


                        } else if ("BankToWallet".equalsIgnoreCase(channel) && accountOwner.getUser() != null) {

                            sendBankToWalletCreditAppNotification(fundDTO, accountOwner, request, amountCredited);

                            sendBankToWalletCreditEmail(fundDTO, amountCredited, accountNumber, accountName, transDate, transTime, email);

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (TransactionStatus.REVERSED.equals(status)) {

                String email = null;

                if (accountOwner.getUser() != null) {

                    if (accountOwner.getUser().getEmail() != null) {
                        email = accountOwner.getUser().getEmail();
                    }
                }

                //send reversal alert
                userService.sendReversalAlert(accountOwner.getPhoneNumber(), amountCredited, accountNumber,
                    displayMemo, Double.parseDouble(currentBalance1),
                    transDateTime);

                if ("BankToWallet".equalsIgnoreCase(channel)) {

                    request.setMessage("Funding of your account(" + accountName + ") with the Sum of " + utility.formatMoney(amountCredited) + " has failed!");
                    request.setTitle("Fund Wallet Failed");
                    request.setToken(accountOwner.getDeviceNotificationToken());
                    request.setRecipient(accountOwner.getPhoneNumber());
                    pushNotificationService.sendPushNotificationToToken(request);

                    sendBankToWalletReversalEmail(fundDTO, amountCredited, accountNumber, accountName, transDate, transTime, email, accountOwner, displayMemo, Double.parseDouble(currentBalance1));

                }

                if ("WalletToWallet".equalsIgnoreCase(channel)) {
                    sendWalletToWalletReversalEmail(fundDTO, amountCredited, accountNumber, accountName, transDate, transTime, email, accountOwner, displayMemo, Double.parseDouble(currentBalance1));
                }
            }

        }//end of credit

        //Start debit messages
        else if (
            line.getDebit() > 0
                && accountOwner != null
                && 3 != schemeId
                && 4 != accountypeID) {

            Double amountDebited = line.getDebit();
            Double transactionAmount = fundDTO.getAmount() + fundDTO.getBonusAmount();

            String accountNumber = account.getAccountNumber();
            String accountName = account.getAccountName();
            String currentBalance1 = account.getCurrentBalance();
            LocalDateTime transDateTime = journal.getTransDate();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mma");
            String transDate = transDateTime.format(dateFormatter);
            String transTime = transDateTime.format(timeFormatter);

            if (TransactionStatus.COMPLETED.equals(status)) {

                try {
                    //send debit alert
                    if (charges > 0.00) {
                        displayMemo = displayMemo + " plus fees of " + utility.formatMoney(charges + vatFee);
                    }

                    if (fundDTO.getSpecificChannel().contains("disco")) {
                        displayMemo = displayMemo + ". Your Electricity token is " + fundDTO.getShortComment();
                    }

                    userService.sendDebitAlert(accountOwner.getPhoneNumber(), amountDebited, accountNumber,
                        displayMemo, Double.parseDouble(currentBalance1), transDateTime, fundDTO.getBonusAmount());

                    //send debit email
                    if (accountOwner.getUser() != null) {
                        String email = null;
                        if (accountOwner.getUser().getEmail() != null) {
                            email = accountOwner.getUser().getEmail();
                        }

                        out.println("Notification Email ===> " + email);

                        if ("INVOICE".equalsIgnoreCase(channel)) {
                            request.setMessage("The Sum of " + utility.formatMoney(amountDebited) + " is requested from " + fundDTO.getBeneficiaryName());
                            request.setTitle("Request Money");
                            request.setToken(accountOwner.getDeviceNotificationToken());
                            request.setRecipient(accountOwner.getPhoneNumber());
                            pushNotificationService.sendPushNotificationToToken(request);

                        } else if ("WalletToWallet".equalsIgnoreCase(channel)) {

                            sendDebitEmailWalletToWallet(fundDTO, charges, vatFee, specificChannel, amountDebited, accountNumber, accountName, transDate, transTime, email, transactionAmount);
//
                            sendWalletToWalletDebitAppNotification(fundDTO, accountOwner, charges, vatFee, request, specificChannel, transactionAmount);

                        } else if ("WalletToBank".equalsIgnoreCase(channel) && accountOwner.getUser() != null) {
                            String bankName = getBankName(fundDTO);

                            sendWalletToBankDebitEmail(fundDTO, charges, vatFee, amountDebited, accountNumber, accountName, transDate, transTime, email, bankName, transactionAmount);

                            sendWalletToBankDebitAppNotification(fundDTO, accountOwner, charges, vatFee, request, bankName, transactionAmount);

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (TransactionStatus.REVERSED.equals(status)) {

                //send debit alert
                if (charges > 0.00) {
                    displayMemo = displayMemo + " plus fees of " + utility.formatMoney(charges + vatFee);
                }

                userService.sendDebitAlert(accountOwner.getPhoneNumber(), line.getDebit(), accountNumber,
                    displayMemo, Double.parseDouble(currentBalance1), transDateTime, fundDTO.getBonusAmount());

                String email = null;

                if (accountOwner.getUser() != null) {
                    if (accountOwner.getUser().getEmail() != null) {
                        email = accountOwner.getUser().getEmail();
                    }
                }

                if ("WalletToBank".equalsIgnoreCase(channel)) {

                    String bankName = getBankName(fundDTO);

//                    sendWalletToBankDebitEmail(fundDTO, charges, vatFee, amountDebited, accountNumber, accountName, transDate, transTime, email, bankName, transactionAmount);

                    sendReversalEmail(fundDTO, amountDebited, accountNumber, transDate, transTime, email, accountOwner, displayMemo, Double.parseDouble(currentBalance1));

                    request.setMessage("Transfer of " + utility.formatMoney(transactionAmount) + " to account number : " + fundDTO.getAccountNumber() + " has failed");
                    request.setTitle("Send Money Failed");
                    request.setToken(accountOwner.getDeviceNotificationToken());
                    request.setRecipient(accountOwner.getPhoneNumber());
                    pushNotificationService.sendPushNotificationToToken(request);

                } else if ("WalletToWallet".equalsIgnoreCase(channel)) {

                    request.setMessage("Transfer of the Sum of " + utility.formatMoney(transactionAmount) + " to " + fundDTO.getBeneficiaryName() + " has failed!");
                    request.setTitle("Send Money Failed!");

                    if (specificChannel.toLowerCase().contains("vtu")) {
                        String message = "Purchase of " + utility.formatMoney(transactionAmount) + " Airtime";
                        if (StringUtils.isEmpty(fundDTO.getPhoneNumber())) {
                            message = message + " for " + fundDTO.getPhoneNumber();
                        }
                        message = message + " has failed";
                        request.setMessage(message);
                        request.setTitle("Airtime Purchase Failed");

                    } else if (specificChannel.toLowerCase().contains("data")) {
                        String message = "Purchase of " + utility.formatMoney(transactionAmount) + " Data";
                        if (StringUtils.isEmpty(fundDTO.getPhoneNumber())) {
                            message = message + " for " + fundDTO.getPhoneNumber();
                        }
                        message = message + " has failed";
                        request.setMessage(message);
                        request.setTitle("Data Purchase Failed");

                    } else if (SpecificChannel.PAY_RRR.getName().equalsIgnoreCase(specificChannel)) {
                        String message = "Purcahase of RRR- " + fundDTO.getRrr() + ": " + utility.formatMoney(transactionAmount) + " to Biller has failed!";
                        request.setMessage(message);
                        request.setTitle("RRR Payment Failed");

                    } else if (SpecificChannel.PAY_BILLER.getName().equalsIgnoreCase(specificChannel)) {
                        String message = "Purchase of " + utility.formatMoney(transactionAmount) + " to Biller has failed!";
                        request.setMessage(message);
                        request.setTitle("Biller Payment Failed");

                    } else if (specificChannel.toLowerCase().contains("disco")) {
                        String message = "Purchase of " + utility.formatMoney(transactionAmount) + " electricity has failed!";
                        request.setMessage(message);
                        request.setTitle("Electricity Payment Failed");

                    } else if (specificChannel.toLowerCase().contains("cabletv")) {
                        String message = "Purchase of " + utility.formatMoney(transactionAmount) + " cable tv subscription has failed!";
                        request.setMessage(message);
                        request.setTitle("Cable TV SubscriptionFailed");

                    } else if (specificChannel.toLowerCase().contains("internet")) {
                        String message = "Purchase of " + utility.formatMoney(transactionAmount) + " internet subscription has failed!";
                        request.setMessage(message);
                        request.setTitle("Internet Subscription Failed");

                    } else if (SpecificChannel.PAY_BILLS.getName().equalsIgnoreCase(specificChannel)) {
                        String message = "Purchase of " + utility.formatMoney(transactionAmount) + " to Biller has failed!";
                        request.setMessage(message);
                        request.setTitle("Bills Payment Failed");
                    }
                    request.setToken(accountOwner.getDeviceNotificationToken());
                    request.setRecipient(accountOwner.getPhoneNumber());
                    pushNotificationService.sendPushNotificationToToken(request);

//                    sendDebitEmailWalletToWallet(fundDTO, charges, vatFee, specificChannel, amountDebited, accountNumber, accountName, transDate, transTime, email, transactionAmount);
                    sendReversalEmail(fundDTO, amountDebited, accountNumber, transDate, transTime, email, accountOwner, displayMemo, Double.parseDouble(currentBalance1));


                } else if ("INVOICE".equalsIgnoreCase(channel)) {

                    request.setMessage("Request for the Sum of " + utility.formatMoney(amountDebited) + " from " + fundDTO.getSourceAccountName() + " has failed!");
                    request.setTitle("Request Money Failed");
                    request.setToken(accountOwner.getDeviceNotificationToken());
                    request.setRecipient(accountOwner.getPhoneNumber());
                    pushNotificationService.sendPushNotificationToToken(request);

                }
            }
        }
    }

    private void sendWalletToWalletReversalEmail(FundDTO fundDTO, Double amountCredited, String accountNumber, String accountName, String transDate, String transTime, String email, Profile profile, String displayMemo, double balance) {
        sendReversalEmail(fundDTO, amountCredited, accountNumber, transDate, transTime, email, profile, displayMemo, balance);
    }

    private void sendBankToWalletReversalEmail(FundDTO fundDTO, Double amountCredited, String accountNumber, String accountName, String transDate, String transTime, String email, Profile profile, String displayMemo, double balance) {

        sendReversalEmail(fundDTO, amountCredited, accountNumber, transDate, transTime, email, profile, displayMemo, balance);
    }

    private void sendReversalEmail(FundDTO fundDTO, Double amountCredited, String accountNumber, String transDate, String transTime, String email, Profile profile, String displayMemo, double balance) {
        User user = profile.getUser();
        if (user != null) {
            String firstName = user.getFirstName();

            String msg = "Dear " + firstName + ","
                + "<br/>"
                + "<br/>"
                + "Date : " + transDate
                + "<br/>"
                + "Time : " + transTime
                + "<p>"
                + "<p>"
                + "<p>"
                + "Amount: " + utility.formatMoney(amountCredited)
                + "<p>"
                + "A/C: " + utility.maskAccountNumber(accountNumber)
                + "<p>"
                + "Description: " + displayMemo
                + "<p>"
                + "Transaction reference: " + fundDTO.getTransRef()
                + "<p>"
                + "<b>"
                + "Available Balance:  : " + utility.formatMoney(balance)
                + "</b>"
                + "<p>";
            if (fundDTO.getBonusAmount() > 0) {
                msg = msg + "<p>"
                    + "<p>"
                    + "You have also been refunded " + utility.formatMoney(fundDTO.getBonusAmount()) + " bonus which has been added to your bonus pot.";
            }
            msg = msg
                + "<p>"
                + "<p>"
                + "<p>"
                + "<b>"
                + "<i>"
                + "You can also buy airtime, buy power, pay for TV subscription and pay other Billers with your wallet.</i></b>";

            if (utility.checkStringIsValid(email)) {
                utility.sendEmail(email, "POUCHII REVERSAL", msg);
            }

            if (utility.checkStringIsValid(email)) {
                utility.sendEmail(Kazeem, "POUCHII REVERSAL", msg);
            }

        }
    }

    private void sendBankToWalletCreditEmail(FundDTO fundDTO, Double amountCredited, String accountNumber, String accountName, String transDate, String transTime, String email) {

        String extraMessage = "Funding of " + utility.formatMoney(amountCredited) + " to wallet account " + fundDTO.getAccountNumber();

        String emailMessage = buildEmailBody(fundDTO, amountCredited, accountNumber, accountName, transDate, transTime, extraMessage, false, amountCredited);

        if (!StringUtils.isEmpty(email)) {
            utility.sendEmail(email, creditTitle, emailMessage);

        }

        if (!StringUtils.isEmpty(email)) {
            utility.sendEmail(Kazeem, creditTitle, emailMessage);
        }
    }

    private void sendBankToWalletCreditAppNotification(FundDTO fundDTO, Profile accountOwner, PushNotificationRequest request, Double amountCredited) {
        request.setMessage("Your Wallet (" + fundDTO.getAccountNumber() + ") has been credited with the sum of " + utility.formatMoney(amountCredited));
        request.setTitle("Fund Wallet");
        request.setToken(accountOwner.getDeviceNotificationToken());
        request.setRecipient(accountOwner.getPhoneNumber());
        pushNotificationService.sendPushNotificationToToken(request);
    }

    private void sendWalletToWalletCreditEmail(FundDTO fundDTO, Double amountCredited, String accountNumber, String accountName, String transDate, String transTime, String email) {
        String extraMessage = "Transfer of " + utility.formatMoney(amountCredited) + " to " + fundDTO.getBeneficiaryName() + " (" + fundDTO.getAccountNumber() + ")";
                          /*  if (charges > 0.00) {
                                extraMessage = extraMessage + " plus fees of " + utility.formatMoney(charges);
                            }*/


        if (SpecificChannel.LIBERTY.getName().equalsIgnoreCase(fundDTO.getSpecificChannel())) {
            extraMessage = "Transfer of " + utility.formatMoney(amountCredited) + " from Liberty to " + accountName + "(" + accountNumber + ")";
        }

        String emailMessage = buildEmailBody(fundDTO, zeroFee, accountNumber, accountName, transDate, transTime, extraMessage, false, amountCredited);

        if (!StringUtils.isEmpty(email)) {

            utility.sendEmail(email, creditTitle, emailMessage);

//                            utility.sendEmail(email, fundTransferSubject, sendMoneyCreditContent + " " + utility.formatMoney(line.getCredit()) + "\n" + description + narration + "\n" + reference + transRef);
        }
//                        utility.sendEmail(akinrindeEmail, fundTransferSubject, sendMoneyCreditContent + " " + utility.formatMoney(line.getCredit()) + "\n" + description + narration + "\n" + reference + transRef);
        if (!StringUtils.isEmpty(email)) {
            utility.sendEmail(Kazeem, creditTitle, emailMessage);
        }
    }

    private void sendWalletToWalletCreditAppNotification(FundDTO fundDTO, Profile accountOwner, PushNotificationRequest request, Double amountCredited) {

        request.setMessage("Your Wallet (" + fundDTO.getAccountNumber() + ") has been credited with the sum of " + utility.formatMoney(amountCredited));
        request.setTitle("Send Money");
        request.setToken(accountOwner.getDeviceNotificationToken());
        request.setRecipient(accountOwner.getPhoneNumber());
        pushNotificationService.sendPushNotificationToToken(request);
    }

    private String getBankName(FundDTO fundDTO) {
        String destBankCode = fundDTO.getDestBankCode();

        String bankName = "";

        Optional<Bank> bankOptional;
        if (StringUtils.isEmpty(destBankCode)) {
            bankOptional = bankService.findByBankCode(destBankCode);
            if (bankOptional.isPresent()) {
                bankName = bankOptional.get().getBankName();
            }
        }
        return bankName;
    }

    private void sendWalletToBankDebitAppNotification(FundDTO fundDTO, Profile accountOwner, Double charges, Double vatFee, PushNotificationRequest request, String bankName, Double transactionAmount) {
        //send Debit Notification
        request.setMessage("You have successfully sent the sum of " + utility.formatMoney(transactionAmount) + " to " + fundDTO.getBeneficiaryName() + " (" + fundDTO.getAccountNumber() + " - " + bankName + ")");
        if (charges + vatFee > 0) {
            request.setMessage(request.getMessage() + " plus fees of " + utility.formatMoney(charges + vatFee));
        }

        if (fundDTO.getBonusAmount() > 0) {
            request.setMessage(request.getMessage() + " and bonus point of " + utility.formatMoney(fundDTO.getBonusAmount()));
        }

        request.setTitle("Send money");
        request.setToken(accountOwner.getDeviceNotificationToken());
        request.setRecipient(accountOwner.getPhoneNumber());
        pushNotificationService.sendPushNotificationToToken(request);
    }

    private void sendWalletToBankDebitEmail(FundDTO fundDTO, Double charges, Double vatFee, Double amountDebited, String accountNumber, String accountName, String transDate, String transTime, String email, String bankName, Double transactionAmount) {

        String extraMessage = "Transfer of " + utility.formatMoney(transactionAmount) + " to " + fundDTO.getBeneficiaryName() + " (" + fundDTO.getAccountNumber() + "  " + bankName + ")";

        if (charges > 0.00) {

            extraMessage = extraMessage + " plus fees of " + utility.formatMoney(charges + vatFee);
        }

        String emailMessage = buildEmailBody(fundDTO, amountDebited, accountNumber, accountName, transDate, transTime, extraMessage, true, transactionAmount);

        if (!StringUtils.isEmpty(email)) {

            utility.sendEmail(email, debitTitle, emailMessage);
        }
        if (!StringUtils.isEmpty(email)) {
            utility.sendEmail(Kazeem, debitTitle, emailMessage);
        }
    }

    private void sendWalletToWalletDebitAppNotification(FundDTO fundDTO, Profile accountOwner, Double charges, Double vatFee, PushNotificationRequest request, String specificChannel, Double transactionAmount) {
        request.setMessage("You have successfully sent the Sum of " + utility.formatMoney(transactionAmount) + " to " + fundDTO.getBeneficiaryName());
        request.setTitle("Send Money");

        if (specificChannel.toLowerCase().contains("vtu")) {
            String message = "You have successfully purchased " + utility.formatMoney(transactionAmount) + " Airtime";
            if (StringUtils.isEmpty(fundDTO.getPhoneNumber())) {
                message = message + " for " + fundDTO.getPhoneNumber();
            }
            message = message + ". Thank you";
            request.setMessage(message);
            request.setTitle("Airtime Purchase");

        } else if (specificChannel.toLowerCase().contains("data")) {
            String message = "You have successfully purchased " + utility.formatMoney(transactionAmount) + " Data";
            if (StringUtils.isEmpty(fundDTO.getPhoneNumber())) {
                message = message + " for " + fundDTO.getPhoneNumber();
            }
            message = message + ". Thank you";
            request.setMessage(message);
            request.setTitle("Data Purchase");

        } else if (SpecificChannel.PAY_RRR.getName().equalsIgnoreCase(specificChannel)) {
            String message = "You have successfully paid RRR- " + fundDTO.getRrr() + ": " + utility.formatMoney(transactionAmount) + " plus fees of " + utility.formatMoney(charges + vatFee) + " to Biller. Thank You";
            request.setMessage(message);
            request.setTitle("RRR Payment");

        } else if (SpecificChannel.PAY_BILLER.getName().equalsIgnoreCase(specificChannel)) {
            String message = "You have successfully paid " + utility.formatMoney(transactionAmount) + " plus fees of " + utility.formatMoney(charges + vatFee) + " to Biller. Thank You";
            request.setMessage(message);
            request.setTitle("Biller Payment");

        } else if (SpecificChannel.PAY_ELECTRICITY.getName().equalsIgnoreCase(specificChannel)) {
            String message = "You have successfully made a purchase of " + utility.formatMoney(transactionAmount) + " plus fees of " + utility.formatMoney(charges + vatFee) + ". Thank you";
            request.setMessage(message);
            request.setTitle("Electricity Payment");

        } else if (specificChannel.toLowerCase().contains("disco")) {
            String message = "You have successfully made a purchase of " + utility.formatMoney(transactionAmount) + " electricity plus fees of " + utility.formatMoney(charges + vatFee) + ". Your electricity token is " + fundDTO.getShortComment() + ". Thank you.";
            request.setMessage(message);
            request.setTitle("Electricity Payment");

        } else if (specificChannel.toLowerCase().contains("cabletv")) {
            String message = "You have successfully made a cable tv subscription of " + utility.formatMoney(transactionAmount) + " plus fees of " + utility.formatMoney(charges + vatFee) + ". Thank you";
            request.setMessage(message);
            request.setTitle("Cable TV Subscription");

        } else if (SpecificChannel.PAY_CABLE_TV_ITEX.getName().equalsIgnoreCase(specificChannel)) {
            String message = "You have successfully made a cable tv subscription of " + utility.formatMoney(transactionAmount) + " plus fees of " + utility.formatMoney(charges + vatFee) + ". Thank you";
            request.setMessage(message);
            request.setTitle("Cable TV Subscription");

        } else if (specificChannel.toLowerCase().contains("internet")) {
            String message = "You have successfully made an internet subscription of " + utility.formatMoney(transactionAmount) + " plus fees of " + utility.formatMoney(charges + vatFee) + ". Thank you";
            request.setMessage(message);
            request.setTitle("Internet Subscription");

        } else if (SpecificChannel.PAY_BILLS.getName().equalsIgnoreCase(specificChannel)) {
            String message = "You have successfully made a purchase of " + utility.formatMoney(transactionAmount) + " plus fees of " + utility.formatMoney(charges + vatFee) + ". Thank you";
            request.setMessage(message);
            request.setTitle("Bills Payment");
        }

        if (fundDTO.getBonusAmount() > 0) {
            request.setMessage(request.getMessage() + " and bonus point of " + utility.formatMoney(fundDTO.getBonusAmount()));
        }

        request.setToken(accountOwner.getDeviceNotificationToken());
        request.setRecipient(accountOwner.getPhoneNumber());
        pushNotificationService.sendPushNotificationToToken(request);
    }

    private void sendDebitEmailWalletToWallet(FundDTO fundDTO, Double charges, Double vatFee, String specificChannel, Double amountDebited, String accountNumber, String accountName, String transDate, String transTime, String email, Double transactionAmount) {
        String extraMessage = "Transfer of " + utility.formatMoney(transactionAmount) + " to " + fundDTO.getBeneficiaryName() + " (" + fundDTO.getAccountNumber() + ")";

        if (specificChannel.toLowerCase().contains("vtu")) {
            extraMessage = "Purchase of " + utility.formatMoney(transactionAmount) + " Airtime";
            if (StringUtils.isEmpty(fundDTO.getPhoneNumber())) {
                extraMessage = extraMessage + " for " + fundDTO.getPhoneNumber();

            }
        } else if (specificChannel.toLowerCase().contains("data")) {
            extraMessage = "Purchase of " + utility.formatMoney(transactionAmount) + " Data";
            if (StringUtils.isEmpty(fundDTO.getPhoneNumber())) {
                extraMessage = extraMessage + " for " + fundDTO.getPhoneNumber();
            }

        } else if (SpecificChannel.PAY_RRR.getName().equalsIgnoreCase(specificChannel)) {
            extraMessage = "Payment of RRR- " + fundDTO.getRrr() + ": " + utility.formatMoney(transactionAmount) + " to Biller";

        } else if (SpecificChannel.PAY_BILLER.getName().equalsIgnoreCase(specificChannel)) {
            extraMessage = "Payment of  " + utility.formatMoney(transactionAmount) + " to Biller";

        } else if (SpecificChannel.PAY_ELECTRICITY.getName().equalsIgnoreCase(specificChannel)) {
            extraMessage = "Purchase of " + utility.formatMoney(transactionAmount) + " electricity";

        } else if (specificChannel.toLowerCase().contains("disco")) {
            extraMessage = "Purchase of " + utility.formatMoney(transactionAmount) + " electricity. Your electricity token is " + fundDTO.getShortComment();

        } else if (specificChannel.toLowerCase().contains("cabletv")) {
            extraMessage = "Purchase of " + utility.formatMoney(transactionAmount) + " cable tv subscription";

        } else if (SpecificChannel.PAY_CABLE_TV_ITEX.getName().equalsIgnoreCase(specificChannel)) {
            extraMessage = "Purchase of " + utility.formatMoney(transactionAmount) + " cable tv subscription";

        } else if (specificChannel.contains("internet")) {
            extraMessage = "Purchase of " + utility.formatMoney(transactionAmount) + " internet subscription";

        } else if (SpecificChannel.PAY_BILLS.getName().equalsIgnoreCase(specificChannel)) {
            extraMessage = "Purchase of " + utility.formatMoney(transactionAmount) + " bills";
        }

        if (charges > 0.00) {
            extraMessage = extraMessage + " plus fees of " + utility.formatMoney(charges + vatFee);
        }

        String emailMessage = buildEmailBody(fundDTO, amountDebited, accountNumber, accountName, transDate, transTime, extraMessage, true, transactionAmount);

        if (!StringUtils.isEmpty(email)) {
            utility.sendEmail(email, debitTitle, emailMessage);
//                            utility.sendEmail(email, fundTransferSubject, fundWalletDebitContent + " " + utility.formatMoney(line.getDebit()) + "\n" + description + narration + "\n" + reference + transRef);
        }
        if (!StringUtils.isEmpty(email)) {
            utility.sendEmail(email, debitTitle, emailMessage);
        }
    }

    private String buildEmailBody(FundDTO fundDTO, Double amountDebited, String accountNumber, String accountName, String transDate, String transTime, String extraMessage, boolean isDebit, Double transactionAmount) {

        String introMsg = "";

        if (!isDebit) {
            introMsg = "Your Wallet " + accountNumber + " (" + accountName + ")" + " has been credited with the sum of " + utility.formatMoney(transactionAmount);
        } else {

            introMsg = "Your Wallet " + accountNumber + " (" + accountName + ") has been debited with the sum of " + utility.formatMoney(amountDebited);

            if (fundDTO.getBonusAmount() > 0) {
                introMsg = introMsg + " and bonus point of " + utility.formatMoney(fundDTO.getBonusAmount());
            }

        }

        String msg = "Date : " + transDate
            + "<br/>"
            + "Time : " + transTime
            + "<p>"
            + "<p>"
            + "<p>"
            + introMsg
            + "<p>"
            + "<p>"
            + "<b>"
            + "Description : " + extraMessage
            + "</b>"
            + "<p>"
            + "<p>"
            + "Transaction reference : " + fundDTO.getTransRef();

        if (fundDTO.getBonusAmount() > 0) {
            msg = msg + "<p>"
                + "<p>"
                + "You have also been awarded " + utility.formatMoney(fundDTO.getBonusAmount()) + " bonus  which has been added to your bonus pot.";
        }
        return msg = msg
            + "<p>"
            + "<p>"
            + "<p>"
            + "<b>"
            + "<i>"
            + "You can also buy airtime, buy power, pay for TV subscription and pay other Billers with your wallet.</i></b>";


    }

    private void markTransactionReversed(String transRef) {
        if (transRef.endsWith("_1")) {

            String oldTransRef = transRef.substring(0, transRef.length() - 2);

            out.println("REVERSED FundDTO transRef == > " + oldTransRef);

            FundDTO byTransRef = transactionLogService.findByTransRef(oldTransRef);
            if (byTransRef != null) {
                byTransRef.setStatus(TransactionStatus.REVERSED);
                FundDTO save = transactionLogService.save(byTransRef);

                out.println("UPDATED FundDTO status reversed == > " + save);
            }

            Optional<Journal> byReference = journalService.findByReference(oldTransRef);
            byReference.ifPresent(journal -> {
                journal.setTransactionStatus(TransactionStatus.REVERSED);
                Journal save1 = journalService.save(journal);

                out.println("UPDATED Journal status reversed == > " + save1);

            });
        }
    }

    private String walletToWallet(FundDTO fundDTO) throws Exception {
        TransactionStatus status = setFundDTOStatus(fundDTO, TransactionStatus.START);

        String memo = "Transfer of " + utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount());
        ArrayList<JournalLineDTO> lines = new ArrayList<>();
        JournalLineDTO sourceAccountDebitLine = new JournalLineDTO();
        JournalLineDTO bonusPointDebitLine = new JournalLineDTO();

        out.printf(" The source account === %s destination === %s%n", fundDTO.getSourceAccountNumber(), fundDTO.getAccountNumber());
        WalletAccount debitAccount = walletAccountService.findOneByAccountNumber(fundDTO.getSourceAccountNumber());
        WalletAccount creditAccount = walletAccountService.findOneByAccountNumber(fundDTO.getAccountNumber());

        Double transactionFee;
        double charges = 0.0;
        String specificChannel = fundDTO.getSpecificChannel();
        if (utility.checkStringIsValid(String.valueOf(fundDTO.getCharges()))) {
            transactionFee = fundDTO.getCharges();
        } else {
            transactionFee = getTransactionFee(fundDTO.getAmount() + fundDTO.getBonusAmount(), specificChannel, fundDTO);
        }

        Double vatFee = getVATFee(transactionFee);
        if (SpecificChannel.POLARIS_CARD_REQUEST.getName().equalsIgnoreCase(specificChannel)) {
            vatFee = getVATFee(fundDTO.getAmount());
        }
        Double feeToDebit = transactionFee + vatFee;
        double partneringBankFee = calculatePolarisPartneringFee(feeToDebit); //Polaris commission payable wallet
        double polarisPomengranateFee = calculatePolarisPomengranateFee(feeToDebit); //Polaris pomengranate
        double ssIncludeVAT = calculateSSIncludeVAT(feeToDebit);
        double ssIncomeExcludingVAT = calculateSSIncomeExcludingVAT(ssIncludeVAT);
        double vatPayableFee = calculateVATPayable(ssIncomeExcludingVAT); //vat payable acct
        double billerCostofFUnds = calculateBillerCostOfFunds(transactionFee);
        double schemeBillerFeeCut = calculateSchemeBillerFeeCut(transactionFee);
        double schemeVat;

        double creditAmount = fundDTO.getAmount() + fundDTO.getBonusAmount();
        double overallTransactionAmount = fundDTO.getAmount() + feeToDebit;

        if(SpecificChannel.PAYMASTA_WALLET_TO_WALLET.getName().equalsIgnoreCase(specificChannel)
        || StringUtils.containsIgnoreCase(debitAccount.getScheme().getScheme(), "PayMasta")){
            if (utility.checkStringIsValid(String.valueOf(fundDTO.getCharges()))) {
                charges = fundDTO.getCharges();
                transactionFee = getTransactionFee(fundDTO.getAmount() + fundDTO.getBonusAmount(), specificChannel, fundDTO);
                vatFee = getVATFee(transactionFee);
                feeToDebit = transactionFee + vatFee;
                schemeVat = getVATFee(charges);
                overallTransactionAmount = charges + schemeVat + fundDTO.getAmount() + feeToDebit;
            }
        }

        if (debitAccount != null && creditAccount != null) {
            Double bonusPointAmount = calculateBonusPointEarned(transactionFee, specificChannel, debitAccount); //bonusPoint acct
            double introducerFee = calculateIntroducerFee(transactionFee); //Introducer acct

            double netIncome = ssIncomeExcludingVAT - bonusPointAmount - introducerFee; //pouchii income acct
            if (SpecificChannel.WALLENCY_SCHOOLS_FEES_PAYMENT.getName().equalsIgnoreCase(specificChannel)) {
                netIncome = getPouchiiSchoolFeesCommision(fundDTO);
            }

            if (SpecificChannel.PAYMASTA_WALLET_TO_WALLET.getName().equalsIgnoreCase(specificChannel)
                || SpecificChannel.FORGE_WALLET_TO_WALLET.getName().equalsIgnoreCase(specificChannel)
                || SpecificChannel.WYNK_WALLET_TO_WALLET.getName().equalsIgnoreCase(specificChannel)) {
                netIncome = transactionFee;
            }

            if (SpecificChannel.IBILE_PAY.getName().equalsIgnoreCase(specificChannel)) {
                sourceAccountDebitLine.setAccount(debitAccount);
                sourceAccountDebitLine.setCredit(zeroFee);
                sourceAccountDebitLine.setDebit(overallTransactionAmount);
                sourceAccountDebitLine.setChannel(fundDTO.getChannel());
                lines.add(sourceAccountDebitLine);

                JournalLineDTO transitAccountCreditLine = new JournalLineDTO();
                WalletAccount transitAccount = walletAccountService.findOneByAccountNumber(TRANSIT_ACCOUNT);
                transitAccountCreditLine.setAccount(transitAccount);
                transitAccountCreditLine.setCredit(overallTransactionAmount);
                transitAccountCreditLine.setDebit(zeroFee);
                lines.add(transitAccountCreditLine);

                status = setFundDTOStatus(fundDTO, TransactionStatus.INTRANSIT);

                boolean isSuccessful = true;

                if (fundDTO.getAccountNumber().equalsIgnoreCase(BILLER_PAYMENT_ACCOUNT)) {
                    isSuccessful = billerTransactionService.billNotification(fundDTO);
                }

                Scheme sourceAccountScheme = debitAccount.getScheme();
                Scheme destinationAccountScheme = creditAccount.getScheme();

                if (sourceAccountScheme != null && destinationAccountScheme != null
                    && !sourceAccountScheme.equals(destinationAccountScheme)
                ) {
                    GenericResponseDTO responseDTO = polarisSendMoneyToOtherPoolAcct(fundDTO);
                    if (!HttpStatus.OK.equals(responseDTO.getStatus())) {
                        isSuccessful = false;
                    }
                }

                if (isSuccessful) {
                    JournalLineDTO transitAccountDeditLine = new JournalLineDTO();
                    transitAccountDeditLine.setAccount(transitAccount);
                    transitAccountDeditLine.setDebit(overallTransactionAmount);
                    transitAccountDeditLine.setCredit(zeroFee);
                    transitAccountDeditLine.setChannel(specificChannel);
                    lines.add(transitAccountDeditLine);

                    status = TransactionStatus.INCOMPLETE;

                    JournalLineDTO destinationAccountCreditLine = new JournalLineDTO();
                    destinationAccountCreditLine.setAccount(creditAccount);
                    destinationAccountCreditLine.setCredit(creditAmount);
                    destinationAccountCreditLine.setDebit(zeroFee);
                    lines.add(destinationAccountCreditLine); //Add credit lines

                    JournalLineDTO feeAccountCreditLine = new JournalLineDTO();
                    WalletAccount chargesAccount = walletAccountService.findOneByAccountNumber(POUCHII_INCOME_ACCT);
                    feeAccountCreditLine.setCredit(transactionFee);
                    feeAccountCreditLine.setAccount(chargesAccount);
                    feeAccountCreditLine.setDebit(zeroFee);
                    lines.add(feeAccountCreditLine);

                    if (vatFee > 0.0) {
                        JournalLineDTO vatAccountCreditLine = new JournalLineDTO();
                        WalletAccount vatAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
                        vatAccountCreditLine.setAccount(vatAccount);
                        vatAccountCreditLine.setCredit(vatFee);
                        vatAccountCreditLine.setDebit(zeroFee);
                        lines.add(vatAccountCreditLine);
                    }

                    memo = getMemoForWalletToWallet(fundDTO, memo, fundDTO.getSourceAccountName(), fundDTO.getBeneficiaryName());
                    memo = String.format("%s. Payment Transaction Ref : %s", memo, fundDTO.getTransRef());

                    status = TransactionStatus.COMPLETED;
                    status = setFundDTOStatus(fundDTO, status);

                    if ("INVOICE".equalsIgnoreCase(fundDTO.getChannel())) {
                        status = TransactionStatus.INCOMPLETE;
                        status = setFundDTOStatus(fundDTO, status);
                        fundDTO.setBulkTrans(false);
                    }
                    String doubleEntryReturn = doubleEntry(lines, memo, transactionFee, status, fundDTO, vatFee);
                    try {
                        transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return doubleEntryReturn;
                }

                memo = getMemoForWalletToWallet(fundDTO, memo, fundDTO.getSourceAccountName(), fundDTO.getBeneficiaryName());

                memo = memo + ". Payment Transaction Ref : " + fundDTO.getTransRef();

                JournalLineDTO debitAccountReversalLine = new JournalLineDTO();
                debitAccountReversalLine.setAccount(debitAccount);
                debitAccountReversalLine.setCredit(overallTransactionAmount);
                debitAccountReversalLine.setDebit(zeroFee);
                debitAccountReversalLine.setChannel(fundDTO.getChannel());

                JournalLineDTO transitAccountReversalLine = new JournalLineDTO();
                transitAccountReversalLine.setAccount(transitAccount);
                transitAccountReversalLine.setCredit(zeroFee);
                transitAccountReversalLine.setDebit(overallTransactionAmount);

                lines.add(sourceAccountDebitLine);
                lines.add(transitAccountCreditLine);
                lines.add(debitAccountReversalLine);
                lines.add(transitAccountReversalLine);

                status = TransactionStatus.REVERSED;
                status = setFundDTOStatus(fundDTO, status);

                String doubleEntryReturn = doubleEntry(lines, memo, transactionFee, status, fundDTO, vatFee);
                try {
                    transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return doubleEntryReturn;
            } //End of ibile transaction
            else {
                sourceAccountDebitLine.setAccount(debitAccount);
                sourceAccountDebitLine.setCredit(zeroFee);

                sourceAccountDebitLine.setDebit(overallTransactionAmount);
                sourceAccountDebitLine.setChannel(fundDTO.getChannel());
                lines.add(sourceAccountDebitLine);

                Profile debitAccountOwner = debitAccount.getAccountOwner();
                WalletAccount bonusPointAccount = null;

                if (debitAccountOwner != null) { //debit customer bonus pot
                    bonusPointAccount = walletAccountService.getCustomerBonusAccount(debitAccountOwner);
                    out.println("Retrieved bonusPointWalletAccount ===> " + bonusPointAccount);

                    if (fundDTO.getBonusAmount() > 0) {
                        bonusPointDebitLine.setAccount(bonusPointAccount);
                        bonusPointDebitLine.setDebit(fundDTO.getBonusAmount());
                        bonusPointDebitLine.setCredit(zeroFee);
                        lines.add(bonusPointDebitLine);
                    }
                }

                //Debit transit account
                JournalLineDTO transitAccountCreditLine = new JournalLineDTO();
                WalletAccount transitAccount = walletAccountService.findOneByAccountNumber(TRANSIT_ACCOUNT);
                transitAccountCreditLine.setAccount(transitAccount);
                transitAccountCreditLine.setCredit(overallTransactionAmount);
                transitAccountCreditLine.setDebit(zeroFee);
                lines.add(transitAccountCreditLine);

                status = setFundDTOStatus(fundDTO, TransactionStatus.INTRANSIT);
                boolean isSuccessful = true;

                if (fundDTO.getAccountNumber().equalsIgnoreCase(BILLER_PAYMENT_ACCOUNT)) {
                    isSuccessful = billerTransactionService.billNotification(fundDTO);
                }

                if (isSuccessful) {
                    //Credit back transit account
                    JournalLineDTO transitAccountDebitLine = new JournalLineDTO();
                    transitAccountDebitLine.setAccount(transitAccount);
                    transitAccountDebitLine.setDebit(overallTransactionAmount);
                    transitAccountDebitLine.setCredit(zeroFee);
                    transitAccountDebitLine.setChannel(specificChannel);
                    lines.add(transitAccountDebitLine);

                    status = TransactionStatus.INCOMPLETE;

                    //credit the destination  account
                    JournalLineDTO destinationAccountCreditLine = new JournalLineDTO();
                    destinationAccountCreditLine.setAccount(creditAccount);
                    destinationAccountCreditLine.setCredit(creditAmount);
                    destinationAccountCreditLine.setDebit(zeroFee);

                    //get Pouchii commision from Itex transaction
                    Double commission = getCommission(specificChannel, creditAmount, fundDTO.getAccountNumber());
                    if (commission > 0) {
                        JournalLineDTO itexAccountCreditLine = new JournalLineDTO();
                        WalletAccount chargesAccount = walletAccountService.findOneByAccountNumber(ITEX_COMMISSION_ACCT);
                        itexAccountCreditLine.setAccount(chargesAccount);
                        itexAccountCreditLine.setCredit(commission);
                        itexAccountCreditLine.setDebit(zeroFee);
                        lines.add(itexAccountCreditLine);

                        if (StringUtils.containsIgnoreCase(debitAccount.getScheme().getScheme(), "PayMasta")
                            || StringUtils.containsIgnoreCase(debitAccount.getScheme().getScheme(), "Forge")
                            || StringUtils.containsIgnoreCase(debitAccount.getScheme().getScheme(), "Wynk")) {
                            JournalLineDTO schemeCreditLine = new JournalLineDTO();
                            chargesAccount =  walletAccountService.findOneByAccountNumber(PAYMASTA_INCOME_ACCT);
                            if(StringUtils.containsIgnoreCase(debitAccount.getScheme().getScheme(), "Forge"))chargesAccount =  walletAccountService.findOneByAccountNumber(FORGE_INCOME_ACCT);
                            if(StringUtils.containsIgnoreCase(debitAccount.getScheme().getScheme(), "Wynk"))chargesAccount =  walletAccountService.findOneByAccountNumber(WYNK_INCOME_ACCT);
                            schemeCreditLine.setAccount(chargesAccount);
                            schemeCreditLine.setCredit(schemeBillerFeeCut);
                            schemeCreditLine.setDebit(zeroFee);
                            lines.add(schemeCreditLine);

                            JournalLineDTO billerCostOfFundsCreditLine = new JournalLineDTO();
                            chargesAccount = walletAccountService.findOneByAccountNumber(BILLER_COST_OF_FUNDS);
                            billerCostOfFundsCreditLine.setAccount(chargesAccount);
                            billerCostOfFundsCreditLine.setCredit(billerCostofFUnds);
                            billerCostOfFundsCreditLine.setDebit(zeroFee);
                            lines.add(billerCostOfFundsCreditLine);

                            if (!specificChannel.toLowerCase().contains("vtu")) netIncome = 0.25 * transactionFee;

                        }

                        if(StringUtils.containsIgnoreCase(debitAccount.getScheme().getScheme(), "PayMasta")){
                            chargesAccount = walletAccountService.findOneByAccountNumber(PAYMASTA_COMMISSIONS_ACCT);
                            JournalLineDTO schemeCommissionLine = new JournalLineDTO();
                            schemeCommissionLine.setAccount(chargesAccount);
                            schemeCommissionLine.setDebit(zeroFee);
                            schemeCommissionLine.setCredit(charges);
                            lines.add(schemeCommissionLine);

                            schemeVat = getVATFee(charges);

                            chargesAccount = walletAccountService.findOneByAccountNumber(PAYMASTA_VAT_ACCT);
                            JournalLineDTO schemeVATLine = new JournalLineDTO();
                            schemeVATLine.setAccount(chargesAccount);
                            schemeVATLine.setDebit(zeroFee);
                            schemeVATLine.setCredit(schemeVat);
                            lines.add(schemeVATLine);
                        }
                        //Subtract commission from creditAmount
                        destinationAccountCreditLine.setCredit(creditAmount - commission);

                    }

                    if(SpecificChannel.PAYMASTA_WALLET_TO_WALLET.getName().equalsIgnoreCase(specificChannel)){
                        WalletAccount chargesAccount = walletAccountService.findOneByAccountNumber(PAYMASTA_COMMISSIONS_ACCT);
                        JournalLineDTO schemeCommissionLine = new JournalLineDTO();
                        schemeCommissionLine.setAccount(chargesAccount);
                        schemeCommissionLine.setDebit(zeroFee);
                        schemeCommissionLine.setCredit(charges);
                        lines.add(schemeCommissionLine);

                        schemeVat = getVATFee(charges);

                        chargesAccount = walletAccountService.findOneByAccountNumber(PAYMASTA_VAT_ACCT);
                        JournalLineDTO schemeVATLine = new JournalLineDTO();
                        schemeVATLine.setAccount(chargesAccount);
                        schemeVATLine.setDebit(zeroFee);
                        schemeVATLine.setCredit(schemeVat);
                        lines.add(schemeVATLine);

                    }

                    if (SpecificChannel.INSURANCE.getName().equalsIgnoreCase(specificChannel)) {
                        //calculate pouchii commision from mutual benefits transaction
                        double insuranceCommission = creditAmount * mutualBenefitPercentage;
                        WalletAccount chargesAccount = walletAccountService.findOneByAccountNumber(INSURANCE_COMMISSION_ACCT);
                        JournalLineDTO insuranceAccountCreditlIne = new JournalLineDTO();
                        insuranceAccountCreditlIne.setAccount(chargesAccount);
                        insuranceAccountCreditlIne.setDebit(zeroFee);
                        insuranceAccountCreditlIne.setCredit(insuranceCommission);
                        lines.add(insuranceAccountCreditlIne);

                        //Subtract commission from credit amount
                        destinationAccountCreditLine.setCredit(creditAmount - insuranceCommission);
                    }

                    //Add destinationAccountCreditLine to credit lines
                    lines.add(destinationAccountCreditLine);

                    JournalLineDTO polarisCommissionCreditLine = new JournalLineDTO();
                    WalletAccount polarisCommissionAccount = walletAccountService.findOneByAccountNumber(POLARIS_COMMISSION_ACCT);
                    polarisCommissionCreditLine.setAccount(polarisCommissionAccount);
                    polarisCommissionCreditLine.setCredit(zeroFee);
                    polarisCommissionCreditLine.setDebit(zeroFee);
                    lines.add(polarisCommissionCreditLine);

                    JournalLineDTO polarisPomengranateCreditLine = new JournalLineDTO();
                    WalletAccount polarisPomengranateAccount =
                        walletAccountService.findOneByAccountNumber(POLARIS_POMENGRANATE_COMMISSION_ACCT);
                    polarisPomengranateCreditLine.setAccount(polarisPomengranateAccount);
                    polarisPomengranateCreditLine.setCredit(zeroFee);
                    polarisPomengranateCreditLine.setDebit(zeroFee);
                    lines.add(polarisPomengranateCreditLine);

                    if (vatFee > 0.0) {
                        JournalLineDTO vatPayableAccountCreditLine = new JournalLineDTO();
                        WalletAccount vatAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
                        vatPayableAccountCreditLine.setAccount(vatAccount);
                        vatPayableAccountCreditLine.setCredit(vatFee);
                        vatPayableAccountCreditLine.setDebit(zeroFee);
                        lines.add(vatPayableAccountCreditLine);
                    }

                    out.println("Bonus Point Amount ====> " + bonusPointAmount);

                    if (bonusPointAmount > 0) { //Add bonus point amount
                        JournalLineDTO bonusPointCreditLine = new JournalLineDTO();
                        bonusPointCreditLine.setAccount(bonusPointAccount);
                        bonusPointCreditLine.setCredit(bonusPointAmount);
                        bonusPointCreditLine.setDebit(zeroFee);
                        lines.add(bonusPointCreditLine);
                    }

                    JournalLineDTO introducerCreditLine = new JournalLineDTO();
                    WalletAccount introducerAccount = walletAccountService.findOneByAccountNumber(INTRODUCER_ACCT);
                    introducerCreditLine.setAccount(introducerAccount);
                    introducerCreditLine.setCredit(introducerFee);
                    introducerCreditLine.setDebit(zeroFee);
                    lines.add(introducerCreditLine);

                    JournalLineDTO feeAccountCreditLine = new JournalLineDTO();
                    WalletAccount chargesAccount = walletAccountService.findOneByAccountNumber(POUCHII_INCOME_ACCT);
                    feeAccountCreditLine.setCredit(netIncome);

                    if (SpecificChannel.LIBERTY.getName().equalsIgnoreCase(specificChannel)) {
                        chargesAccount = walletAccountService.findOneByAccountNumber(LENDING_DISBURSEMENT_INCOME_ACCT);
                        feeAccountCreditLine.setCredit(transactionFee);
                    }
                    feeAccountCreditLine.setAccount(chargesAccount);
                    feeAccountCreditLine.setDebit(zeroFee);
                    lines.add(feeAccountCreditLine);

                    //adding bonus Point to Profile
                    if (debitAccountOwner != null) {
                        double totalBonus = debitAccountOwner.getTotalBonus();
                        out.println("current total Bonus ===> " + totalBonus);

                        totalBonus = totalBonus - fundDTO.getBonusAmount() + bonusPointAmount;

                        out.println("Updated total Bonus ===> " + totalBonus);

                        debitAccountOwner.setTotalBonus(totalBonus);
                        Profile savedProfile = profileService.save(debitAccountOwner);
                        out.println(" The saved profile \n\n\n\n\n\n\n\n====Profile==" + savedProfile);
                    }

                    memo = getMemoForWalletToWallet(fundDTO, memo, fundDTO.getSourceAccountName(), fundDTO.getBeneficiaryName());
                    memo = String.format("%s. Payment Transaction Ref : %s", memo, fundDTO.getTransRef());

                    status = TransactionStatus.COMPLETED;
                    status = setFundDTOStatus(fundDTO, status);

                    if ("INVOICE".equalsIgnoreCase(fundDTO.getChannel())) {
                        status = TransactionStatus.INCOMPLETE;
                        status = setFundDTOStatus(fundDTO, status);
                        fundDTO.setBulkTrans(false);
                    }
                    String doubleEntryReturn = doubleEntry(lines, memo, transactionFee, status, fundDTO, vatFee);
                    try {
                        transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return doubleEntryReturn;
                }

                memo = getMemoForWalletToWallet(fundDTO, memo, fundDTO.getSourceAccountName(), fundDTO.getBeneficiaryName());

                memo = memo + ". Payment Transaction Ref : " + fundDTO.getTransRef();

                JournalLineDTO sourceAccountReversalLine = new JournalLineDTO();
                sourceAccountReversalLine.setAccount(debitAccount);
                sourceAccountReversalLine.setCredit(overallTransactionAmount);
                sourceAccountReversalLine.setDebit(zeroFee);
                sourceAccountReversalLine.setChannel(fundDTO.getChannel());

                JournalLineDTO transitAccountReversalLine = new JournalLineDTO();
                transitAccountReversalLine.setAccount(transitAccount);
                transitAccountReversalLine.setCredit(zeroFee);
                transitAccountReversalLine.setDebit(overallTransactionAmount);

                if (fundDTO.getBonusAmount() > 0) {
                    JournalLineDTO journalLine9DTO = new JournalLineDTO();
                    journalLine9DTO.setCredit(fundDTO.getBonusAmount());
                    journalLine9DTO.setDebit(zeroFee);
                    journalLine9DTO.setAccount(bonusPointAccount);

                    lines.add(journalLine9DTO);
                }

                lines.add(sourceAccountDebitLine);
                lines.add(transitAccountCreditLine);
                lines.add(sourceAccountReversalLine);
                lines.add(transitAccountReversalLine);
                status = TransactionStatus.REVERSED;
                status = setFundDTOStatus(fundDTO, status);


                String doubleEntryReturn = doubleEntry(lines, memo, transactionFee, status, fundDTO, vatFee);
                try {
                    transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return doubleEntryReturn;
            }
        }

        return "Source Account or Debit Account cannot be null";
    }

    private WalletAccount getSchemePartnerWalletAccount(String sourceAccountNumber) {
        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(sourceAccountNumber);
        Scheme scheme = walletAccount.getScheme();
        List<WalletAccountDTO> operationalWallets = walletAccountService.findAllBySchemeAndAccountOwnerIsNull(scheme);
        for (WalletAccountDTO w : operationalWallets) {
            if (StringUtils.containsIgnoreCase(w.getAccountName(), "Income")) return walletAccountMapper.toEntity(w);
        }
        return null;
    }

    private double calculateBillerCostOfFunds(Double transactionFee) {
        return transactionFee * 0.5;
    }

    private double calculateSchemeBillerFeeCut(Double transactionFee) {
        return transactionFee * 0.25;
    }

    private double calculateBillerFeeIncome(Double transactionFee) {
        return transactionFee * 0.25;
    }


    private String walletToWallets(FundDTO fundDTO) {
        out.println("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        out.println("Wallet to Wallets FundDTO===>   " + fundDTO);
        out.println("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        TransactionStatus status = setFundDTOStatus(fundDTO, TransactionStatus.START);

        String memo = "Transfer of " + utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount());
        ArrayList<JournalLineDTO> lines = new ArrayList<>();
        Set<JournalLineDTO> nubanEntryLines = new LinkedHashSet<>();
        JournalLineDTO journalLine1DTO = new JournalLineDTO();
        JournalLineDTO journalLine8DTO = new JournalLineDTO();

        Double intraFee;

        String specificChannel = fundDTO.getSpecificChannel();
        if (utility.checkStringIsValid(String.valueOf(fundDTO.getCharges()))) {
            intraFee = fundDTO.getCharges();
        } else {
            intraFee = getTransactionFee(fundDTO.getAmount() + fundDTO.getBonusAmount(), specificChannel, fundDTO);
        }

        Double vatFee = getVATFee(intraFee);

        out.printf(" The source account === %s destination === %s%n", fundDTO.getSourceAccountNumber(), fundDTO.getAccountNumber());
        WalletAccount debitAccount = walletAccountService.findOneByAccountNumber(fundDTO.getSourceAccountNumber());
//        WalletAccount creditAccount = walletAccountService.findOneByAccountNumber(fundDTO.getAccountNumber());

        if (debitAccount != null) {
            journalLine1DTO.setAccount(debitAccount);
            journalLine1DTO.setCredit(zeroFee);

            double overallTransactionAmount = fundDTO.getAmount() + intraFee + vatFee;
            journalLine1DTO.setDebit(overallTransactionAmount);
            Profile debitAccountOwner = debitAccount.getAccountOwner();
            WalletAccount bonusPointAccount = null;

            JournalLineDTO customerNubanDebitLine = new JournalLineDTO();
            customerNubanDebitLine.setCredit(zeroFee);
            customerNubanDebitLine.setDebit(overallTransactionAmount);
            customerNubanDebitLine.setAccount(debitAccount);


            journalLine1DTO.setChannel(fundDTO.getChannel());

            if (debitAccountOwner != null) {
                bonusPointAccount = walletAccountService.getCustomerBonusAccount(debitAccountOwner);
                out.println("Retrieved bonusPointWalletAccount ===> " + bonusPointAccount);

                if (fundDTO.getBonusAmount() > 0) {
                    journalLine8DTO.setAccount(bonusPointAccount);
                    journalLine8DTO.setDebit(fundDTO.getBonusAmount());
                    journalLine8DTO.setCredit(zeroFee);
                    lines.add(journalLine8DTO);
                    customerNubanDebitLine.setDebit(customerNubanDebitLine.getDebit() + fundDTO.getBonusAmount());
                }
            }

            JournalLineDTO journalLine4DTO = new JournalLineDTO();
            WalletAccount transitAccount = walletAccountService.findOneByAccountNumber(TRANSIT_ACCOUNT);
            journalLine4DTO.setAccount(transitAccount);
            journalLine4DTO.setCredit(overallTransactionAmount);
            journalLine4DTO.setDebit(zeroFee);

            status = setFundDTOStatus(fundDTO, TransactionStatus.INTRANSIT);

            boolean isSuccessful = true;

            if (isSuccessful) {

                JournalLineDTO journalLine5DTO = new JournalLineDTO();
                journalLine5DTO.setAccount(transitAccount);
                journalLine5DTO.setDebit(overallTransactionAmount);
                journalLine5DTO.setCredit(zeroFee);
                journalLine5DTO.setChannel(specificChannel);

                status = TransactionStatus.INCOMPLETE;

                for (BulkBeneficiaryDTO b : fundDTO.getBulkAccountNos()) {
                    WalletAccount w = walletAccountService.findOneByAccountNumber(b.getAccountNumber());
                    double creditAmount = b.getAmount();
                    JournalLineDTO journalLine2DTO = new JournalLineDTO();
                    journalLine2DTO.setAccount(w);
                    journalLine2DTO.setCredit(creditAmount);
                    journalLine2DTO.setDebit(zeroFee);
                    lines.add(journalLine2DTO);
//                    nubanEntryLines.add(journalLine2DTO);
                }

                double bonusPointAmount = calculateBonusPointEarned(intraFee, specificChannel, debitAccount);

                out.println("Bonus Point Amount ====> " + bonusPointAmount);

                if (bonusPointAmount > 0) {
                    JournalLineDTO bonusPointJournalLine = new JournalLineDTO();
                    bonusPointJournalLine.setAccount(bonusPointAccount);
                    bonusPointJournalLine.setCredit(bonusPointAmount);
                    bonusPointJournalLine.setDebit(zeroFee);
                    lines.add(bonusPointJournalLine);

                    customerNubanDebitLine.setDebit(customerNubanDebitLine.getDebit() - bonusPointAmount);
                }

                nubanEntryLines.add(customerNubanDebitLine);

                JournalLineDTO journalLine3DTO = new JournalLineDTO();
                WalletAccount chargesAccount = walletAccountService.findOneByAccountNumber(POUCHII_INCOME_ACCT);
                journalLine3DTO.setCredit(intraFee - bonusPointAmount);

                journalLine3DTO.setAccount(chargesAccount);
                journalLine3DTO.setDebit(zeroFee);

                nubanEntryLines.add(journalLine3DTO);

                if (vatFee > 0.0) {
                    JournalLineDTO journalLine15DTO = new JournalLineDTO();
                    WalletAccount vatAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
                    journalLine15DTO.setAccount(vatAccount);
                    journalLine15DTO.setCredit(vatFee);
                    journalLine15DTO.setDebit(zeroFee);
                    lines.add(journalLine15DTO);

                    nubanEntryLines.add(journalLine15DTO);
                }

                lines.add(journalLine3DTO);
                lines.add(journalLine5DTO);

                lines.add(journalLine1DTO);
                lines.add(journalLine4DTO);

                //adding bonus Point to Profile
                if (debitAccountOwner != null) {
                    double totalBonus = debitAccountOwner.getTotalBonus();
                    out.println("current total Bonus ===> " + totalBonus);

                    totalBonus = totalBonus - fundDTO.getBonusAmount() + bonusPointAmount;

                    out.println("Updated total Bonus ===> " + totalBonus);

                    debitAccountOwner.setTotalBonus(totalBonus);
                    Profile savedProfile = profileService.save(debitAccountOwner);
                    out.println(" The saved profile \n\n\n\n\n\n\n\n====Profile==" + savedProfile);
                }

                memo = getMemoForWalletToWallet(fundDTO, memo, fundDTO.getSourceAccountName(), fundDTO.getBeneficiaryName());
                memo = String.format("%s. Payment Transaction Ref : %s", memo, fundDTO.getTransRef());

                status = TransactionStatus.COMPLETED;
                status = setFundDTOStatus(fundDTO, status);

                if ("INVOICE".equalsIgnoreCase(fundDTO.getChannel())) {
                    status = TransactionStatus.INCOMPLETE;
                    status = setFundDTOStatus(fundDTO, status);

                }

                String doubleEntryReturn = doubleEntry(lines, memo, intraFee, status, fundDTO, vatFee);
                try {
                    transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return doubleEntryReturn;
            }

            memo = getMemoForWalletToWallet(fundDTO, memo, fundDTO.getSourceAccountName(), fundDTO.getBeneficiaryName());

            memo = memo + ". Payment Transaction Ref : " + fundDTO.getTransRef();

            JournalLineDTO journalLine6DTO = new JournalLineDTO();
            journalLine6DTO.setAccount(debitAccount);
            journalLine6DTO.setCredit(overallTransactionAmount);
            journalLine6DTO.setDebit(zeroFee);
            journalLine6DTO.setChannel(fundDTO.getChannel());

            JournalLineDTO journalLine7DTO = new JournalLineDTO();
            journalLine7DTO.setAccount(transitAccount);
            journalLine7DTO.setCredit(zeroFee);
            journalLine7DTO.setDebit(overallTransactionAmount);

            if (fundDTO.getBonusAmount() > 0) {

                JournalLineDTO journalLine9DTO = new JournalLineDTO();
                journalLine9DTO.setCredit(fundDTO.getBonusAmount());
                journalLine9DTO.setDebit(zeroFee);
                journalLine9DTO.setAccount(bonusPointAccount);

                lines.add(journalLine9DTO);
            }

            lines.add(journalLine1DTO);
            lines.add(journalLine4DTO);

            lines.add(journalLine6DTO);
            lines.add(journalLine7DTO);

            status = TransactionStatus.REVERSED;
            status = setFundDTOStatus(fundDTO, status);


            String doubleEntryReturn = doubleEntry(lines, memo, intraFee, status, fundDTO, vatFee);
            try {
                transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return doubleEntryReturn;
        }

        return "Source Account or Debit Account cannot be null";
    }

    private void balanceNubanAccounts(Set<JournalLineDTO> nubanEntryLines, String narration) {
        out.println("balanceNubanAccounts params : journalLine1DTO = " + " nubanEntryLines = " + nubanEntryLines + " narration " + narration);
        nubanEntryLines
            .stream()
            .filter(journalLineDTO -> journalLineDTO.getDebit() > 0)
            .forEach(journalLineDTO -> {
                try {
                    out.println("balanceNubanAccounts Debit: current journalLineDTO " + journalLineDTO);
                    WalletAccount sourceAccount = journalLineDTO.getAccount();
                    out.println("balanceNubanAccounts Debit: current journalLineDTO source walletAccount " + sourceAccount);
                    if (sourceAccount != null) {
                        String sourceAccountNubanNo;
                        if (utility.checkStringIsValid(sourceAccount.getNubanAccountNo())) {
                            sourceAccountNubanNo = sourceAccount.getNubanAccountNo();
                        } else {
                            Optional<String> walletAccountOptional = utility.getWalletAccountNubanByAccountNumber(sourceAccount.getAccountNumber());
                            out.println("balanceNubanAccounts Debit: walletAccountOptional " + walletAccountOptional);
                            sourceAccountNubanNo = walletAccountOptional.orElse("null");
                        }
                        out.println("balanceNubanAccounts  Debit: Wallet Accounts, SourceAccountNubanNo = " + sourceAccountNubanNo + "DestinationAccount " + CASHCONNECT_VIRTUAL_ACCOUNT_NUMBER);

                        out.println("Inside the balanceNubanAccounts executor ==> ");
                        GenericResponseDTO genericResponseDTO = cashConnectService.intraTransfer(
                            sourceAccountNubanNo, CASHCONNECT_VIRTUAL_ACCOUNT_NUMBER, "Systemspecs Virtual Account",
                            journalLineDTO.getDebit(), narration);

                        out.println("balanceNubanAccounts Response balanceNubanAccounts  Debit " + genericResponseDTO);
                    } else {

                        out.println("balanceNubanAccounts Debit Failed because source Account is null");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("balanceNubanAccounts debit error occur " + e.getLocalizedMessage());
                }

            });

        nubanEntryLines
            .stream()
            .filter(journalLineDTO -> journalLineDTO.getCredit() > 0)
            .forEach(journalLineDTO -> {
                out.println("balanceNubanAccounts Credit: current journalLineDTO " + journalLineDTO);
                try {
                    WalletAccount destinationAccount = journalLineDTO.getAccount();
                    out.println("balanceNubanAccounts Credit: current journalLineDTO destination WalletAccount " + destinationAccount);
                    if (destinationAccount != null) {
                        String destinationAccountNo;
                        if (utility.checkStringIsValid(destinationAccount.getNubanAccountNo())) {
                            destinationAccountNo = destinationAccount.getNubanAccountNo();
                        } else {
                            Optional<String> nubanOptional =
                                utility.getWalletAccountNubanByAccountNumber(destinationAccount.getAccountNumber());
                            out.println("balanceNubanAccounts Credit: nubanOptional " + nubanOptional);
                            destinationAccountNo = nubanOptional.orElse(null);
                        }
                        out.println("balanceNubanAccounts Credit: destinationAccountNo " + destinationAccountNo);
                        if (utility.checkStringIsNotValid(destinationAccountNo)) {
                            out.println("Could not retrieve destination account number " + destinationAccountNo);
                            throw new Exception("Could not retrieve destination account number");
                        }
                        out.println("balanceNubanAccounts  Credit: Wallet Accounts, destinationAccountNo = " + destinationAccountNo + "sourceAccountNo " + CASHCONNECT_VIRTUAL_ACCOUNT_NUMBER);
                        out.println("Inside the balanceNubanAccounts executor ==> ");
                        GenericResponseDTO genericResponseDTO = cashConnectService.intraTransfer(
                            CASHCONNECT_VIRTUAL_ACCOUNT_NUMBER, destinationAccountNo, destinationAccount.getAccountName(),
                            journalLineDTO.getCredit(), narration);
                        out.println("balanceNubanAccounts Response balanceNubanAccounts Credit " + genericResponseDTO);

                        if (genericResponseDTO.getStatus().isError()) {
                            utility.sendEmail(GMAIL_COM, "Virtual account debit failure",
                                "  journalLineDTO ===>" + journalLineDTO + " <br/>" + "  Amount ===>" + journalLineDTO.getCredit() + " " +
                                    "<br/>" + "  narration ===>" + narration + " " +
                                    "<br/>" + "  genericResponseDTO ===>" + genericResponseDTO + " " +
                                    "<br/>" + "  destinationAccount ===>" + destinationAccount + " " +
                                    "<br/>"
                            );
                        }

                    } else {
                        out.println("balanceNubanAccounts Credit Failed because source Account is null");
                        utility.sendEmail(GMAIL_COM, "Virtual account debit failure",
                            "  journalLineDTO ===>" + journalLineDTO + " <br/>" + "  Amount ===>" + journalLineDTO.getCredit() + " " +
                                "<br/>" + "  narration ===>" + narration + " " +
                                "<br/>" + "  destinationAccount ===>" + destinationAccount + " " +
                                "<br/>"
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("balanceNubanAccounts credit occur " + e.getLocalizedMessage());
                    utility.sendEmail(GMAIL_COM, "Virtual account debit failure",
                        "  journalLineDTO ===>" + journalLineDTO + " <br/>" + "  Amount ===>" + journalLineDTO.getCredit() + " " +
                            "<br/>" + "  narration ===>" + narration + " " +
                            "<br/>" + "  Exception ===>" + e.getMessage() + " " +
                            "<br/>"
                    );
                }

            });
    }

    private Double getCommission(String specificChannel, double creditAmount, String accountNumber) {

        if (SpecificChannel.PAY_AEDC_ELECTRICITY_ITEX.getName().equals(specificChannel)) {
            return creditAmount * aedcCommission;

        } else if (SpecificChannel.PAY_EKEDC_ELECTRICITY_ITEX.getName().equals(specificChannel)) {
            return creditAmount * ekedcCommission;

        } else if (SpecificChannel.PAY_KEDCO_ELECTRICITY_ITEX.getName().equals(specificChannel)) {
            return creditAmount * kedcoCommission;

        } else if (SpecificChannel.PAY_EEDC_ELECTRICITY_ITEX.getName().equals(specificChannel)) {
            return creditAmount * eedcCommission;

        } else if (SpecificChannel.PAY_IBEDC_ELECTRICITY_ITEX.getName().equals(specificChannel)) {
            return creditAmount * ibedcCommission;

        } else if (SpecificChannel.PAY_IKEDC_ELECTRICITY_ITEX.getName().equals(specificChannel)) {
            return creditAmount * ikedcCommission;

        } else if (SpecificChannel.PAY_PHEDC_ELECTRICITY_ITEX.getName().equals(specificChannel)) {
            return creditAmount * phedcCommission;

        } else if (SpecificChannel.PAY_INTERNET_ITEX.getName().equals(specificChannel)) {
            return creditAmount * internetCommission;

        } else if (SpecificChannel.PAY_CABLE_TV_ITEX.getName().equals(specificChannel)) {
            return creditAmount * cableTvCommission;

        } else if (SpecificChannel.STARTIMES_TV.getName().equals(specificChannel)) {
            return creditAmount * startimesTvCommission;

        } else if (specificChannel.toLowerCase().contains("mtn")) {
            return creditAmount * mtnCommission;

        } else if (specificChannel.toLowerCase().contains("glo")) {
            return creditAmount * gloCommission;

        } else if (specificChannel.toLowerCase().contains("airtel")) {
            return creditAmount * airtelCommission;

        } else if (specificChannel.toLowerCase().contains("mobile")) {
            return creditAmount * mobileCommission;

        }/* else if (SpecificChannel.IBILE_PAY.getName().equals(specificChannel)) {

            if (accountNumber.equalsIgnoreCase(LIRS_INCOME_ACCT)) {
                return creditAmount * IBILE_COMMISSION;
            }
        }*/
        return 0.0;

    }

    public Double getVATFee(Double intraFee) {
        return intraFee * vatFeePercentage;
    }

    public Double getLendHubFee(Double loanAmount) {
        return loanAmount * lendHubFeePercentage;
    }

    private String getMemoForWalletToWallet(FundDTO fundDTO, String memo, String debitName, String creditName) {
        if ("INVOICE".equalsIgnoreCase(fundDTO.getChannel())) {
            memo = utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount()) + " requested from " + creditName + " To " + debitName;
        } else if (fundDTO.getSpecificChannel().toLowerCase().contains("vtu")) {
            memo = "Purchase of " + utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount()) + " for airtime";
        } else if (fundDTO.getSpecificChannel().toLowerCase().contains("data")) {
            memo = "Purchase of " + utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount()) + " for data";
        } else if ("payRRR".equalsIgnoreCase(fundDTO.getSpecificChannel())) {
            memo = utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount()) + " for RRR payment";
        } else if (SpecificChannel.PAY_BILLS.getName().equalsIgnoreCase(fundDTO.getSpecificChannel())) {
            memo = utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount()) + " for Utils payment";
        } else if (fundDTO.getSpecificChannel().toLowerCase().contains("disco")) {
            memo = utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount()) + " for Electricity payment";
        } else if (fundDTO.getSpecificChannel().toLowerCase().contains("cabletv")) {
            memo = utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount()) + " for cable tv subscription";
        } else if (fundDTO.getSpecificChannel().toLowerCase().contains("internet")) {
            memo = utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount()) + " for internet subscription";
        } else if (SpecificChannel.PAY_BILLER.getName().equalsIgnoreCase(fundDTO.getSpecificChannel())) {
            memo = utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount()) + " for bill payment";
        } else if (SpecificChannel.WALLENCY_SCHOOLS_FEES_PAYMENT.getName().equalsIgnoreCase(fundDTO.getSpecificChannel())
            || SpecificChannel.WALLENCY_SCHOOLS_FEES_PAYMENT_MCU.getName().equalsIgnoreCase(fundDTO.getSpecificChannel())) {
            memo = utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount()) + " for " + fundDTO.getRrr() + " School Fees Payment";
        } else if (SpecificChannel.POLARIS_CARD_REQUEST.getName().equalsIgnoreCase(fundDTO.getSpecificChannel())) {
            memo = utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount()) + " for Debit Card Request";
        } else {
            memo = memo + " from " + debitName + " to " + creditName;
        }
        return memo;
    }

    private TransactionStatus setFundDTOStatus(FundDTO fundDTO, TransactionStatus status) {
        fundDTO.setStatus(status);
        out.println("INITIAL FundDTO == > " + fundDTO.getTransRef());
        FundDTO save = transactionLogService.save(fundDTO);
        return save.getStatus();
    }


    private String BankToWallet(FundDTO fundDTO) {

        WalletAccount creditAccount = walletAccountService.findOneByAccountNumber(fundDTO.getAccountNumber());

        if (creditAccount != null) {

//            boolean isACashConnectTranaction = creditAccount.getScheme() != null && IBILE_SCHEME.equalsIgnoreCase(creditAccount.getScheme().getSchemeID());
            boolean isACashConnectTranaction = false;

            TransactionStatus status = setFundDTOStatus(fundDTO, TransactionStatus.INCOMPLETE);

            Set<JournalLineDTO> nubanEntryLines = new LinkedHashSet<>();

            String memo = "Transfer of " + utility.formatMoney(fundDTO.getAmount());
            ArrayList<JournalLineDTO> lines = new ArrayList<>();
            JournalLineDTO journalLine1DTO = new JournalLineDTO();
            Double bankToWalletCharges = getBankToWalletCharges(fundDTO.getAmount(), fundDTO.getSpecificChannel());

            WalletAccount debitAccount = walletAccountService.findOneByAccountNumber(CORRESPONDENCE_ACCOUNT);
            journalLine1DTO.setAccount(debitAccount);
            journalLine1DTO.setCredit(zeroFee);
            journalLine1DTO.setDebit(fundDTO.getAmount() + bankToWalletCharges); //Debit

            memo = memo + " From " + fundDTO.getSourceAccountName();

            JournalLineDTO journalLine2DTO = new JournalLineDTO();
            journalLine1DTO.setChannel(fundDTO.getChannel());
            memo = memo + " To " + fundDTO.getBeneficiaryName() + ". Payment Transaction Ref : " + fundDTO.getTransRef();
            journalLine2DTO.setAccount(creditAccount);
            journalLine2DTO.setCredit(fundDTO.getAmount());
            journalLine2DTO.setDebit(zeroFee);

            nubanEntryLines.add(journalLine2DTO);


            if (SpecificChannel.CORAL_PAY.getName().equalsIgnoreCase(fundDTO.getSpecificChannel())) {
                JournalLineDTO journalLine6DTO = new JournalLineDTO();
                WalletAccount coralIncomeAccount = walletAccountService.findOneByAccountNumber(CORAL_PAY_USSD_FUNDING_ACCT);
                journalLine6DTO.setAccount(coralIncomeAccount);
                journalLine6DTO.setDebit(zeroFee);
                journalLine6DTO.setCredit(bankToWalletCharges);
                lines.add(journalLine6DTO);

                nubanEntryLines.add(journalLine6DTO); //debit

            } else {
                JournalLineDTO journalLine4DTO = new JournalLineDTO();

                if (!SpecificChannel.FUND_WALLET_CASHCONNECT.equals(fundDTO.getSpecificChannel())) {
                    if (!SpecificChannel.FUND_WALLET_POLARIS.equals(fundDTO.getSpecificChannel())) {
                        JournalLineDTO journalLine7DTO = new JournalLineDTO();
                        WalletAccount rpslInlineAccount = walletAccountService.findOneByAccountNumber(RSPL_INLINE_ACCT);
                        journalLine7DTO.setAccount(rpslInlineAccount);
                        journalLine7DTO.setDebit(zeroFee);
                        journalLine7DTO.setCredit(bankToWalletCharges);
                        lines.add(journalLine7DTO);

                        nubanEntryLines.add(journalLine7DTO); //debit
                    }
                }
            }

            status = TransactionStatus.COMPLETED;
            status = setFundDTOStatus(fundDTO, status);

            lines.add(journalLine1DTO);
            lines.add(journalLine2DTO);

            if (isACashConnectTranaction) {
                out.println("Transaction Origin ====> " + fundDTO.getSpecificChannel());

                if (!SpecificChannel.FUND_WALLET_CASHCONNECT.getName().equalsIgnoreCase(fundDTO.getSpecificChannel())) {
                    Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
                    if (asyncExecutor != null) {
                        asyncExecutor.execute(() -> {
                            try {
                                balanceNubanAccounts(nubanEntryLines, fundDTO.getNarration());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }

            String doubleEntryReturn = doubleEntry(lines, memo, bankToWalletCharges, status, fundDTO, zeroFee);
            try {
                transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return doubleEntryReturn;
        }

        return "creditAccount cannot be null";

    }

    public SinglePaymentResponse sendToBankAccount(FundDTO fundDTO) {
        SinglePaymentRequest request = new SinglePaymentRequest();

        String accountNumber = fundDTO.getSourceAccountNumber();
        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
        if (walletAccount != null && walletAccount.getScheme() != null) {
            Scheme scheme = walletAccount.getScheme();
            request.setAmount(String.valueOf(fundDTO.getAmount() + fundDTO.getBonusAmount()));
            request.setCreditAccount(String.valueOf(fundDTO.getAccountNumber()));
            request.setDebitAccount(scheme.getAccountNumber());
//            request.setDebitAccount("2033196815");
            request.setFromBank(scheme.getBankCode());
//            request.setFromBank("011");
            request.setToBank(fundDTO.getDestBankCode());
            request.setNarration("Send Money from Pouchii Account To " + fundDTO.getAccountNumber());

            request.setTransRef(fundDTO.getTransRef());
            return ritsService.singlePayment(request);
        }
        return null;
    }

    private String loanDisbursement(FundDTO fundDTO) throws Exception {
        TransactionStatus status = setFundDTOStatus(fundDTO, TransactionStatus.START);

        String memo = "Transfer of " + utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount());
        ArrayList<JournalLineDTO> lines = new ArrayList<>();
        JournalLineDTO sourceAccountDebitLine = new JournalLineDTO();

        Double transactionFee;

        String specificChannel = fundDTO.getSpecificChannel();
/*        if (utility.checkStringIsValid(String.valueOf(fundDTO.getCharges()))) {
            transactionFee = fundDTO.getCharges();
        } else {*/
        transactionFee = getTransactionFee(fundDTO.getAmount() + fundDTO.getBonusAmount(), "liberty", fundDTO);
        /*}*/


        Double lendhubFee = getLendHubFee(fundDTO.getAmount());
        double vatPayableFee = getVATFee(transactionFee + lendhubFee); //vat payable acct
        Double feeToDebit = transactionFee + vatPayableFee + lendhubFee;
        Double charges = transactionFee + lendhubFee;
        double creditAmount = fundDTO.getAmount();

        out.printf(" The source account === %s destination === %s%n", fundDTO.getSourceAccountNumber(), fundDTO.getAccountNumber());
        WalletAccount debitAccount = walletAccountService.findOneByAccountNumber(fundDTO.getSourceAccountNumber());
        WalletAccount creditAccount = walletAccountService.findOneByAccountNumber(fundDTO.getAccountNumber());

        if (debitAccount != null && creditAccount != null) {
            if (SpecificChannel.LIBERTY.getName().equalsIgnoreCase(specificChannel)) {
                sourceAccountDebitLine.setAccount(debitAccount);
                sourceAccountDebitLine.setCredit(zeroFee);
                double overallTransactionAmount = fundDTO.getAmount() + feeToDebit;
                sourceAccountDebitLine.setDebit(overallTransactionAmount);
                sourceAccountDebitLine.setChannel(fundDTO.getChannel());
                lines.add(sourceAccountDebitLine);

                Profile debitAccountOwner = debitAccount.getAccountOwner();

                //Debit transit account
                JournalLineDTO transitAccountCreditLine = new JournalLineDTO();
                WalletAccount transitAccount = walletAccountService.findOneByAccountNumber(TRANSIT_ACCOUNT);
                transitAccountCreditLine.setAccount(transitAccount);
                transitAccountCreditLine.setCredit(overallTransactionAmount);
                transitAccountCreditLine.setDebit(zeroFee);
                lines.add(transitAccountCreditLine);

                status = setFundDTOStatus(fundDTO, TransactionStatus.INTRANSIT);
                boolean isSuccessful = true;

                if (isSuccessful) {
                    //Credit back transit account
                    JournalLineDTO transitAccountDebitLine = new JournalLineDTO();
                    transitAccountDebitLine.setAccount(transitAccount);
                    transitAccountDebitLine.setDebit(overallTransactionAmount);
                    transitAccountDebitLine.setCredit(zeroFee);
                    transitAccountDebitLine.setChannel(specificChannel);
                    lines.add(transitAccountDebitLine);

                    status = TransactionStatus.INCOMPLETE;

                    //credit the destination  account
                    JournalLineDTO destinationAccountCreditLine = new JournalLineDTO();
                    destinationAccountCreditLine.setAccount(creditAccount);
                    destinationAccountCreditLine.setCredit(creditAmount);
                    destinationAccountCreditLine.setDebit(zeroFee);
                    //Add destinationAccountCreditLine to credit lines
                    lines.add(destinationAccountCreditLine);

                    if (vatPayableFee > 0.0) {
                        JournalLineDTO vatPayableAccountCreditLine = new JournalLineDTO();
                        WalletAccount vatAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
                        vatPayableAccountCreditLine.setAccount(vatAccount);
                        vatPayableAccountCreditLine.setCredit(vatPayableFee);
                        vatPayableAccountCreditLine.setDebit(zeroFee);
                        lines.add(vatPayableAccountCreditLine);
                    }

                    JournalLineDTO feeAccountCreditLine = new JournalLineDTO();
                    WalletAccount chargesAccount = walletAccountService.findOneByAccountNumber(LENDING_DISBURSEMENT_INCOME_ACCT);
                    feeAccountCreditLine.setCredit(transactionFee);
                    feeAccountCreditLine.setAccount(chargesAccount);
                    feeAccountCreditLine.setDebit(zeroFee);
                    lines.add(feeAccountCreditLine);


                    JournalLineDTO lendhubAccountCreditLine = new JournalLineDTO();
                    WalletAccount lendHubAccount = walletAccountService.findOneByAccountNumber(LENDING_HUB_FEE_ACCT);
                    lendhubAccountCreditLine.setCredit(lendhubFee);
                    lendhubAccountCreditLine.setAccount(lendHubAccount);
                    lendhubAccountCreditLine.setDebit(zeroFee);
                    lines.add(lendhubAccountCreditLine);

                    memo = getMemoForWalletToWallet(fundDTO, memo, fundDTO.getSourceAccountName(), fundDTO.getBeneficiaryName());
                    memo = String.format("%s. Payment Transaction Ref : %s", memo, fundDTO.getTransRef());

                    status = TransactionStatus.COMPLETED;
                    status = setFundDTOStatus(fundDTO, status);

                    if ("INVOICE".equalsIgnoreCase(fundDTO.getChannel())) {
                        status = TransactionStatus.INCOMPLETE;
                        status = setFundDTOStatus(fundDTO, status);
                        fundDTO.setBulkTrans(false);
                    }
                    String doubleEntryReturn = doubleEntry(lines, memo, charges, status, fundDTO, vatPayableFee);
                    try {
                        transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return doubleEntryReturn;
                }

                memo = getMemoForWalletToWallet(fundDTO, memo, fundDTO.getSourceAccountName(), fundDTO.getBeneficiaryName());

                memo = memo + ". Payment Transaction Ref : " + fundDTO.getTransRef();

                JournalLineDTO sourceAccountReversalLine = new JournalLineDTO();
                sourceAccountReversalLine.setAccount(debitAccount);
                sourceAccountReversalLine.setCredit(overallTransactionAmount);
                sourceAccountReversalLine.setDebit(zeroFee);
                sourceAccountReversalLine.setChannel(fundDTO.getChannel());

                JournalLineDTO transitAccountReversalLine = new JournalLineDTO();
                transitAccountReversalLine.setAccount(transitAccount);
                transitAccountReversalLine.setCredit(zeroFee);
                transitAccountReversalLine.setDebit(overallTransactionAmount);

                lines.add(sourceAccountDebitLine);
                lines.add(transitAccountCreditLine);
                lines.add(sourceAccountReversalLine);
                lines.add(transitAccountReversalLine);
                status = TransactionStatus.REVERSED;
                status = setFundDTOStatus(fundDTO, status);

                String doubleEntryReturn = doubleEntry(lines, memo, charges, status, fundDTO, vatPayableFee);
                try {
                    transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return doubleEntryReturn;
            }
        }

        return "Source Account or Debit Account cannot be null";
    }

    private String WalletToBank(FundDTO fundDTO) throws Exception {

        TransactionStatus status = TransactionStatus.START;
        status = setFundDTOStatus(fundDTO, status);

        ArrayList<JournalLineDTO> lines = new ArrayList<>();
        JournalLineDTO sourceAccountDebitLine = new JournalLineDTO();
        JournalLineDTO bonusPointAccountDebitLine = new JournalLineDTO();

        WalletAccount debitAccount = walletAccountService.findOneByAccountNumber(fundDTO.getSourceAccountNumber());
        boolean isACashConnectTranaction = false;

        if (debitAccount != null) {
            out.println("isCashconnectTransaction --->==> " + isACashConnectTranaction);
            double transactionFee = getTransactionFee(fundDTO.getAmount() + fundDTO.getBonusAmount(),
                fundDTO.getSpecificChannel(), fundDTO);
            double vatFee = getVATFee(transactionFee);
            double feeToDebit = transactionFee + vatFee;
            double nibbsCostOfTransaction = getNibbsCostOfTransaction(); //Polaris interbank account
            double spread = feeToDebit - nibbsCostOfTransaction; //STSL spread
            double partneringBankFee = calculatePolarisPartneringFee(spread); //Polaris commission payable wallet
            double polarisPomengranateFee = calculatePolarisPomengranateFee(spread); //Polaris pomengranate
            double ssIncludeVAT = calculateSSIncludeVAT(spread);
            double ssIncomeExcludingVAT = calculateSSIncomeExcludingVAT(ssIncludeVAT);
            double vatPayable = calculateVATPayable(ssIncomeExcludingVAT); //vat payable acct
            Double bonusPointAmount = calculateBonusPointEarned(transactionFee, fundDTO.getSpecificChannel(), debitAccount);
            double introducerFee = calculateIntroducerFee(transactionFee); //Introducer acct
            double netIncome = ssIncomeExcludingVAT - bonusPointAmount - introducerFee; //pouchii income acct
            double debitAmount = fundDTO.getAmount() + feeToDebit;

            double charges;
            double schemeVat;

            if(StringUtils.containsIgnoreCase(debitAccount.getScheme().getScheme(), "PayMasta")){
                if (utility.checkStringIsValid(String.valueOf(fundDTO.getCharges()))) {
                    charges = fundDTO.getCharges();
                    transactionFee = getTransactionFee(fundDTO.getAmount() + fundDTO.getBonusAmount(), fundDTO.getSpecificChannel(), fundDTO);
                    vatFee = getVATFee(transactionFee);
                    feeToDebit = transactionFee + vatFee;
                    schemeVat = getVATFee(charges);
                    debitAmount = charges + schemeVat + fundDTO.getAmount() + feeToDebit;
                }
            }

            WalletAccount bonusPointAccount = null;

            sourceAccountDebitLine.setAccount(debitAccount);
            sourceAccountDebitLine.setCredit(zeroFee);
            sourceAccountDebitLine.setDebit(debitAmount);
            lines.add(sourceAccountDebitLine);

            Profile accountOwner = debitAccount.getAccountOwner();
            if (accountOwner != null) {
                bonusPointAccount = walletAccountService.getCustomerBonusAccount(accountOwner);
                out.println("BonusPoint Account ==> " + bonusPointAccount);

                if (fundDTO.getBonusAmount() > 0) {
                    bonusPointAccountDebitLine.setAccount(bonusPointAccount);
                    bonusPointAccountDebitLine.setDebit(fundDTO.getBonusAmount());
                    bonusPointAccountDebitLine.setCredit(zeroFee);
                    lines.add(bonusPointAccountDebitLine);
                }
            }

            JournalLineDTO transitAccountDebitLine = new JournalLineDTO();
            WalletAccount transitAccount = walletAccountService.findOneByAccountNumber(TRANSIT_ACCOUNT);
            transitAccountDebitLine.setAccount(transitAccount);
            transitAccountDebitLine.setDebit(zeroFee);
            transitAccountDebitLine.setCredit(debitAmount);
            transitAccountDebitLine.setChannel(fundDTO.getSpecificChannel());
            lines.add(transitAccountDebitLine);

            status = TransactionStatus.INTRANSIT;
            status = setFundDTOStatus(fundDTO, TransactionStatus.INTRANSIT);

            String memo = "Transfer of " + utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount());

            boolean isSuccessful = true;
            if (isSuccessful) {
                JournalLineDTO transitAccountCreditLine = new JournalLineDTO();
                transitAccountCreditLine.setAccount(transitAccount);
                transitAccountCreditLine.setChannel(fundDTO.getSpecificChannel());
                transitAccountCreditLine.setDebit(debitAmount);
                transitAccountCreditLine.setCredit(zeroFee);
                lines.add(transitAccountCreditLine);

                status = TransactionStatus.INCOMPLETE;
                status = setFundDTOStatus(fundDTO, status);

                JournalLineDTO correspondenceAccountCreditLine = new JournalLineDTO();
                WalletAccount creditAccount = walletAccountService.findOneByAccountNumber(CORRESPONDENCE_ACCOUNT);
                correspondenceAccountCreditLine.setAccount(creditAccount);
                correspondenceAccountCreditLine.setCredit(fundDTO.getAmount() + fundDTO.getBonusAmount());
                correspondenceAccountCreditLine.setDebit(zeroFee);
                correspondenceAccountCreditLine.setChannel(fundDTO.getChannel());
                correspondenceAccountCreditLine.setDestinationAccountNumber(fundDTO.getAccountNumber());
                lines.add(correspondenceAccountCreditLine);

                if (isACashConnectTranaction) {
                    if (bonusPointAmount > 0) {
                        JournalLineDTO bonusPointAccountCreditLine = new JournalLineDTO();
                        bonusPointAccountCreditLine.setAccount(bonusPointAccount);
                        bonusPointAccountCreditLine.setCredit(bonusPointAmount);
                        bonusPointAccountCreditLine.setDebit(zeroFee);
                        lines.add(bonusPointAccountCreditLine);
                    }

                    JournalLineDTO pouchiiIncomeCreditLine = new JournalLineDTO();
                    WalletAccount chargeAccount = walletAccountService.findOneByAccountNumber(POUCHII_INCOME_ACCT);
                    pouchiiIncomeCreditLine.setAccount(chargeAccount);
                    pouchiiIncomeCreditLine.setDebit(zeroFee);
                    pouchiiIncomeCreditLine.setCredit(netIncome);
                    lines.add(pouchiiIncomeCreditLine);

                    JournalLineDTO vatAccountCreditLine = new JournalLineDTO();
                    WalletAccount vatAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
                    vatAccountCreditLine.setAccount(vatAccount);
                    vatAccountCreditLine.setCredit(vatPayable);
                    vatAccountCreditLine.setDebit(zeroFee);
                    lines.add(vatAccountCreditLine);

                    JournalLineDTO cashconnectInterbankCreditLine = new JournalLineDTO();
                    cashconnectInterbankCreditLine.setDebit(zeroFee);
                    WalletAccount cashConnectInterbankAccount = walletAccountService.findOneByAccountNumber(CASH_CONNECT_INTERBANK_SERVICES_ACCT);
                    cashconnectInterbankCreditLine.setAccount(cashConnectInterbankAccount);
                    cashconnectInterbankCreditLine.setCredit(walletToBankCharge);
                    lines.add(cashconnectInterbankCreditLine);

                    //adding bonus Point to Profile
                    if (accountOwner != null) {
                        double totalBonus = accountOwner.getTotalBonus();
                        out.println("current total Bonus ===> " + totalBonus);

                        totalBonus = totalBonus - fundDTO.getBonusAmount() + bonusPointAmount;

                        out.println("Updated total Bonus ===> " + totalBonus);

                        accountOwner.setTotalBonus(totalBonus);
                        Profile savedProfile = profileService.save(accountOwner);
                        out.println(" The saved profile \n\n\n\n\n\n\n\n====Profile==" + savedProfile);

                    }
                    Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
                    if (asyncExecutor != null) {
                        asyncExecutor.execute(() -> {
                            try {
                                postBonusPointToCustomerNubanAccount(chargeAccount.getNubanAccountNo(), fundDTO.getSourceAccountNumber(),
                                    fundDTO.getSourceAccountName(), bonusPointAmount, fundDTO.getNarration());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    memo = memo + " To " + fundDTO.getBeneficiaryName();

                    status = TransactionStatus.COMPLETED;
                    status = setFundDTOStatus(fundDTO, status);
                    out.println("Lines ===> " + lines);

                    String doubleEntryReturn = doubleEntry(lines, memo, transactionFee, status, fundDTO, vatFee);
                    transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
                    return doubleEntryReturn;

                }
                else { //Polaris transaction

                    if(StringUtils.containsIgnoreCase(debitAccount.getScheme().getScheme(), "PayMasta")){
                        WalletAccount chargesAccount = walletAccountService.findOneByAccountNumber(PAYMASTA_COMMISSIONS_ACCT);
                        JournalLineDTO schemeCommissionLine = new JournalLineDTO();
                        schemeCommissionLine.setAccount(chargesAccount);
                        schemeCommissionLine.setDebit(zeroFee);
                        schemeCommissionLine.setCredit(fundDTO.getCharges());
                        lines.add(schemeCommissionLine);

                        schemeVat = getVATFee(fundDTO.getCharges());

                        chargesAccount = walletAccountService.findOneByAccountNumber(PAYMASTA_VAT_ACCT);
                        JournalLineDTO schemeVATLine = new JournalLineDTO();
                        schemeVATLine.setAccount(chargesAccount);
                        schemeVATLine.setDebit(zeroFee);
                        schemeVATLine.setCredit(schemeVat);
                        lines.add(schemeVATLine);
                    }

                    JournalLineDTO polarisInterbankCreditLine = new JournalLineDTO();
                    polarisInterbankCreditLine.setDebit(zeroFee);
                    WalletAccount polarisInterbankAccount = walletAccountService.findOneByAccountNumber(POLARIS_INTERBANK_SERVICES_ACCT);
                    polarisInterbankCreditLine.setAccount(polarisInterbankAccount);
                    polarisInterbankCreditLine.setCredit(nibbsCostOfTransaction);
                    lines.add(polarisInterbankCreditLine);

                    JournalLineDTO polarisCommissionCreditLine = new JournalLineDTO();
                    polarisCommissionCreditLine.setDebit(zeroFee);
                    WalletAccount polarisCommissionAccount = walletAccountService.findOneByAccountNumber(POLARIS_COMMISSION_ACCT);
                    polarisCommissionCreditLine.setAccount(polarisCommissionAccount);
                    polarisCommissionCreditLine.setCredit(partneringBankFee);
                    lines.add(polarisCommissionCreditLine);

                    JournalLineDTO polarisPomengranateCreditLine = new JournalLineDTO();
                    polarisPomengranateCreditLine.setDebit(zeroFee);
                    WalletAccount polarisPomengranateAccount =
                        walletAccountService.findOneByAccountNumber(POLARIS_POMENGRANATE_COMMISSION_ACCT);
                    polarisPomengranateCreditLine.setAccount(polarisPomengranateAccount);
                    polarisPomengranateCreditLine.setCredit(polarisPomengranateFee);
                    lines.add(polarisPomengranateCreditLine);

                    JournalLineDTO vatAccountPayableCreditLine = new JournalLineDTO();
                    WalletAccount vatAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
                    vatAccountPayableCreditLine.setAccount(vatAccount);
                    vatAccountPayableCreditLine.setCredit(vatPayable);
                    vatAccountPayableCreditLine.setDebit(zeroFee);
                    lines.add(vatAccountPayableCreditLine);

                    JournalLineDTO introducerAccountCreditLine = new JournalLineDTO();
                    introducerAccountCreditLine.setDebit(zeroFee);
                    WalletAccount introducerAccount = walletAccountService.findOneByAccountNumber(INTRODUCER_ACCT);
                    introducerAccountCreditLine.setAccount(introducerAccount);
                    introducerAccountCreditLine.setCredit(introducerFee);
                    lines.add(introducerAccountCreditLine);

                    JournalLineDTO pouchiiIncomeCreditLine = new JournalLineDTO();
                    WalletAccount chargesAccount = walletAccountService.findOneByAccountNumber(POUCHII_INCOME_ACCT);
                    pouchiiIncomeCreditLine.setAccount(chargesAccount);
                    pouchiiIncomeCreditLine.setDebit(zeroFee);
                    pouchiiIncomeCreditLine.setCredit(netIncome);
                    lines.add(pouchiiIncomeCreditLine);

                    if (bonusPointAmount > 0) {
                        JournalLineDTO bonusPointAccountCreditLine = new JournalLineDTO();
                        bonusPointAccountCreditLine.setAccount(bonusPointAccount);
                        bonusPointAccountCreditLine.setCredit(bonusPointAmount);
                        bonusPointAccountCreditLine.setDebit(zeroFee);
                        lines.add(bonusPointAccountCreditLine);
                    }

                    out.println("Pouchii To Bank lines ====> " + lines);

                    //adding bonus Point to Profile
                    if (accountOwner != null) {
                        double totalBonus = accountOwner.getTotalBonus();
                        out.println("current total Bonus ===> " + totalBonus);

                        totalBonus = totalBonus - fundDTO.getBonusAmount() + bonusPointAmount;

                        out.println("Updated total Bonus ===> " + totalBonus);

                        accountOwner.setTotalBonus(totalBonus);
                        Profile savedProfile = profileService.save(accountOwner);
                        out.println(" The saved profile \n\n\n\n\n\n\n\n====Profile==" + savedProfile);
                    }

                    memo = memo + " To " + fundDTO.getBeneficiaryName();

                    status = TransactionStatus.COMPLETED;
                    status = setFundDTOStatus(fundDTO, status);
                    out.println("Lines ===> " + lines);

                    log.debug("Txn DoubleEntry Call  ===>>>  " + fundDTO.getTransRef());
                    String doubleEntry = doubleEntry(lines, memo, transactionFee, status, fundDTO, vatFee);

                    try {
                        if (SpecificChannel.SEND_MONEY.getName().equalsIgnoreCase(fundDTO.getSpecificChannel())) {
                            if (isACashConnectTranaction) {
                                GenericResponseDTO genericResponseDTO = cashConnectService.sendMoneyToBank(fundDTO/*, intraFee, vatFee*/);
                                out.println("CashConnect send money to bank response ===> " + genericResponseDTO);
                                if (genericResponseDTO != null && !HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                                } else {
                                    String trackingRef = (String) genericResponseDTO.getData();
                                    fundDTO.setRrr(trackingRef);
                                }
                            } else {
                                out.println("PolarisSendMoney Thread");
                                GenericResponseDTO genericResponseDTO = polarisSendMoneyToBank(fundDTO);
                                out.println("Polaris send money to bank response ===> " + genericResponseDTO);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return doubleEntry;
                    }
                    try {
                        transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return doubleEntry;
                }
            }

            memo = memo + " From " + fundDTO.getSourceAccountName() + ". Payment Transaction Ref : " + fundDTO.getTransRef();

            JournalLineDTO journalLine8DTO = new JournalLineDTO();
            journalLine8DTO.setAccount(transitAccount);
            journalLine8DTO.setDebit(debitAmount);
            journalLine8DTO.setCredit(zeroFee);
            journalLine8DTO.setChannel(fundDTO.getSpecificChannel());

            JournalLineDTO journalLine7DTO = new JournalLineDTO();
            journalLine7DTO.setAccount(debitAccount);
            journalLine7DTO.setCredit(debitAmount);
            journalLine7DTO.setDebit(zeroFee);

            lines.add(transitAccountDebitLine);
            lines.add(journalLine8DTO);
            lines.add(journalLine7DTO);

            if (fundDTO.getBonusAmount() > 0) {
                JournalLineDTO journalLine10DTO = new JournalLineDTO();
                journalLine10DTO.setCredit(fundDTO.getBonusAmount());
                journalLine10DTO.setDebit(zeroFee);
                journalLine10DTO.setAccount(bonusPointAccount);

                lines.add(journalLine10DTO);
            }

            status = TransactionStatus.REVERSED;
            status = setFundDTOStatus(fundDTO, status);

            String doubleEntryReturn = doubleEntry(lines, memo, transactionFee, status, fundDTO, vatFee);
            try {
                transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return doubleEntryReturn;
        }
        return "Source Account cannot be null";
    }

    private String WalletToBanks(FundDTO fundDTO) throws Exception {
        String specificChannel = fundDTO.getSpecificChannel();
        ArrayList<JournalLineDTO> lines = new ArrayList<>();
        boolean isSuccessful = true;

        TransactionStatus status = TransactionStatus.START;
        status = setFundDTOStatus(fundDTO, status);
        Double transactionAmount = fundDTO.getAmount();
        Double bonusAmount = fundDTO.getBonusAmount();
        WalletAccount bonusPointAccount = null;

        if (SpecificChannel.HM_PAYROLL.getName().equalsIgnoreCase(specificChannel)) {

            double bulkBeneficiaryAmount = 0, bulkTransactionFee = 0, bulkVatFee = 0,
                bulkFeeToDebit = 0, bulkNibbsCostOfTransaction = 0,
                bulkPolarisPomengranateFee = 0, bulkVatPayable = 0, bulkIntroducerFee = 0, bulkNetIncome = 0,
                bulkHMProcessingFee = 0, bulkHMProcessingFeeVAT = 0,
                overallTransitAccountDebitAmount = 0, bulkPartneringBankFee = 0, bulkAmountToBeReversed = 0;

            WalletAccount debitAccount = walletAccountService.findOneByAccountNumber(fundDTO.getSourceAccountNumber());

            double chargesPlusVAT = getTransactionFee(transactionAmount, specificChannel, fundDTO);

            if (debitAccount != null) {
                //get All accounts
                WalletAccount transitAccount = walletAccountService.findOneByAccountNumber(TRANSIT_ACCOUNT);
                WalletAccount hMCorrespondenceAccount = walletAccountService.findOneByAccountNumber(HM_CORRESPONDENCE_ACCOUNT);
                WalletAccount polarisInterbankAccount = walletAccountService.findOneByAccountNumber(POLARIS_INTERBANK_SERVICES_ACCT);
                WalletAccount polarisCommissionAccount = walletAccountService.findOneByAccountNumber(POLARIS_COMMISSION_ACCT);
                WalletAccount polarisPomengranateAccount = walletAccountService.findOneByAccountNumber(POLARIS_POMENGRANATE_COMMISSION_ACCT);
                WalletAccount vatAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
                WalletAccount introducerAccount = walletAccountService.findOneByAccountNumber(INTRODUCER_ACCT);
                WalletAccount chargesAccount = walletAccountService.findOneByAccountNumber(POUCHII_INCOME_ACCT);
                WalletAccount hmProcessingFeeAccount = walletAccountService.findOneByAccountNumber(HM_PROCESSING_FEE_ACCT);
                WalletAccount hMVatPayableAccount = walletAccountService.findOneByAccountNumber(HM_VAT_PAYABLE_ACCT);

                //overall debit amount
                double debitAmount = transactionAmount + chargesPlusVAT;
                JournalLineDTO sourceAccountDebitLine = new JournalLineDTO(debitAmount, 0.00, debitAccount, specificChannel);
                lines.add(sourceAccountDebitLine);

                Profile accountOwner = debitAccount.getAccountOwner();

//                if (accountOwner != null) {
//                    bonusPointAccount = walletAccountService.getCustomerBonusAccount(accountOwner);
//                    out.println("BonusPoint Account ==> " + bonusPointAccount);
//
//                    if (bonusAmount > 0) {
//                        JournalLineDTO bonusPointDebitLine = new JournalLineDTO(bonusAmount, zeroFee, bonusPointAccount, specificChannel);
//                        lines.add(bonusPointDebitLine);
//                    }
//                }

                //credit transit account
                JournalLineDTO transitAccountCreditLine = new JournalLineDTO(zeroFee, debitAmount + bonusAmount,
                    transitAccount,
                    specificChannel);
                lines.add(transitAccountCreditLine);

                status = TransactionStatus.INTRANSIT;
                status = setFundDTOStatus(fundDTO, TransactionStatus.INTRANSIT);
                String memo = "Transfer of " + utility.formatMoney(transactionAmount + bonusAmount);

//               perform all creditTitle to bankService and other splits

                String originalRef = fundDTO.getTransRef();
                double originalAmt = transactionAmount + bonusAmount;

                for (BulkBeneficiaryDTO beneficiary : fundDTO.getBulkAccountNos()) {
                    String newRef = utility.getUniqueTransRef();
                    originalRef = originalRef + "||" + newRef;
                    FundDTO newFundDTO = buildHMFundDTO(fundDTO, originalRef, beneficiary, newRef);

                    FundDTO save = transactionLogService.saveBulkTrans(newFundDTO);
                    log.info("=========================");
                    log.info("Child Bulk fundDTO =======> " + save);
                    log.info("=========================\n");

                    //setting wallet to Bank fee
                    double beneficiaryAmount = newFundDTO.getAmount() + newFundDTO.getBonusAmount();
                    String newFundDtoSpecificChannel = newFundDTO.getSpecificChannel();
                    double transactionFee = getTransactionFee(beneficiaryAmount,
                        newFundDtoSpecificChannel, newFundDTO);
                    double vatFee = getVATFee(transactionFee);
                    double feeToDebit = transactionFee + vatFee;
                    double nibbsCostOfTransaction = getNibbsCostOfTransaction(); //Polaris interbank account
                    double spread = feeToDebit - nibbsCostOfTransaction; //STSL spread
                    double partneringBankFee = calculatePolarisPartneringFee(spread); //Polaris commission payable wallet
                    double polarisPomengranateFee = calculatePolarisPomengranateFee(spread); //Polaris pomengranate
                    double ssIncludeVAT = calculateSSIncludeVAT(spread);
                    double ssIncomeExcludingVAT = calculateSSIncomeExcludingVAT(ssIncludeVAT);
                    double vatPayable = calculateVATPayable(ssIncomeExcludingVAT); //vat payable acct
                    Double bonusPointAmount = calculateBonusPointEarned(transactionFee, newFundDtoSpecificChannel, debitAccount);
                    double introducerFee = calculateIntroducerFee(transactionFee); //Introducer acct
                    double netIncome = ssIncomeExcludingVAT - bonusPointAmount - introducerFee; //pouchii income acct
                    double hMProcessingFee = calculateHumanManagerProcessingFee();
                    double hMProcessingFeeVAT = getVATFee(hMProcessingFee);


                    GenericResponseDTO genericResponseDTO = polarisSendMoneyToBank(fundDTO);
                    out.println("Polaris send money to bank response ===> " + genericResponseDTO);
//                    if (genericResponseDTO == null || !HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
//                        isSuccessful = false;
//                    }

                    double beneficiaryTransitDebitAmount =
                        beneficiaryAmount + feeToDebit + hMProcessingFee + hMProcessingFeeVAT;

                    isSuccessful = true;

                    if (isSuccessful) {

                        overallTransitAccountDebitAmount += beneficiaryTransitDebitAmount;

                        bulkBeneficiaryAmount += beneficiaryAmount;

                        bulkNibbsCostOfTransaction += nibbsCostOfTransaction;

                        bulkPartneringBankFee += partneringBankFee;

                        bulkPolarisPomengranateFee += polarisPomengranateFee;

                        bulkVatPayable += vatPayable;

                        bulkIntroducerFee += introducerFee;

                        bulkNetIncome += netIncome;

                        bulkHMProcessingFee += hMProcessingFee;

                        bulkHMProcessingFeeVAT += hMProcessingFeeVAT;

                        bulkTransactionFee += transactionFee;

                        bulkVatFee += vatFee;

                    } else {
                        bulkHMProcessingFee += hMProcessingFee;

                        bulkHMProcessingFeeVAT += hMProcessingFeeVAT;

                        double amountTobeReversed = beneficiaryAmount + feeToDebit;
                        bulkAmountToBeReversed += amountTobeReversed;
                    }

                }//end of for loop

                JournalLineDTO transitDebitLine = new JournalLineDTO(overallTransitAccountDebitAmount, zeroFee,
                    transitAccount, specificChannel);
                lines.add(transitDebitLine);

                //update Transaction Log
                status = TransactionStatus.INCOMPLETE;
                status = setFundDTOStatus(fundDTO, status);

                //simulate beneficiary destination account
                JournalLineDTO correspondenceAccountCreditLine = new JournalLineDTO(zeroFee,
                    bulkBeneficiaryAmount, hMCorrespondenceAccount, specificChannel);
                correspondenceAccountCreditLine.setDestinationAccountNumber(fundDTO.getAccountNumber());
                lines.add(correspondenceAccountCreditLine);

                JournalLineDTO polarisInterbankCreditLine = new JournalLineDTO(zeroFee,
                    bulkNibbsCostOfTransaction, polarisInterbankAccount, specificChannel);
                lines.add(polarisInterbankCreditLine);

                JournalLineDTO polarisCommissionCreditLine = new JournalLineDTO(zeroFee,
                    bulkPartneringBankFee, polarisCommissionAccount, specificChannel);
                lines.add(polarisCommissionCreditLine);

                JournalLineDTO polarisPomengranateCreditLine = new JournalLineDTO(zeroFee,
                    bulkPolarisPomengranateFee, polarisPomengranateAccount, specificChannel);
                lines.add(polarisPomengranateCreditLine);

                JournalLineDTO vatAccountPayableCreditLine = new JournalLineDTO(zeroFee, bulkVatPayable,
                    vatAccount, specificChannel);
                lines.add(vatAccountPayableCreditLine);

                JournalLineDTO introducerAccountCreditLine = new JournalLineDTO(zeroFee, bulkIntroducerFee,
                    introducerAccount, specificChannel);
                lines.add(introducerAccountCreditLine);

                JournalLineDTO pouchiiIncomeCreditLine = new JournalLineDTO(zeroFee, bulkNetIncome,
                    chargesAccount, specificChannel);
                lines.add(pouchiiIncomeCreditLine);

                JournalLineDTO hmProcessingFeeCreditLine = new JournalLineDTO(zeroFee, bulkHMProcessingFee,
                    hmProcessingFeeAccount, specificChannel);
                lines.add(hmProcessingFeeCreditLine);

                JournalLineDTO hmVATPayableCreditLine = new JournalLineDTO(zeroFee, bulkHMProcessingFeeVAT,
                    hMVatPayableAccount, specificChannel);
                lines.add(hmVATPayableCreditLine);

                if (bulkAmountToBeReversed > 0) {

                    JournalLineDTO sourceAccountCreditLine = new JournalLineDTO(zeroFee, bulkAmountToBeReversed,
                        debitAccount, specificChannel);
                    lines.add(sourceAccountCreditLine);
                }

                out.println("Pouchii To Bank lines ====> " + lines);

                memo = memo + " To " + fundDTO.getBeneficiaryName();

                status = TransactionStatus.COMPLETED;
                status = setFundDTOStatus(fundDTO, status);
                out.println("Lines ===> " + lines);

                String doubleEntryReturn = doubleEntry(lines, memo, bulkTransactionFee, status, fundDTO, bulkVatFee);
                try {
                    transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return doubleEntryReturn;
            }

            return "Invalid source account";
        } else {
            JournalLineDTO journalLine2DTO = new JournalLineDTO();
            JournalLineDTO journalLine9DTO = new JournalLineDTO();

            WalletAccount debitAccount = walletAccountService.findOneByAccountNumber(fundDTO.getSourceAccountNumber());

            if (debitAccount != null) {
                Double intraFee = getTransactionFee(transactionAmount + bonusAmount, specificChannel,
                    fundDTO);
                Double vatFee = getVATFee(intraFee);
                double debitAmount = transactionAmount + intraFee + vatFee;

                journalLine2DTO.setAccount(debitAccount);
                journalLine2DTO.setCredit(zeroFee);
                journalLine2DTO.setDebit(debitAmount);

                Profile accountOwner = debitAccount.getAccountOwner();

                if (accountOwner != null) {
                    bonusPointAccount = walletAccountService.getCustomerBonusAccount(accountOwner);
                    out.println("BonusPoint Account ==> " + bonusPointAccount);

                    if (bonusAmount > 0) {
                        journalLine9DTO.setAccount(bonusPointAccount);
                        journalLine9DTO.setDebit(bonusAmount);
                        journalLine9DTO.setCredit(zeroFee);
                        lines.add(journalLine9DTO);
                    }
                }

                JournalLineDTO journalLine5DTO = new JournalLineDTO();
                WalletAccount transitAccount = walletAccountService.findOneByAccountNumber(TRANSIT_ACCOUNT);
                journalLine5DTO.setAccount(transitAccount);
                journalLine5DTO.setDebit(zeroFee);
                journalLine5DTO.setCredit(debitAmount);
                journalLine5DTO.setChannel(specificChannel);

                status = TransactionStatus.INTRANSIT;
                status = setFundDTOStatus(fundDTO, TransactionStatus.INTRANSIT);

                String memo = "Transfer of " + utility.formatMoney(transactionAmount + bonusAmount);


                boolean isACashConnectTransaction = false;
                if (SpecificChannel.SEND_MONEY_TO_BANKS.getName().equalsIgnoreCase(specificChannel)) {
                    String bulkRefs = "";
                    String originalRef = fundDTO.getTransRef();
                    double originalAmt = transactionAmount;
                    String originalBeneficiary = fundDTO.getBeneficiaryName();

                    for (BulkBeneficiaryDTO beneficiary : fundDTO.getBulkAccountNos()) {
                        fundDTO.setBeneficiaryName(beneficiary.getBeneficiaryFullName());
                        String newRef = utility.getUniqueTransRef();
                        fundDTO.setRrr(originalRef + "||" + newRef);
                        fundDTO.setTransRef(newRef);
                        bulkRefs = bulkRefs + "||" + newRef;
                        fundDTO.setStatus(TransactionStatus.START);
                        fundDTO.setAccountNumber(beneficiary.getAccountNumber());
                        fundDTO.setAmount(beneficiary.getAmount());
                        FundDTO save = transactionLogService.saveBulkTrans(fundDTO);
                        log.info("=========================");
                        log.info("Saved Bulk fundDTO =======> " + save);
                        log.info("=========================\n");

                        //todo construct new fundDTO here
                        FundDTO bulkFundDTO = fundDTO;
                        bulkFundDTO.setAmount(beneficiary.getAmount());
                        bulkFundDTO.setAccountNumber(beneficiary.getAccountNumber());
                        bulkFundDTO.setDestBankCode(beneficiary.getBankCode());
                        bulkFundDTO.setBeneficiaryName(beneficiary.getBeneficiaryFullName());
                        GenericResponseDTO genericResponseDTO = polarisSendMoneyToBank(bulkFundDTO);
                        out.println("Polaris send money to bank response ===> " + genericResponseDTO);
                    }
/*                    if (genericResponseDTO == null || !HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                        isSuccessful = false;
                    }*/
                }

                if (isSuccessful) {
                    JournalLineDTO journalLine6DTO = new JournalLineDTO();
                    journalLine6DTO.setAccount(transitAccount);
                    journalLine6DTO.setChannel(specificChannel);
                    journalLine6DTO.setDebit(debitAmount);
                    journalLine6DTO.setCredit(zeroFee);

                    status = TransactionStatus.INCOMPLETE;
                    status = setFundDTOStatus(fundDTO, status);

                    for (BulkBeneficiaryDTO b : fundDTO.getBulkAccountNos()) {
                        JournalLineDTO journalLine1DTO = new JournalLineDTO();
                        WalletAccount creditAccount = walletAccountService.findOneByAccountNumber(CORRESPONDENCE_ACCOUNT);
                        journalLine1DTO.setAccount(creditAccount);
                        journalLine1DTO.setCredit(b.getAmount());
                        journalLine1DTO.setDebit(zeroFee);
                        journalLine1DTO.setChannel(fundDTO.getChannel());
                        journalLine1DTO.setDestinationAccountNumber(b.getAccountNumber());
                        lines.add(journalLine1DTO);
                    }

                    double walletFee = intraFee - walletToBankCharge;

                    Double bonusPointAmount = calculateBonusPointEarned(intraFee, specificChannel, debitAccount);

                    if (bonusPointAmount > 0) {
                        JournalLineDTO journalLine11DTO = new JournalLineDTO();
                        journalLine11DTO.setAccount(bonusPointAccount);
                        journalLine11DTO.setCredit(bonusPointAmount);
                        journalLine11DTO.setDebit(zeroFee);
                        lines.add(journalLine11DTO);
                    }

                    JournalLineDTO journalLine3DTO = new JournalLineDTO();
                    WalletAccount chargeAccount = walletAccountService.findOneByAccountNumber(POUCHII_INCOME_ACCT);
                    journalLine3DTO.setAccount(chargeAccount);
                    journalLine3DTO.setCredit(intraFee - bonusPointAmount);
                    if (SpecificChannel.SEND_MONEY_TO_BANKS.getName().equalsIgnoreCase(specificChannel)) {
                        journalLine3DTO.setCredit(walletFee - bonusPointAmount);
                    }

                    journalLine3DTO.setDebit(zeroFee);

                    JournalLineDTO journalLine10DTO = new JournalLineDTO();
                    WalletAccount vatAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
                    journalLine10DTO.setAccount(vatAccount);
                    journalLine10DTO.setCredit(vatFee);
                    journalLine10DTO.setDebit(zeroFee);

                    JournalLineDTO journalLine4DTO = new JournalLineDTO();
                    journalLine4DTO.setDebit(zeroFee);

                    lines.add(journalLine2DTO);
                    lines.add(journalLine3DTO);
                    lines.add(journalLine4DTO);
                    lines.add(journalLine6DTO);
                    lines.add(journalLine5DTO);
                    lines.add(journalLine10DTO);

                    out.println("Pouchii To Bank lines ====> " + lines);

                    //adding bonus Point to Profile
                    if (accountOwner != null) {
                        double totalBonus = accountOwner.getTotalBonus();
                        out.println("current total Bonus ===> " + totalBonus);

                        totalBonus = totalBonus - bonusAmount + bonusPointAmount;

                        out.println("Updated total Bonus ===> " + totalBonus);

                        accountOwner.setTotalBonus(totalBonus);
                        Profile savedProfile = profileService.save(accountOwner);
                        out.println(" The saved profile \n\n\n\n\n\n\n\n====Profile==" + savedProfile);

                        if (isACashConnectTransaction) {
                            Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
                            if (asyncExecutor != null) {
                                asyncExecutor.execute(() -> {
                                    try {
                                        postBonusPointToCustomerNubanAccount(chargeAccount.getNubanAccountNo(), fundDTO.getSourceAccountNumber(),
                                            fundDTO.getSourceAccountName(), bonusPointAmount, fundDTO.getNarration());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }

                        }
                    }

                    memo = memo + " To " + fundDTO.getBeneficiaryName();

                    status = TransactionStatus.COMPLETED;
                    status = setFundDTOStatus(fundDTO, status);
                    out.println("Lines ===> " + lines);

                    String doubleEntryReturn = doubleEntry(lines, memo, intraFee, status, fundDTO, vatFee);
                    try {
                        transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return doubleEntryReturn;
                }

                memo = memo + " From " + fundDTO.getSourceAccountName() + ". Payment Transaction Ref : " + fundDTO.getTransRef();

                JournalLineDTO journalLine7DTO = new JournalLineDTO();
                journalLine7DTO.setAccount(debitAccount);
                journalLine7DTO.setCredit(debitAmount);
                journalLine7DTO.setDebit(zeroFee);

                JournalLineDTO journalLine8DTO = new JournalLineDTO();
                journalLine8DTO.setAccount(transitAccount);
                journalLine8DTO.setDebit(debitAmount);
                journalLine8DTO.setCredit(zeroFee);
                journalLine8DTO.setChannel(specificChannel);

                if (bonusAmount > 0) {

                    JournalLineDTO journalLine10DTO = new JournalLineDTO();
                    journalLine10DTO.setCredit(bonusAmount);
                    journalLine10DTO.setDebit(zeroFee);
                    journalLine10DTO.setAccount(bonusPointAccount);

                    lines.add(journalLine10DTO);
                }

                status = TransactionStatus.REVERSED;
                status = setFundDTOStatus(fundDTO, status);

                lines.add(journalLine2DTO);
                lines.add(journalLine5DTO);
                lines.add(journalLine7DTO);
                lines.add(journalLine8DTO);

                String doubleEntryReturn = doubleEntry(lines, memo, intraFee, status, fundDTO, vatFee);
                try {
                    transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return doubleEntryReturn;
            }
            return "Source Account cannot be null";
        }
       /* utility.sendEmailByConsumer(GMAIL_COM, "HM Wallet To Bank Failure", "HM request failed : fundDTO ==> " + fundDTO);
        utility.sendEmailByConsumer(Kazeem, "HM Wallet To Banks Failure", "HM request failed : fundDTO ==> " + fundDTO);
        throw new IllegalArgumentException("Hm transaction failure " + fundDTO);*/
    }

    private String WalletToBankReversal(FundDTO fundDTO) {

        String customerWalletNumber = fundDTO.getAccountNumber();
        out.println(" The source account ===" + fundDTO.getSourceAccountNumber() + " destination===" + customerWalletNumber);
        WalletAccount creditAccount = walletAccountService.findOneByAccountNumber(customerWalletNumber);
//        boolean isACashConnectTranaction = creditAccount.getScheme() != null && IBILE_SCHEME.equalsIgnoreCase(creditAccount.getScheme().getSchemeID());
        boolean isACashConnectTranaction = false;

        //calculate intraFee
        Double intraFee = getTransactionFee(fundDTO.getAmount(), SpecificChannel.SEND_MONEY.getName(), fundDTO);

        //calculate bonusPointAmount
        Double bonusPointAmount = calculateBonusPointEarned(intraFee, fundDTO.getSpecificChannel(), null);

        //calculate vatFee
        Double vatFee = getVATFee(getVATFee(intraFee));

        TransactionStatus status = setFundDTOStatus(fundDTO, TransactionStatus.START);

        String memo = "Transfer of " + utility.formatMoney(fundDTO.getAmount());
        ArrayList<JournalLineDTO> lines = new ArrayList<>();
        JournalLineDTO journalLine1DTO = new JournalLineDTO();


        //Debit Correspondent acct
        WalletAccount debitAccount = walletAccountService.findOneByAccountNumber(CORRESPONDENCE_ACCOUNT);
        journalLine1DTO.setAccount(debitAccount);
        journalLine1DTO.setCredit(zeroFee);
        journalLine1DTO.setDebit(fundDTO.getAmount() + fundDTO.getBonusAmount());
        String debitName = debitAccount.getAccountOwner() == null ? debitAccount.getAccountName() : debitAccount.getAccountOwner().getFullName();
        journalLine1DTO.setChannel(fundDTO.getChannel());

        //Credit Customer acct
        JournalLineDTO journalLine2DTO = new JournalLineDTO();
        journalLine2DTO.setAccount(creditAccount);
        double creditAmount = fundDTO.getAmount() + intraFee + vatFee;
        journalLine2DTO.setCredit(creditAmount);
        journalLine2DTO.setDebit(zeroFee);

        memo = memo + " from " + debitName + " to " + creditAccount.getAccountName();

        //Debit Remita Wallency Income Acct
        JournalLineDTO journalLine3DTO = new JournalLineDTO();
        WalletAccount chargesAccount = walletAccountService.findOneByAccountNumber(POUCHII_INCOME_ACCT);
        journalLine3DTO.setAccount(chargesAccount);
        journalLine3DTO.setCredit(zeroFee);
        journalLine3DTO.setDebit(intraFee - bonusPointAmount);


        //Debit VatFee from VAt Account
        JournalLineDTO journalLine4DTO = new JournalLineDTO();
        WalletAccount vatAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
        journalLine4DTO.setAccount(vatAccount);
        journalLine4DTO.setCredit(zeroFee);
        journalLine4DTO.setDebit(vatFee);

        if (isACashConnectTranaction) {
            JournalLineDTO journalLine5DTO = new JournalLineDTO();
            WalletAccount remitaIncomeAccount = walletAccountService.findOneByAccountNumber(CASH_CONNECT_INTERBANK_SERVICES_ACCT);
            journalLine5DTO.setAccount(remitaIncomeAccount);
            journalLine5DTO.setDebit(walletToBankCharge);
            journalLine5DTO.setCredit(zeroFee);
            lines.add(journalLine5DTO);
        }

        //Get Bonus Point Acct of Customer

        Profile customerProfile = creditAccount.getAccountOwner();

        if (customerProfile != null) {
            WalletAccount bonusPointAccount = walletAccountService.getCustomerBonusAccount(customerProfile);
            out.println("BonusPoint Account ==> " + bonusPointAccount);

            if (bonusPointAccount != null) {
                if (fundDTO.getBonusAmount() > 0) {

                    //Credit back  bonusAmount used back to customer
                    JournalLineDTO journalLine6DTO = new JournalLineDTO();
                    journalLine6DTO.setAccount(bonusPointAccount);
                    journalLine6DTO.setCredit(fundDTO.getBonusAmount());
                    journalLine6DTO.setDebit(zeroFee);
                    lines.add(journalLine6DTO);
                }

                //Debit bonusAmount earned from Customer BonusPoint account
                JournalLineDTO journalLine7DTO = new JournalLineDTO();
                journalLine7DTO.setAccount(bonusPointAccount);
                journalLine7DTO.setDebit(bonusPointAmount);
                journalLine7DTO.setCredit(zeroFee);

                lines.add(journalLine7DTO);

            }
        }

        memo = memo + ". Payment Transaction Ref : " + fundDTO.getTransRef();

        status = TransactionStatus.COMPLETED;
        status = setFundDTOStatus(fundDTO, status);

        lines.add(journalLine1DTO);
        lines.add(journalLine2DTO);
        lines.add(journalLine3DTO);
        lines.add(journalLine4DTO);

        String doubleEntryReturn = doubleEntry(lines, memo, intraFee, status, fundDTO, vatFee);
        try {
            transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return doubleEntryReturn;

    }

    private String WalletToBankIncompleteWallet(FundDTO fundDTO) {

        ArrayList<JournalLineDTO> lines = new ArrayList<>();

        WalletAccount debitAccount = walletAccountService.findOneByAccountNumber(fundDTO.getSourceAccountNumber());

        JournalLineDTO journalLine2DTO = new JournalLineDTO();
        journalLine2DTO.setAccount(debitAccount);
        journalLine2DTO.setCredit(zeroFee);
        Double intraFee = getTransactionFee(fundDTO.getAmount(), fundDTO.getSpecificChannel(), fundDTO);
        Double vatFee = getVATFee(intraFee);
        journalLine2DTO.setDebit(fundDTO.getAmount() + fundDTO.getBonusAmount() + intraFee + vatFee);

        TransactionStatus status = setFundDTOStatus(fundDTO, TransactionStatus.INCOMPLETE);

        String memo = "Transfer of " + utility.formatMoney(fundDTO.getAmount());

        JournalLineDTO journalLine1DTO = new JournalLineDTO();
        WalletAccount creditAccount = walletAccountService.findOneByAccountNumber(CORRESPONDENCE_ACCOUNT);
        journalLine1DTO.setAccount(creditAccount);
        journalLine1DTO.setCredit(fundDTO.getAmount());
        journalLine1DTO.setDebit(zeroFee);
        journalLine1DTO.setChannel(fundDTO.getChannel());


        JournalLineDTO journalLine3DTO = new JournalLineDTO();
        WalletAccount chargeAccount = walletAccountService.findOneByAccountNumber(POUCHII_INCOME_ACCT);
        journalLine3DTO.setAccount(chargeAccount);
        journalLine3DTO.setCredit(intraFee - walletToBankCharge);
        journalLine3DTO.setDebit(zeroFee);

        JournalLineDTO journalLine5DTO = new JournalLineDTO();
        WalletAccount vatAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
        journalLine5DTO.setAccount(vatAccount);
        journalLine5DTO.setCredit(vatFee);
        journalLine5DTO.setDebit(zeroFee);


        JournalLineDTO journalLine4DTO = new JournalLineDTO();
        WalletAccount remitaIncomeAccount = walletAccountService.findOneByAccountNumber(RPSL_INTERBANK_SERVICES_ACCT);
        journalLine4DTO.setAccount(remitaIncomeAccount);
        journalLine4DTO.setCredit(walletToBankCharge);
        journalLine4DTO.setDebit(zeroFee);

        memo = memo + " To " + fundDTO.getBeneficiaryName();

        status = TransactionStatus.COMPLETED;
        status = setFundDTOStatus(fundDTO, status);

        if (debitAccount != null && debitAccount.getAccountOwner() != null) {
        }
        memo = memo + " From " + fundDTO.getSourceAccountName() + ". Payment Transaction Ref : " + fundDTO.getTransRef();

        lines.add(journalLine1DTO);
        lines.add(journalLine2DTO);
        lines.add(journalLine3DTO);
        lines.add(journalLine4DTO);

        String doubleEntryReturn = doubleEntry(lines, memo, intraFee, status, fundDTO, vatFee);
        try {
            transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return doubleEntryReturn;

    }

    private String WalletToBankIncompleteBank(FundDTO fundDTO) {

        ArrayList<JournalLineDTO> lines = new ArrayList<JournalLineDTO>();

        Double intraFee = getTransactionFee(fundDTO.getAmount(), fundDTO.getSpecificChannel(), fundDTO);
        Double vatFee = getVATFee(intraFee);

        JournalLineDTO journalLine2DTO = new JournalLineDTO();
        WalletAccount debitAccount = walletAccountService.findOneByAccountNumber(CORRESPONDENCE_ACCOUNT);
        journalLine2DTO.setAccount(debitAccount);
        journalLine2DTO.setCredit(zeroFee);
        journalLine2DTO.setDebit(fundDTO.getAmount() + fundDTO.getBonusAmount());

        TransactionStatus status = setFundDTOStatus(fundDTO, TransactionStatus.INCOMPLETE);

        JournalLineDTO journalLine3DTO = new JournalLineDTO();
        WalletAccount chargeAccount = walletAccountService.findOneByAccountNumber(POUCHII_INCOME_ACCT);
        journalLine3DTO.setAccount(chargeAccount);
        journalLine3DTO.setDebit(intraFee - walletToBankCharge);
        journalLine3DTO.setCredit(zeroFee);

        JournalLineDTO journalLine5DTO = new JournalLineDTO();
        WalletAccount vatAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
        journalLine5DTO.setAccount(vatAccount);
        journalLine5DTO.setDebit(vatFee);
        journalLine5DTO.setCredit(zeroFee);

        JournalLineDTO journalLine4DTO = new JournalLineDTO();
        WalletAccount remitaIncomeAccount = walletAccountService.findOneByAccountNumber(RPSL_INTERBANK_SERVICES_ACCT);
        journalLine4DTO.setAccount(remitaIncomeAccount);
        journalLine4DTO.setDebit(walletToBankCharge);
        journalLine4DTO.setCredit(zeroFee);

        JournalLineDTO journalLine1DTO = new JournalLineDTO();
        WalletAccount creditAccount = walletAccountService.findOneByAccountNumber(fundDTO.getAccountNumber());
        journalLine1DTO.setAccount(creditAccount);
        journalLine1DTO.setCredit(fundDTO.getAmount() + intraFee);
        journalLine1DTO.setDebit(zeroFee);
        journalLine1DTO.setChannel(fundDTO.getChannel());

        String memo = "Transfer of " + utility.formatMoney(fundDTO.getAmount() + intraFee);


        memo = memo + " from correspondent account ";

        memo = memo + " To " + fundDTO.getBeneficiaryName() + ". Payment Transaction Ref : " + fundDTO.getTransRef();

        status = TransactionStatus.COMPLETED;
        status = setFundDTOStatus(fundDTO, status);

        lines.add(journalLine1DTO);
        lines.add(journalLine2DTO);
        lines.add(journalLine3DTO);
        lines.add(journalLine4DTO);

        String doubleEntryReturn = doubleEntry(lines, memo, intraFee, status, fundDTO, vatFee);
        try {
            transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return doubleEntryReturn;

    }

    private String BankToWalletReversal(FundDTO fundDTO) {
        TransactionStatus status = TransactionStatus.START;

        ArrayList<JournalLineDTO> lines = new ArrayList<>();

        Double bankToWalletCharges = getBankToWalletCharges(fundDTO.getAmount(), fundDTO.getSpecificChannel());
        Double vatFee = getVATFee(bankToWalletCharges);

        JournalLineDTO journalLine2DTO = new JournalLineDTO();
        WalletAccount debitAccount = walletAccountService.findOneByAccountNumber(fundDTO.getSourceAccountNumber());
        journalLine2DTO.setAccount(debitAccount);
        journalLine2DTO.setCredit(zeroFee);
        journalLine2DTO.setDebit(fundDTO.getAmount());

        JournalLineDTO journalLine5DTO = new JournalLineDTO();
        WalletAccount transitAccount = walletAccountService.findOneByAccountNumber(TRANSIT_ACCOUNT);
        journalLine5DTO.setAccount(transitAccount);
        journalLine5DTO.setDebit(zeroFee);
        journalLine5DTO.setCredit(fundDTO.getAmount());
        journalLine5DTO.setChannel(fundDTO.getSpecificChannel());

        status = setFundDTOStatus(fundDTO, status);

        String memo = "Transfer of " + utility.formatMoney(fundDTO.getAmount() + bankToWalletCharges);


        fundDTO.setAmount(fundDTO.getAmount() + bankToWalletCharges);
//        SinglePaymentResponse singlePaymentResponse = sendToBankAccount(fundDTO);
        fundDTO.setAmount(fundDTO.getAmount() - bankToWalletCharges);

//        System.out.println("SINGLE PAYMENT ===\n\n\n\n " + singlePaymentResponse);

//        boolean isSuccessful = true;
//        if ("success".equalsIgnoreCase(singlePaymentResponse.getStatus()) && singlePaymentResponse.getData() != null) {
//            fundDTO.setRrr(singlePaymentResponse.getData().getRrr());
//        } else {
//            isSuccessful = false;
//        }


        JournalLineDTO journalLine6DTO = new JournalLineDTO();
        journalLine6DTO.setAccount(transitAccount);
        journalLine6DTO.setChannel(fundDTO.getSpecificChannel());
        journalLine6DTO.setDebit(fundDTO.getAmount());
        journalLine6DTO.setCredit(zeroFee);

        status = TransactionStatus.INCOMPLETE;
        status = setFundDTOStatus(fundDTO, status);

        JournalLineDTO journalLine1DTO = new JournalLineDTO();
        WalletAccount creditAccount = walletAccountService.findOneByAccountNumber(CORRESPONDENCE_ACCOUNT);
        journalLine1DTO.setAccount(creditAccount);
        journalLine1DTO.setCredit(fundDTO.getAmount() + bankToWalletCharges);
        journalLine1DTO.setDebit(zeroFee);
        journalLine1DTO.setChannel(fundDTO.getChannel());


        JournalLineDTO journalLine3DTO = new JournalLineDTO();
        WalletAccount chargeAccount = walletAccountService.findOneByAccountNumber(POUCHII_INCOME_ACCT);
        journalLine3DTO.setAccount(chargeAccount);
        journalLine3DTO.setDebit(bankToWalletCharges - walletToBankCharge);
        journalLine3DTO.setCredit(zeroFee);

        JournalLineDTO journalLine7DTO = new JournalLineDTO();
        WalletAccount vatAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
        journalLine7DTO.setAccount(vatAccount);
        journalLine7DTO.setDebit(vatFee);
        journalLine7DTO.setCredit(zeroFee);


        JournalLineDTO journalLine4DTO = new JournalLineDTO();
        WalletAccount remitaIncomeAccount = walletAccountService.findOneByAccountNumber(RPSL_INTERBANK_SERVICES_ACCT);
        journalLine4DTO.setAccount(remitaIncomeAccount);
        journalLine4DTO.setDebit(walletToBankCharge);
        journalLine4DTO.setCredit(zeroFee);


        lines.add(journalLine1DTO);
        lines.add(journalLine3DTO);
        lines.add(journalLine4DTO);
        lines.add(journalLine6DTO);
        lines.add(journalLine7DTO);

        memo = memo + " To " + fundDTO.getBeneficiaryName();

        status = TransactionStatus.COMPLETED;
        status = setFundDTOStatus(fundDTO, status);

        if (debitAccount != null && debitAccount.getAccountOwner() != null) {
            memo = memo + " From " + fundDTO.getSourceAccountName() + ". Payment Transaction Ref : " + fundDTO.getTransRef();
        } else if (debitAccount != null) {
            memo = memo + " From " + fundDTO.getSourceAccountName() + ". Payment Transaction Ref : " + fundDTO.getTransRef();

        }

        lines.add(journalLine2DTO);
        lines.add(journalLine5DTO);

        String doubleEntryReturn = doubleEntry(lines, memo, bankToWalletCharges, status, fundDTO, vatFee);
        try {
            transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return doubleEntryReturn;

    }

    private String debitWallet(FundDTO fundDTO) {

        TransactionStatus status = TransactionStatus.START;

        status = setFundDTOStatus(fundDTO, TransactionStatus.START);

        String memo = "Transfer of " + utility.formatMoney(fundDTO.getAmount());
        ArrayList<JournalLineDTO> lines = new ArrayList<>();

        Double intraFee = getTransactionFee(fundDTO.getAmount(), fundDTO.getSpecificChannel(), fundDTO);
        Double vatFee = getVATFee(intraFee);

        out.println(" The source account ===" + fundDTO.getSourceAccountNumber() + " destination===" + fundDTO.getAccountNumber());

        WalletAccount debitAccount = walletAccountService.findOneByAccountNumber(fundDTO.getSourceAccountNumber());
        JournalLineDTO journalLine1DTO = new JournalLineDTO();
        journalLine1DTO.setAccount(debitAccount);
        journalLine1DTO.setCredit(zeroFee);
        journalLine1DTO.setDebit(fundDTO.getAmount() + fundDTO.getBonusAmount() + intraFee + vatFee);
        String debitName = debitAccount.getAccountOwner() == null ? debitAccount.getAccountName() : debitAccount.getAccountOwner().getFullName();
        journalLine1DTO.setChannel(fundDTO.getChannel());

        JournalLineDTO journalLine2DTO = new JournalLineDTO();
        WalletAccount creditAccount = walletAccountService.findOneByAccountNumber(fundDTO.getAccountNumber());
        journalLine2DTO.setAccount(creditAccount);
        journalLine2DTO.setCredit(fundDTO.getAmount());
        journalLine2DTO.setDebit(zeroFee);

        memo = memo + " from " + debitName + " to correspondent account";

        memo = memo + ". Payment Transaction Ref : " + fundDTO.getTransRef();

        JournalLineDTO journalLine3DTO = new JournalLineDTO();
        WalletAccount chargesAccount = walletAccountService.findOneByAccountNumber(POUCHII_INCOME_ACCT);
        journalLine3DTO.setAccount(chargesAccount);
        journalLine3DTO.setCredit(intraFee);
        journalLine3DTO.setDebit(zeroFee);

        JournalLineDTO journalLine4DTO = new JournalLineDTO();
        WalletAccount vatAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
        journalLine4DTO.setAccount(vatAccount);
        journalLine4DTO.setCredit(vatFee);
        journalLine4DTO.setDebit(zeroFee);


        status = TransactionStatus.COMPLETED;
        status = setFundDTOStatus(fundDTO, status);

        lines.add(journalLine2DTO);
        lines.add(journalLine3DTO);
        lines.add(journalLine1DTO);
        lines.add(journalLine4DTO);

        String doubleEntryReturn = doubleEntry(lines, memo, intraFee, status, fundDTO, vatFee);
        try {
            transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return doubleEntryReturn;
    }

    public void treatInvoice(String reference, String action, String accountNumber) {
        out.println(" TTTTTTTTTTTTTTTT reference ==" + reference);
        List<JournalLine> journalLines = journalLineService.findByJounalReference(reference);

        out.println("Treat Invoice JournalLines ===> " + journalLines);

        for (JournalLine journalLine : journalLines) {
            WalletAccount walletAccount1 = journalLine.getWalletAccount();
            if (journalLine.getDebit() > 0 && walletAccount1 != null && walletAccount1.getAccountOwner() != null) {
                if (!StringUtils.isEmpty(accountNumber)) {
                    WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
                    out.println("Pouchii Account retrieved ===> " + walletAccount);
                    if (walletAccount != null) {

                        journalLine.setWalletAccount(walletAccount);

                        out.println("updated journalLine ==> " + journalLine);
                        journalLineService.save(journalLine);
                    }
                }
            }
        }


        Journal journal;
        Double creditAmount = null;
        WalletAccount debitAccount = null;
        WalletAccount creditAccount = null;

        FundDTO fundDTO = transactionLogService.findByTransRef(reference);

        if ("reject".equalsIgnoreCase(action)) {
            journal = !journalLines.isEmpty() ? journalLines.get(0).getJounal() : null;
            if (journal != null) {
                out.println("My journal memo here ==" + journal.getMemo());
                journal.setMemo(journal.getMemo() + " REJECTED");
                journal.setPaymentType(PaymentType.REJECTED);
                journal.setTransactionStatus(TransactionStatus.COMPLETED);
                journalService.save(journal);

                for (JournalLine journalLine : journalLines) {
                    if (journalLine.getCredit() > 0) {
                        if (journalLine.getWalletAccount().getAccountOwner() != null) {
                            creditAmount = journalLine.getAmount();
                            creditAccount = journalLine.getWalletAccount();
                        }
                    } else if (journalLine.getDebit() > 0) {
                        if (journalLine.getWalletAccount().getAccountOwner() != null) {
                            debitAccount = journalLine.getWalletAccount();
                        }
                    }
                }

                PushNotificationRequest request = new PushNotificationRequest();
                request.setChannel(creditAccount.getScheme().getScheme().toLowerCase());
                if (creditAmount != null && debitAccount != null && creditAccount != null && debitAccount.getAccountOwner() != null) {
                    out.println("ACCOUNT OWNER " + debitAccount.getAccountOwner());
                    request.setMessage(utility.formatMoney(creditAmount) + " requested from " + debitAccount.getAccountOwner().getFullName() + " has been rejected");
                    request.setTitle("Request for Money");
                    request.setToken(creditAccount.getAccountOwner().getDeviceNotificationToken());
                    request.setRecipient(creditAccount.getAccountOwner().getPhoneNumber());
                    pushNotificationService.sendPushNotificationToToken(request);
                }

                setFundDTOStatus(fundDTO, TransactionStatus.COMPLETED);

            }
        } else if ("approve".equalsIgnoreCase(action)) {
            journal = !journalLines.isEmpty() ? journalLines.get(0).getJounal() : null;

            for (JournalLine journalLine : journalLines) {
                if (journalLine.getCredit() > 0) {
                    journalLine.setCurrentBalance(Double.parseDouble(journalLine.getWalletAccount().getActualBalance()) + journalLine.getCredit());
                } else if (journalLine.getDebit() > 0) {
                    journalLine.setCurrentBalance(Double.parseDouble(journalLine.getWalletAccount().getActualBalance()) - journalLine.getDebit());
                }
                journalLineService.save(journalLine, journalLine.getJounal(), journalLine.getWalletAccount());
            }

            out.println(" journal ==== " + journal);
            if (journal != null) {
                out.println(" My journal memo here ==" + journal.getMemo());

//                journal.setMemo("Payment of Requested Money " + journal.getMemo());
                journal.setMemo(journal.getMemo());
                journal.setNarration(journal.getNarration());
                journal.setPaymentType(PaymentType.ACCEPTED_INVOICE);
                journal.setTransactionStatus(TransactionStatus.COMPLETED);
                journalService.save(journal);
            }

            for (JournalLine journalLine : journalLines) {

                out.println("CURRENT LINE ==> " + journalLine);

                try {

                    WalletAccount account = journalLine.getWalletAccount();
                    if (account != null) {
                        double balance = Double.parseDouble(account.getCurrentBalance());
                        double actualBalance = Double.parseDouble(account.getActualBalance());
                        out.println(" Balance before ====" + balance);
                        out.println(" Actual Balance before ====" + actualBalance);

                        if (journalLine.getCredit() > 0) {
                            balance = balance + journalLine.getCredit();
                            actualBalance = actualBalance + journalLine.getCredit();
                            if (journalLine.getWalletAccount().getAccountOwner() != null) {
                                creditAmount = journalLine.getCredit();
                                creditAccount = journalLine.getWalletAccount();
                                Profile accountOwner = creditAccount.getAccountOwner();

                                out.println("Profile AccountOwner ===> " + accountOwner);
                                try {
                                    if (journal != null && 3 != creditAccount.getScheme().getId()) {
                                        String displayMemo = journal.getDisplayMemo().replaceAll("₦", "NGN");
                                        userService.sendCreditAlert(accountOwner.getPhoneNumber(), journalLine.getCredit(),
                                            creditAccount.getAccountNumber(), displayMemo, actualBalance, journal.getTransDate());
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (journalLine.getDebit() > 0) {
                            balance = balance - journalLine.getDebit();
                            actualBalance = actualBalance - journalLine.getDebit();
                            if (journalLine.getWalletAccount().getAccountOwner() != null) {
                                debitAccount = journalLine.getWalletAccount();

                                Profile accountOwner = debitAccount.getAccountOwner();
                                try {
                                    if (journal != null && 3 != debitAccount.getScheme().getId()) {
                                        String displayMemo = journal.getDisplayMemo().replaceAll("₦", "NGN");
                                        userService.sendDebitAlert(accountOwner.getPhoneNumber(), journalLine.getDebit(),
                                            debitAccount.getAccountNumber(), displayMemo, actualBalance, journal.getTransDate(), fundDTO.getBonusAmount());
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        account.currentBalance(String.valueOf(balance));
                        account.setActualBalance(String.valueOf(actualBalance));
                        walletAccountService.save(account);
                        out.println(" Balance after ==== " + account.getCurrentBalance() + " Actual Balance ===" + account.getActualBalance() + "  account here === " + account);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            PushNotificationRequest request = new PushNotificationRequest();
            request.setChannel(creditAccount.getScheme().getScheme().toLowerCase());
            if (creditAmount != null && debitAccount != null && debitAccount.getAccountOwner() != null) {
                out.println("ACCOUNT OWNER " + debitAccount.getAccountOwner());
                request.setMessage(utility.formatMoney(creditAmount) + " requested from " + debitAccount.getAccountOwner().getFullName() + " has been approved");
                request.setTitle("Request for Money");
                request.setToken(creditAccount.getAccountOwner().getDeviceNotificationToken());
                request.setRecipient(creditAccount.getAccountOwner().getPhoneNumber());
                pushNotificationService.sendPushNotificationToToken(request);
            }

            setFundDTOStatus(fundDTO, TransactionStatus.COMPLETED);
        }

    }


    private FundDTO buildNewFundDTO(FundDTO fundDTO, String originalRef, BulkBeneficiaryDTO beneficiary, String newRef) {
        fundDTO.setBeneficiaryName(beneficiary.getBeneficiaryFullName());
        fundDTO.setRrr(originalRef);
        fundDTO.setTransRef(newRef);
        fundDTO.setStatus(TransactionStatus.START);
        fundDTO.setAccountNumber(beneficiary.getAccountNumber());
        fundDTO.setAmount(beneficiary.getAmount());
        fundDTO.setAmount(beneficiary.getAmount());
        fundDTO.setAccountNumber(beneficiary.getAccountNumber());
        fundDTO.setDestBankCode(beneficiary.getBankCode());
        fundDTO.setBeneficiaryName(beneficiary.getBeneficiaryFullName());
        fundDTO.setStatus(TransactionStatus.INTRANSIT);
        fundDTO.setSpecificChannel(SpecificChannel.SEND_MONEY.getName());
        return fundDTO;
    }

    private FundDTO buildHMFundDTO(FundDTO fundDTO, String originalRef, BulkBeneficiaryDTO beneficiary, String newRef) {
        fundDTO.setBeneficiaryName(beneficiary.getBeneficiaryFullName());
        fundDTO.setRrr(originalRef);
        fundDTO.setTransRef(newRef);
        fundDTO.setStatus(TransactionStatus.START);
        fundDTO.setAccountNumber(beneficiary.getAccountNumber());
        fundDTO.setAmount(beneficiary.getAmount());
        fundDTO.setAccountNumber(beneficiary.getAccountNumber());
        fundDTO.setDestBankCode(beneficiary.getBankCode());
        fundDTO.setBeneficiaryName(beneficiary.getBeneficiaryFullName());
        fundDTO.setStatus(TransactionStatus.INTRANSIT);
        fundDTO.setSpecificChannel(SpecificChannel.SEND_MONEY_HMPAYROLL.getName());
        return fundDTO;
    }

    private GenericResponseDTO polarisSendMoneyToOtherPoolAcct(FundDTO fundDTO) throws Exception {
        //using disburse
        String[] names = fundDTO.getSourceAccountName().split(" ");
        double amount = (fundDTO.getAmount() + fundDTO.getBonusAmount());

        PolarisVulteFundTransferDTO transferDTO = new PolarisVulteFundTransferDTO();
        transferDTO.setAmount(amount);
        transferDTO.setPhoneNumber(fundDTO.getPhoneNumber());
        transferDTO.setSourceAccountFirstName(names[0]);
        transferDTO.setSourceAccountLastName(names[1]);

        String accountNumber = fundDTO.getSourceAccountNumber();
        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
        if (walletAccount == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid source wallet account");
        }
        if (walletAccount.getScheme() == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid source wallet scheme");
        }
        Scheme sourceAccountScheme = walletAccount.getScheme();
        out.println("Wallet to bank source scheme ===> " + sourceAccountScheme);

        String destinationAccountNumber = fundDTO.getAccountNumber();
        WalletAccount destinationAccount = walletAccountService.findOneByAccountNumber(destinationAccountNumber);
        if (destinationAccount == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid source wallet account");
        }
        if (destinationAccount.getScheme() == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid source wallet scheme");
        }
        Scheme destinationAccountScheme = destinationAccount.getScheme();
        out.println("Wallet to bank destination scheme ===> " + destinationAccountScheme);
        //todo
        transferDTO.setDestinationAccountNumber(destinationAccountScheme.getAccountNumber());
        transferDTO.setDestinationBankCode(destinationAccountScheme.getBankCode());
        String transactionDescription = getTransactionDescription(sourceAccountScheme, fundDTO);
        transferDTO.setTransactionDescription(transactionDescription);
        transferDTO.setAccountNumber(sourceAccountScheme.getAccountNumber());
        transferDTO.setApiKey(sourceAccountScheme.getApiKey());
        transferDTO.setSecretKey(sourceAccountScheme.getSecretKey());
        transferDTO.setTransRef(fundDTO.getTransRef());


        out.println(" " + transferDTO);
        Profile sourceAccountOwner = walletAccount.getAccountOwner();
        if (sourceAccountOwner != null) {
            String phoneNumber = sourceAccountOwner.getPhoneNumber();
            User user = sourceAccountOwner.getUser();
            if (user != null) {
                Optional<String> primaryWalletIdOptional = utility.getPrimaryWalletId(phoneNumber, sourceAccountScheme, user);
                primaryWalletIdOptional.ifPresent(transferDTO::setCustomerRef);
            }
//        return polarisVulteService.fundTransfer(transferDTO);
        }
        return polarisVulteService.disburse(transferDTO);
    }

    private GenericResponseDTO polarisSendMoneyToBank(FundDTO fundDTO) throws Exception {
        //using disburse
        out.println("Using Polaris Disburse Endpoint");
        String[] names = fundDTO.getSourceAccountName().split(" ");
        double amount = (fundDTO.getAmount() + fundDTO.getBonusAmount());

        PolarisVulteFundTransferDTO transferDTO = new PolarisVulteFundTransferDTO();
        transferDTO.setAmount(amount);
        transferDTO.setPhoneNumber(fundDTO.getPhoneNumber());
        transferDTO.setDestinationBankCode(fundDTO.getDestBankCode());
        transferDTO.setSourceAccountFirstName(names[0]);
        transferDTO.setSourceAccountLastName(names[1]);

        String accountNumber = fundDTO.getSourceAccountNumber();
        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
        if (walletAccount == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid source wallet account");
        }
        if (walletAccount.getScheme() == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid source wallet scheme");
        }
        Scheme scheme = walletAccount.getScheme();
        out.println("Wallet to bank scheme ===> " + scheme);

        String transactionDescription = getTransactionDescription(scheme, fundDTO);
        transferDTO.setTransactionDescription(transactionDescription);
        transferDTO.setDestinationAccountNumber(fundDTO.getAccountNumber());
        transferDTO.setAccountNumber(scheme.getAccountNumber());
        transferDTO.setApiKey(scheme.getApiKey());
        transferDTO.setSecretKey(scheme.getSecretKey());
        transferDTO.setTransRef(fundDTO.getTransRef());


        out.println(" " + transferDTO);
        if (walletAccount.getAccountOwner() != null) {
            String phoneNumber = walletAccount.getAccountOwner().getPhoneNumber();
            User user = walletAccount.getAccountOwner().getUser();
            if (user != null) {
                Optional<String> primaryWalletIdOptional = utility.getPrimaryWalletId(phoneNumber, scheme, user);
                primaryWalletIdOptional.ifPresent(transferDTO::setCustomerRef);
            }
//        return polarisVulteService.fundTransfer(transferDTO);
        }
        return polarisVulteService.disburse(transferDTO);
    }

    private String getTransactionDescription(Scheme scheme, FundDTO fundDTO) {
        String sourceAccountName = fundDTO.getSourceAccountName();
        String sourceFirstName = null;
        if (utility.checkStringIsValid(sourceAccountName)) {
            int indexOfSourceNameFirstSpace = sourceAccountName.indexOf(" ");
            int indexOfSourceNameSecondSpace = sourceAccountName.indexOf(" ", indexOfSourceNameFirstSpace + 1);

            sourceFirstName = sourceAccountName
                .substring(indexOfSourceNameFirstSpace).trim().toUpperCase();
            if (indexOfSourceNameSecondSpace > 0) {
                sourceFirstName =
                    sourceAccountName.substring(indexOfSourceNameFirstSpace, indexOfSourceNameSecondSpace).trim();
            }
        }

        return "FRM " + sourceFirstName;
    }

    private void postBonusPointToCustomerNubanAccount(String wallencyIncomeAccountNo, String userAccountNumber,
                                                      String sourceAccountName, Double bonusPointAmount, String narration) {

        out.println("postBonusPointToCustomerNubanAccount ===> " + wallencyIncomeAccountNo + " ===> " + bonusPointAmount);
        if (bonusPointAmount > 0) {
            Optional<String> nubanOptional = utility.getWalletAccountNubanByAccountNumber(userAccountNumber);
            if (nubanOptional.isPresent()) {
                String userNuban = nubanOptional.get();
                out.println("Inside the if block ===> " + userNuban);

                Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
                if (asyncExecutor != null) {

                    asyncExecutor.execute(() -> {
                        out.println("Inside the balanceNubanAccounts executor ==> ");
                        try {

                            GenericResponseDTO genericResponseDTO =
                                cashConnectService.intraTransfer(wallencyIncomeAccountNo, userNuban, sourceAccountName,
                                    bonusPointAmount, narration);

                            out.println("postBonusPointToCustomerNubanAccount response ===> " + genericResponseDTO);
                        } catch (Exception e) {
                            e.printStackTrace();
                            out.println("postBonusPointToCustomerNubanAccount exception " + e.getLocalizedMessage());
                        }
                    });
                }
            }
        }
    }

    private void sendTransactionFailedAlert(FundDTO fundDTO, SinglePaymentResponse singlePaymentResponse) {

        String responseDescription = "Error from the bank";
        String transRef = "", rrr = "", paymentDate = "";
        transRef = fundDTO.getTransRef();


        if (singlePaymentResponse != null) {
            SinglePayment data = singlePaymentResponse.getData();
            if (data != null) {
                responseDescription = data.getResponseDescription();
                rrr = data.getRrr();
                paymentDate = data.getPaymentDate();

                Data data1 = data.getData();
                if (data1 != null) {
                    responseDescription = responseDescription + " - " + data1.getResponseDescription();
                }
            }
        }

        String transactionRRR = rrr != null ? rrr : fundDTO.getRrr();

        Map<String, String> emails = utility.getNotificationEmailMap();
        String now = utility.checkStringIsValid(paymentDate) ? paymentDate : LocalDate.now().toString();

        for (Map.Entry<String, String> entry : emails.entrySet()) {

            String msg = "Dear " + entry.getKey() + "," +
                "<br/>" +
                "<br/>" +
                "<br/>" +
                "This is to notify you that the transaction referenced below has failed with the following error: " + responseDescription +
                "<br/>" +
                "<br/>" +
                "<br/>" +
                "<b><u> TRANSACTION FAILURE: Send Money to Bank</u></b>" +
                "<br/>" +
                "<br/>" +
                "<br/>" +
                "<br/>" + "Transaction Date : " + now +
                "<br/>" + "Transaction ID : " + transRef +
                "<br/>" + "Transaction Amount : " + utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount()) +
                "<br/>" + "Remita Reference Number (RRR) : " + transactionRRR +
                "<br/>" + "Transaction Type : Send Money to Bank" +
                "<br/>" + "Username : " + fundDTO.getSourceAccountName() +
                "<br/>" + "Wallet ID : " + fundDTO.getSourceAccountNumber() +
                "<br/>" + "Phone Number : " + fundDTO.getPhoneNumber() +
                "<br/>" + "Failed System: RITS - Single Transfer API" +
                "<br/>" + "Reporting Error: " + responseDescription +
                "<br/>" + "Responsibility : Lara Olowolagba - Lead, Transaction Monitoring (+234 814 137 6819)" +
                "<br/>" +
                "<br/>" +
                "<br/>" + "<p><i><b>NOTICE!! </b></i> - Please investigate and ensure resolution as soon as possible</p>";


            utility.sendEmail(entry.getValue(), "TRANSACTION FAILURE", msg);
        }
    }

    public Double getCharges(Double amount, String specificChannel) {
        Double intraFee = getTransactionFee(amount, specificChannel, null);
        Double vat = getVATFee(intraFee);
        if (SpecificChannel.POLARIS_CARD_REQUEST.getName().equalsIgnoreCase(specificChannel)) {
            vat = getVATFee(amount);
        }
        double charge = intraFee + vat;
        return charge;
    }


    public Double getTransactionFee(Double amount, String specificChannel, FundDTO fundDTO) {//Pay RRR /Biller
        double fee = billerCharges;

        if (specificChannel.toLowerCase().contains("vtu") || specificChannel.toLowerCase().contains("data")) {
            fee = zeroFee;
        }

        if(fundDTO != null) {
            WalletAccount debitAccount = walletAccountService.findOneByAccountNumber(fundDTO.getSourceAccountNumber());
            if (StringUtils.containsIgnoreCase(debitAccount.getScheme().getScheme(), "PayMasta")
                || StringUtils.containsIgnoreCase(debitAccount.getScheme().getScheme(), "Forge")
                || StringUtils.containsIgnoreCase(debitAccount.getScheme().getScheme(), "Wynk")) {
                if (specificChannel.toLowerCase().contains("vtu")) {
                    return zeroFee;
                }
            }
        }

        if ("INVOICE".equalsIgnoreCase(specificChannel)) {
            return zeroFee;
        }

        if (SpecificChannel.REQUEST_MONEY.getName().equalsIgnoreCase(specificChannel) || SpecificChannel.IBILE_PAY.getName().equalsIgnoreCase(specificChannel)) {
            return zeroFee;
        }

        if (SpecificChannel.SEND_MONEY_INTRA.getName().equalsIgnoreCase(specificChannel)
            || SpecificChannel.FUND_WALLET_INTRA.getName().equalsIgnoreCase(specificChannel) || SpecificChannel.POLARIS_CARD_REQUEST.getName().equalsIgnoreCase(specificChannel)) {
            return zeroFee;
        }

        if (SpecificChannel.WALLENCY_SCHOOLS_FEES_PAYMENT.getName().equalsIgnoreCase(specificChannel) || SpecificChannel.WALLENCY_SCHOOLS_FEES_PAYMENT_MCU.getName().equalsIgnoreCase(specificChannel)) {
            return 100.0;
        }

        if (SpecificChannel.SEND_MONEY.getName().equalsIgnoreCase(specificChannel)) {
            if (amount <= 5000) {
                return firstRangeCharge;
            } else if (amount <= 50000) {
                return secondRangeCharge;
            } else {
                return thirdRangeCharge;
            }
        }

        if (SpecificChannel.SEND_MONEY_TO_BANKS.getName().equalsIgnoreCase(specificChannel)) {
            if (amount <= 5000) {
                return firstRangeCharge;
            } else if (amount <= 50000) {
                return secondRangeCharge;
            } else {
                return thirdRangeCharge;
            }
        }

        if (SpecificChannel.HM_PAYROLL.getName().equalsIgnoreCase(specificChannel)) {
            if (fundDTO != null) {
                return fundDTO.getBulkAccountNos()
                    .stream()
                    .mapToDouble(beneficiaryDTO -> {
                        double humanManagerProcessingFee = calculateHumanManagerProcessingFee();
                        Double humanManagerProcessingFeeVAT = getVATFee(humanManagerProcessingFee);
                        Double transactionFee = getTransactionFee(beneficiaryDTO.getAmount(), SpecificChannel.SEND_MONEY_HMPAYROLL.getName(), null);
                        Double vatFee = getVATFee(transactionFee);
                        return transactionFee + vatFee + humanManagerProcessingFee + humanManagerProcessingFeeVAT;
                    }).sum();
            }
        }

        if (SpecificChannel.SEND_MONEY_HMPAYROLL.getName().equalsIgnoreCase(specificChannel)) {
            if (HUMAN_MANAGER_PROMO) return 25.0;
            else return getTransactionFee(amount, SpecificChannel.SEND_MONEY.getName(), null);
        }

        if (SpecificChannel.HM_PAYROLL_INTRA.getName().equalsIgnoreCase(specificChannel)) {
            if (fundDTO != null) {
                return fundDTO.getBulkAccountNos()
                    .stream()
                    .mapToDouble(beneficiaryDTO -> {
                        double humanManagerProcessingFee = calculateHumanManagerProcessingFee();
                        Double humanManagerProcessingFeeVAT = getVATFee(humanManagerProcessingFee);
                        Double transactionFee = getInterbankPaymentProcessingFeesWallet();
                        Double vatFee = getVATFee(transactionFee);
                        return transactionFee + vatFee + humanManagerProcessingFee + humanManagerProcessingFeeVAT;
                    }).sum();
            }
        }

        if (SpecificChannel.LIBERTY.getName().equalsIgnoreCase(specificChannel)) {
            return 25.0;
        }

        if (SpecificChannel.PAYMASTA_WALLET_TO_WALLET.getName().equalsIgnoreCase(specificChannel)
            || SpecificChannel.FORGE_WALLET_TO_WALLET.getName().equalsIgnoreCase(specificChannel)
            || SpecificChannel.WYNK_WALLET_TO_WALLET.getName().equalsIgnoreCase(specificChannel)) {
            if (amount <= 5000) {
                return 10.0;
            } else if (amount <= 50000) {
                return 20.0;
            } else {
                return 30.0;
            }
        }

        if(SpecificChannel.PAY_CABLE_TV_ITEX.getName().equalsIgnoreCase(specificChannel)
        ||SpecificChannel.PAY_INTERNET_ITEX.getName().equalsIgnoreCase(specificChannel)
            ||SpecificChannel.STARTIMES_TV.getName().equalsIgnoreCase(specificChannel)
            ||specificChannel.toLowerCase().contains("disco")){
            return 50.0;
        }

        return fee;
    }

    public Double getBankToWalletCharges(Double amount, String specificChannel) {

        if (SpecificChannel.THIRD_PARTY.getName().equalsIgnoreCase(specificChannel)) {
            return zeroFee;
        }

        if (SpecificChannel.CORAL_PAY.getName().equalsIgnoreCase(specificChannel)) {
            return 20.0;
        }

        if (SpecificChannel.PAY_WITH_USSD.getName().equalsIgnoreCase(specificChannel)) {
            return ussdCharge;
        }

        double charges = bankToWalletPercentage * amount;

        if (SpecificChannel.PAY_WITH_REMITA.getName().equalsIgnoreCase(specificChannel)) {
            if (amount <= 5000) {
                charges = firstRangeCharge;
            } else if (amount > 5000 && amount <= 50000) {
                charges = secondRangeCharge;
            } else if (amount > 50000) {
                charges = thirdRangeCharge;
            }
        }

        if (SpecificChannel.FUND_WALLET_CASHCONNECT.equals(specificChannel)) {
            return zeroFee;
        }

        if (charges > cappedAmount) {
            charges = cappedAmount;
        }
        return charges * bankToWalletShare;
    }

    private double calculateIntroducerFee(double transactionFee) {
        return transactionFee * introducerFeePercentage;
    }

    private double calculateVATPayable(double ssIncomeExcludingVAT) {
        return ssIncomeExcludingVAT * vatPayablePercentage;
    }


    private double calculateSSIncludeVAT(double spread) {
        return (spread * grossIncomePercentage);
    }

    private double calculateSSIncomeExcludingVAT(double ssIncludeVAT) {
        return ssIncludeVAT / 1.075;
    }

    private double calculatePolarisPartneringFee(double spread) {
        return partneringFeePercentage * spread;
    }

    private double calculatePolarisPomengranateFee(double spread) {
        return pomengranateFeePercentage * spread;
    }

    private double getNibbsCostOfTransaction() {
        return nibbsCostOfTransaction;
    }

    private double calculateHumanManagerProcessingFee() {
        if (HUMAN_MANAGER_PROMO) humanManagerProcessingFee = 0.0;
        return humanManagerProcessingFee;
    }

    private Double getInterbankPaymentProcessingFeesWallet() {
        return interbankPaymentProcessingFeesWallet;
    }

/*
    private String getObjectAsJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    private String sendCallBack(String url, Object payload) {
        System.out.println("Callback initial request payload ====> " + payload);

        if (payload == null) {
            return "fail";
        }

        try {
            String requestBody = getObjectAsJsonString(payload);
            System.out.println("Get obj requestBody ==>" + requestBody);

            HttpResponse<JsonNode> httpResponse = postCallBackURL(url, requestBody);
            out.println("Post call back response body " + httpResponse.getBody());
            out.println("Post call back response status text " + httpResponse.getStatusText());
            out.println("Post call back response status " + httpResponse.getStatus());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    //    private ResponseEntity<String> postCallBackURL(String url, HttpEntity httpEntity) throws InterruptedException {
    private HttpResponse<JsonNode> postCallBackURL(String url, String body) throws InterruptedException, IllegalAccessException {
        out.println("Url ===> " + url);
        if (utility.checkStringIsNotValid(url)) {
            throw new IllegalAccessException("Invalid url configured " + url);
        }
        boolean isFailing = true;
//        ResponseEntity<String> responseEntity = null;
        HttpResponse<JsonNode> httpResponse = null;
        int count = 0;
        do {
            try {
                httpResponse = Unirest
                    .post(url)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asJson();
                System.out.println("Callback response entity ====> " + httpResponse);
                System.out.println("Callback response entity status text ====> " + httpResponse.getStatusText());
                System.out.println("Callback response entity status ====> " + httpResponse.getStatus());
                System.out.println("Callback response entity body ====> " + httpResponse.getBody());
                System.out.println("Callback response entity headers ====> " + httpResponse.getHeaders());
                if (200 == httpResponse.getStatus()) {
                    String responseBody = httpResponse.getBody().toString();
                    System.out.println("Callback response Body from scheme endpoint ====> " + responseBody);
                    if (responseBody != null) {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        System.out.println("Callback response from scheme endpoint ==>" + jsonObject);
                    }
                    isFailing = false;
                } else {
                    out.println("post Call back failed!! ===> " + httpResponse.getStatus());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (isFailing) {
                Thread.sleep(300000); //try again in 5 mins if ailed
            }
            count++;
        } while (isFailing && count < 10);

        out.println("Final callBack response entity ====> " + httpResponse);

//        return responseEntity;
        return httpResponse;
    }

*/

    //Get School Card Request Commission
    public double getSchoolCardCommission(double amount, String school) {
        double commission = 0.0;
        if (school.equalsIgnoreCase("MCU")) {
            return 250.0;
        }
        return commission;
    }

    public String cardRequestProcessing(FundDTO fundDTO) throws Exception {
        TransactionStatus status = setFundDTOStatus(fundDTO, TransactionStatus.START);

        String memo = "Transfer of " + utility.formatMoney(fundDTO.getAmount() + fundDTO.getBonusAmount());
        ArrayList<JournalLineDTO> lines = new ArrayList<>();
        JournalLineDTO sourceAccountDebitLine = new JournalLineDTO();

        String[] r = fundDTO.getRrr().split("/");
        String cardType = r[0];
        String school = r[1];
        Double transactionFee;

        String specificChannel = fundDTO.getSpecificChannel();
        if (utility.checkStringIsValid(String.valueOf(fundDTO.getCharges()))) {
            transactionFee = fundDTO.getCharges();
        } else {
            transactionFee = getTransactionFee(fundDTO.getAmount() + fundDTO.getBonusAmount(), specificChannel, fundDTO);
        }

        Double vatFee = getVATFee(fundDTO.getAmount());

        Double feeToDebit = transactionFee + vatFee;
        double partneringBankFee = getPolarisFeeForCard(cardType); //Polaris Card fee
        double schoolCommisionFee = getSchoolCardCommission(fundDTO.getAmount(), school);
        double netIncome = fundDTO.getAmount() - partneringBankFee - schoolCommisionFee;

        out.printf(" The source account === %s destination === %s%n", fundDTO.getSourceAccountNumber(), fundDTO.getAccountNumber());
        WalletAccount debitAccount = walletAccountService.findOneByAccountNumber(fundDTO.getSourceAccountNumber());
//        WalletAccount creditAccount = walletAccountService.findOneByAccountNumber(fundDTO.getAccountNumber());

        if (debitAccount != null /*&& creditAccount != null*/) {
            // POLARIS CARD REQUEST
            if (SpecificChannel.POLARIS_CARD_REQUEST.getName().equalsIgnoreCase(specificChannel)) {

                double overallTransactionAmount = fundDTO.getAmount() + feeToDebit;

                sourceAccountDebitLine.setAccount(debitAccount);
                sourceAccountDebitLine.setCredit(zeroFee);
                sourceAccountDebitLine.setDebit(overallTransactionAmount);
                sourceAccountDebitLine.setChannel(fundDTO.getChannel());
                lines.add(sourceAccountDebitLine);

                //Credit transit account
                JournalLineDTO transitAccountCreditLine = new JournalLineDTO();
                WalletAccount transitAccount = walletAccountService.findOneByAccountNumber(TRANSIT_ACCOUNT);
                transitAccountCreditLine.setAccount(transitAccount);
                transitAccountCreditLine.setCredit(overallTransactionAmount);
                transitAccountCreditLine.setDebit(zeroFee);
                lines.add(transitAccountCreditLine);

                status = setFundDTOStatus(fundDTO, TransactionStatus.INTRANSIT);
                boolean isSuccessful = true;

                FundDTO polarisFundDTO = new FundDTO();
                polarisFundDTO.setAccountNumber(CARD_CHARGES_ACCT);
                polarisFundDTO.setTransRef(utility.getUniqueTransRef());
                polarisFundDTO.setAmount(partneringBankFee);
                polarisFundDTO.setDestBankCode("076");
                polarisFundDTO.setSourceAccountName(fundDTO.getSourceAccountName());
                polarisFundDTO.setSourceAccountNumber(fundDTO.getSourceAccountNumber());
                polarisFundDTO.setPhoneNumber(fundDTO.getPhoneNumber());

                GenericResponseDTO genericResponseDTO = polarisSendMoneyToBank(polarisFundDTO);
                out.println("Polaris send money to bank (card request) response ===> " + genericResponseDTO);

                if (genericResponseDTO == null || !HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                    isSuccessful = false;
                }

                if (isSuccessful) {
                    //Debit transit account
                    JournalLineDTO transitAccountDebitLine = new JournalLineDTO();
                    transitAccountDebitLine.setAccount(transitAccount);
                    transitAccountDebitLine.setDebit(overallTransactionAmount);
                    transitAccountDebitLine.setCredit(zeroFee);
                    transitAccountDebitLine.setChannel(specificChannel);
                    lines.add(transitAccountDebitLine);

                    status = TransactionStatus.INCOMPLETE;

                    JournalLineDTO correspondenceAccountCreditLine = new JournalLineDTO();
                    WalletAccount creditAccount = walletAccountService.findOneByAccountNumber(CORRESPONDENCE_ACCOUNT);
                    correspondenceAccountCreditLine.setAccount(creditAccount);
                    correspondenceAccountCreditLine.setCredit(partneringBankFee);
                    correspondenceAccountCreditLine.setDebit(zeroFee);
                    correspondenceAccountCreditLine.setChannel(fundDTO.getChannel());
                    correspondenceAccountCreditLine.setDestinationAccountNumber(fundDTO.getAccountNumber());
                    lines.add(correspondenceAccountCreditLine);

                    if (vatFee > 0.0) {
                        JournalLineDTO vatPayableAccountCreditLine = new JournalLineDTO();
                        WalletAccount vatAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
                        vatPayableAccountCreditLine.setAccount(vatAccount);
                        vatPayableAccountCreditLine.setCredit(vatFee);
                        vatPayableAccountCreditLine.setDebit(zeroFee);
                        lines.add(vatPayableAccountCreditLine);
                    }

                    if (schoolCommisionFee > 0.0) {
                        JournalLineDTO polarisCommissionCreditLine = new JournalLineDTO();
                        WalletAccount schoolCommissionAccount = walletAccountService.findOneByAccountNumber(getCardInstitutionCommissionAcct(school));
                        polarisCommissionCreditLine.setAccount(schoolCommissionAccount);
                        polarisCommissionCreditLine.setCredit(schoolCommisionFee);
                        polarisCommissionCreditLine.setDebit(zeroFee);
                        lines.add(polarisCommissionCreditLine);
                    }

                    JournalLineDTO feeAccountCreditLine = new JournalLineDTO();
                    WalletAccount chargesAccount = walletAccountService.findOneByAccountNumber(POUCHII_INCOME_ACCT);
                    feeAccountCreditLine.setCredit(netIncome);
                    feeAccountCreditLine.setAccount(chargesAccount);
                    feeAccountCreditLine.setDebit(zeroFee);
                    lines.add(feeAccountCreditLine);

                    memo = getMemoForWalletToWallet(fundDTO, memo, fundDTO.getSourceAccountName(), fundDTO.getBeneficiaryName());
                    memo = String.format("%s. Payment Transaction Ref : %s", memo, fundDTO.getTransRef());

                    status = TransactionStatus.COMPLETED;
                    status = setFundDTOStatus(fundDTO, status);

                    String doubleEntryReturn = doubleEntry(lines, memo, transactionFee, status, fundDTO, vatFee);
                    transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
                    return doubleEntryReturn;
                }

                memo = getMemoForWalletToWallet(fundDTO, memo, fundDTO.getSourceAccountName(), fundDTO.getBeneficiaryName());

                memo = memo + ". Payment Transaction Ref : " + fundDTO.getTransRef();

                JournalLineDTO sourceAccountReversalLine = new JournalLineDTO();
                sourceAccountReversalLine.setAccount(debitAccount);
                sourceAccountReversalLine.setCredit(overallTransactionAmount);
                sourceAccountReversalLine.setDebit(zeroFee);
                sourceAccountReversalLine.setChannel(fundDTO.getChannel());

                JournalLineDTO transitAccountReversalLine = new JournalLineDTO();
                transitAccountReversalLine.setAccount(transitAccount);
                transitAccountReversalLine.setCredit(zeroFee);
                transitAccountReversalLine.setDebit(overallTransactionAmount);

                lines.add(sourceAccountDebitLine);
                lines.add(transitAccountCreditLine);
                lines.add(sourceAccountReversalLine);
                lines.add(transitAccountReversalLine);
                status = TransactionStatus.REVERSED;
                status = setFundDTOStatus(fundDTO, status);

                String doubleEntryReturn = doubleEntry(lines, memo, transactionFee, status, fundDTO, vatFee);
                try {
                    transactionCallback(fundDTO, fundDTO.getSpecificChannel(), fundDTO.getChannel(), status);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return doubleEntryReturn;
            }
        }

        return "Source Account or Debit Account cannot be null";
    }

    private double getPolarisFeeForCard(String cardType) {
        if (cardType.equalsIgnoreCase("MULTIFUNCTIONAL")) {
            return 720.0;
        } else {
            return 550.0;
        }
    }

    private String getCardInstitutionCommissionAcct(String school) {
        if (school.equalsIgnoreCase("MCU")) {
            return MCU_COMMISSION_ACCT;
        }
        return "";
    }

    private double getPouchiiSchoolFeesCommision(FundDTO fundDTO) {
        double commission = 0.0;
        if (fundDTO.getRrr().equalsIgnoreCase("MCU")) {
            return commission;
        }
        return commission;
    }

}
