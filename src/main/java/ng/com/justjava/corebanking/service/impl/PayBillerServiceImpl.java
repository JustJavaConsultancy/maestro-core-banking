package ng.com.justjava.corebanking.service.impl;

import com.google.gson.Gson;
import ng.com.justjava.corebanking.domain.*;
import ng.com.justjava.corebanking.repository.BillerRecurringRepository;
import ng.com.justjava.corebanking.repository.InitiateBillerTransactionRepository;
import ng.com.justjava.corebanking.service.accounting.AccountingService;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.kafka.producer.TransProducer;
import ng.com.justjava.corebanking.service.mapper.BillerInitiateTransactionMapper;
import ng.com.justjava.corebanking.client.CamelIntegrationClient;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.justjava.corebanking.service.PayBillerService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.justjava.corebanking.util.Utility;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ng.com.justjava.corebanking.service.dto.stp.ExtendedConstants.ResponseCode.INVALID_ACCOUNT_STATUS;


@Service
public class PayBillerServiceImpl implements PayBillerService {

    private final Logger log = LoggerFactory.getLogger(PayBillerServiceImpl.class);
    private final CamelIntegrationClient camelIntegrationClient;
    private final Utility utility;
    private final WalletAccountService walletAccountService;
    private final TransProducer producer;
    private final AccountingService accountingService;
    private final BillerRecurringRepository billerRecurringRepository;
    private final BillerInitiateTransactionMapper billerInitiateTransactionMapper;
    private final InitiateBillerTransactionRepository billerTransactionRepository;

    @Value("${app.constants.dfs.biller-payment-account}")
    private String BILLER_PAYMENT_ACCOUNT;

    public PayBillerServiceImpl(CamelIntegrationClient camelIntegrationClient, Utility utility, WalletAccountService walletAccountService, TransProducer producer, AccountingService accountingService, BillerRecurringRepository billerRecurringRepository, BillerInitiateTransactionMapper billerInitiateTransactionMapper, InitiateBillerTransactionRepository billerTransactionRepository) {
        this.camelIntegrationClient = camelIntegrationClient;
        this.utility = utility;
        this.walletAccountService = walletAccountService;
        this.producer = producer;
        this.accountingService = accountingService;
        this.billerRecurringRepository = billerRecurringRepository;
        this.billerInitiateTransactionMapper = billerInitiateTransactionMapper;
        this.billerTransactionRepository = billerTransactionRepository;
    }

    @Override
    public List<PayBiller> getBillers() {

        List<PayBiller> billers = camelIntegrationClient.getBiller();
        log.debug("Billers size retrieved: {}", billers.size());

        if (billers.size() > 1) return billers;
        return null;
    }

    @Override
    public List<BillerCategoryNew> getBillerCategories() {

        List<BillerCategoryNew> billerCategories = camelIntegrationClient.getBillerCategories();
        log.debug("Billers category size retrieved: {}", billerCategories.size());

        if (billerCategories.size() > 1) return billerCategories;
        return null;
    }

    @Override
    public List<PayBiller> getBillerCategoryId(String categoryId) {
        log.info("getBillerCategoryId ==> {}", categoryId);
        return camelIntegrationClient.getBillerCategoryId(categoryId);
    }

    @Override
    public List<BillerProduct> getBillerProducts(String billerId) {

        List<BillerProduct> billerProducts = camelIntegrationClient.getBillerProducts(billerId);
        log.debug("Billers product size retrieved: {}", billerProducts.size());

        return billerProducts;
    }

    @Override
    public GenericResponseDTO getRRRDetails(Long rrr) {

        GenericResponseDTO responseDTO = new GenericResponseDTO();

        PayBillerRRR rrrDetails = camelIntegrationClient.getRRRDetails(rrr);
        log.debug("RRR details retrieved: {}", rrrDetails);
        if (rrrDetails != null) {
            responseDTO.setData(rrrDetails);
            responseDTO.setCode("00");
            responseDTO.setStatus(HttpStatus.OK);
            responseDTO.setMessage("Success");
            return responseDTO;
        }
        return new GenericResponseDTO("99", "Failed to retrieve RRR details.");
    }

    @Override
    public GenericResponseDTO validateAndPayBiller(InitiateBillerTransactionDTO initiateBillerTransactionDTO) {

        log.error("VALIDATE REQUEST \n ProductId ==> {}\nCusterId ==> {}",
            initiateBillerTransactionDTO.getBillPaymentProductId(), initiateBillerTransactionDTO.getCustomerId());

//        BillerValidateCustomer billerValidateCustomer =
//            validateCustomer(initiateBillerTransactionDTO.getBillPaymentProductId(), initiateBillerTransactionDTO.getCustomerId());

//        log.info("validate Customer Response ==> {}", billerValidateCustomer);

        try {
//            if (isValidCustomer(billerValidateCustomer) || initiateBillerTransactionDTO.getMetadata().getCustomFields().isEmpty()) {
            InitiateBillerTransaction initiateBillerTransaction =
                billerInitiateTransactionMapper.toInitiateBillerTransaction(initiateBillerTransactionDTO);
            BillerInitiateTransactionResponse billerInitiateTransactionResponse = initiateTransaction(initiateBillerTransaction);

            log.debug("BillerInitiateTransactionResponse  = " + billerInitiateTransactionResponse);

            if (billerInitiateTransactionResponse != null) {
                BillerPayRrrDTO billerPayRRRDTO = new BillerPayRrrDTO();
                billerPayRRRDTO.setSourceAccount(initiateBillerTransactionDTO.getAccountNumber());
                billerPayRRRDTO.setTotalAmount(initiateBillerTransactionDTO.getAmount().toString());
                billerPayRRRDTO.setBonusAmount(initiateBillerTransactionDTO.getBonusAmount());
                billerPayRRRDTO.setTransactionRef(initiateBillerTransactionDTO.getTransactionRef());
                billerPayRRRDTO.setNarration(initiateBillerTransactionDTO.getNarration());
                billerPayRRRDTO.setPhoneNumber(initiateBillerTransactionDTO.getPhoneNumber());
                billerPayRRRDTO.setRrr(billerInitiateTransactionResponse.getRrr());
                billerPayRRRDTO.setRedeemBonus(initiateBillerTransactionDTO.isRedeemBonus());

                return payRRR(billerPayRRRDTO);
            }

            return new GenericResponseDTO("99",
                HttpStatus.BAD_REQUEST, "Error initiating transaction", null);
//            }

//            return new GenericResponseDTO("99",
//                HttpStatus.BAD_REQUEST, "Error Validating Customer", null);
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return new GenericResponseDTO("78", HttpStatus.BAD_REQUEST, "failed", null);
    }

    @NotNull
    @Override
    public GenericResponseDTO payRRR(BillerPayRrrDTO payRrrDTO) {

        WalletAccount account = walletAccountService.findOneByAccountNumber(payRrrDTO.getSourceAccount());

        if (account == null) {

            return new GenericResponseDTO("25", HttpStatus.BAD_REQUEST,
                "Wallet account does not exist [ " + payRrrDTO.getSourceAccount() + " ]", null);
        }


        FundDTO fundDTO = new FundDTO();
        fundDTO.setAccountNumber(BILLER_PAYMENT_ACCOUNT);
        double amount = Double.parseDouble(String.valueOf(payRrrDTO.getTotalAmount())) - payRrrDTO.getBonusAmount();
        fundDTO.setAmount(amount);

        fundDTO.setDestBankCode("ABC");
        fundDTO.setSourceAccountNumber(payRrrDTO.getSourceAccount());
        fundDTO.setChannel("WalletToWallet");
        fundDTO.setTransRef(payRrrDTO.getTransactionRef());
        fundDTO.setSpecificChannel("payBiller");
        fundDTO.setNarration(payRrrDTO.getNarration());
        fundDTO.setPhoneNumber(payRrrDTO.getPhoneNumber());
        fundDTO.setRrr(payRrrDTO.getRrr());
        fundDTO.setBonusAmount(payRrrDTO.getBonusAmount());
        fundDTO.setRedeemBonus(payRrrDTO.isRedeemBonus());

        Double intraFee =
            accountingService.getTransactionFee(fundDTO.getAmount() + fundDTO.getBonusAmount(),
                fundDTO.getSpecificChannel(), null);

        if (Double.parseDouble(account.getCurrentBalance()) < fundDTO.getAmount() + intraFee) {
            return new GenericResponseDTO("15", HttpStatus.BAD_REQUEST, "Insufficient fund in wallet account [ " + payRrrDTO.getSourceAccount() + " ]", null);
        }

        ValidTransactionResponse validTransaction = utility.isValidTransaction(fundDTO.getChannel(), fundDTO.getSourceAccountNumber(), fundDTO.getAccountNumber(), fundDTO.getAmount(), fundDTO.getBonusAmount(), false);
        if (validTransaction.isValid()) {

            WalletAccount destinationAcct = walletAccountService.findOneByAccountNumber(BILLER_PAYMENT_ACCOUNT);
            String destinationName = destinationAcct.getAccountOwner() != null ? destinationAcct.getAccountOwner().getFullName() : destinationAcct.getAccountName();
            fundDTO.setBeneficiaryName(destinationName);

            WalletAccount sourceAcct = walletAccountService.findOneByAccountNumber(payRrrDTO.getSourceAccount());
            String sourceName = sourceAcct.getAccountOwner() != null ? sourceAcct.getAccountOwner().getFullName() : sourceAcct.getAccountName();
            fundDTO.setSourceAccountName(sourceName);

            log.debug("fundDTO after generic response  \n\n\n\n\n\n = " +
                fundDTO);

            producer.send(fundDTO);

            //Call Remita Notification Api
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("rrr", payRrrDTO.getRrr());
            jsonObject.put("transactionRef", payRrrDTO.getTransactionRef());
            jsonObject.put("amount", payRrrDTO.getTotalAmount());
            jsonObject.put("channel", "internetbanking");

            Map<String, String> fundingSource = new HashMap<>();
            fundingSource.put("fundingSource", "STSLBILLER");

            jsonObject.put("metadata", fundingSource);

            //TODO: REVERSE PAYMENT IF NOTIFICATION FAILS
            camelIntegrationClient.billPaymentNotification(jsonObject);
            return new GenericResponseDTO("00", HttpStatus.OK, "Biller payment successful", null);
        }
        return new GenericResponseDTO(INVALID_ACCOUNT_STATUS.getCode(),
            HttpStatus.BAD_REQUEST, validTransaction.getMessage(), null);
    }

    private BillerInitiateTransactionResponse initiateTransaction(InitiateBillerTransaction initiateBillerTransaction) {
        return camelIntegrationClient.initiateTransaction(initiateBillerTransaction);
    }

    private BillerValidateCustomer validateCustomer(String billPaymentProductId, String customerId) {
        return camelIntegrationClient.validateCustomer(billPaymentProductId, customerId);
    }

    @Override
    public GenericResponseDTO checkBalanceIsNotSufficient(String sourceAccountNo, double amount, double bonusAmount, Object obj, String specificChannel) {

        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(sourceAccountNo);
        if (walletAccount == null) {
            return new GenericResponseDTO("25", HttpStatus.BAD_REQUEST, "Wallet account does not exist [ " + sourceAccountNo + " ]", null);
        }
        Profile sourceAccountOwner = walletAccount.getAccountOwner();

        if (sourceAccountOwner == null) {
            return new GenericResponseDTO("26", HttpStatus.BAD_REQUEST, "Account Owner not found", null);
        }

        Double intraFee = accountingService.getTransactionFee(amount, specificChannel, null);

        if (Double.parseDouble(walletAccount.getCurrentBalance()) < amount + intraFee) {
            return new GenericResponseDTO("15", HttpStatus.BAD_REQUEST, "Insufficient fund in wallet account  [" + sourceAccountNo + " ]", null);
        }

        double totalBonus = sourceAccountOwner.getTotalBonus();


        if (totalBonus < bonusAmount && bonusAmount > 0) {
            return new GenericResponseDTO("50", HttpStatus.BAD_REQUEST, "Insufficient Amount in the BonusPot", obj);
        }
        return null;
    }

    @Override
    public GenericResponseDTO payRecurring(BillerRecurring billerRecurring) {
        log.info("Trying to make a recurring payment.");
        if (billerRecurring.getNext() == LocalDate.now() || billerRecurring.getRetry() == LocalDate.now()) {

            String rrr = billerRecurring.getRrr();
            if (rrr == null) {
                Long userId = billerRecurring.getUser().getId();
                InitiateBillerTransactionEntity transactionEntity = billerTransactionRepository.findByUserIdAndRecurringId(userId, billerRecurring.getId());

                String billerTransactionString = transactionEntity.getInitiateBillerTransaction();
                log.info("Deserialized InitiateBillerTransaction json string ==> {}", billerTransactionString);
                InitiateBillerTransaction initiateBillerTransaction = new Gson().fromJson(billerTransactionString, InitiateBillerTransaction.class);

                BillerInitiateTransactionResponse billerInitiateTransactionResponse = initiateTransaction(initiateBillerTransaction);
                rrr = billerInitiateTransactionResponse.getRrr();
                billerRecurring.setRrr(rrr);
                billerRecurringRepository.save(billerRecurring);
            }

            BillerPayRrrDTO billerPayRRRDTO = new BillerPayRrrDTO();
            billerPayRRRDTO.setSourceAccount(billerRecurring.getAccountNumber());
            billerPayRRRDTO.setTotalAmount(billerRecurring.getAmountToPay().toString());
            billerPayRRRDTO.setBonusAmount(0.0);
            billerPayRRRDTO.setTransactionRef(billerRecurring.getTransRef());
            billerPayRRRDTO.setNarration(billerRecurring.getNarration());
            billerPayRRRDTO.setPhoneNumber(billerRecurring.getUser().getLogin());
            billerPayRRRDTO.setRrr(billerRecurring.getRrr());
            billerPayRRRDTO.setRedeemBonus(false);

            GenericResponseDTO payRRRResponse = payRRR(billerPayRRRDTO);

            if (payRRRResponse.getCode().equalsIgnoreCase("00")) {

                Integer successCount = billerRecurring.getSuccessCount() + 1;
                billerRecurring.setSuccessCount(successCount);
                LocalDate next = billerRecurring.getNext();

                if (Objects.equals(billerRecurring.getSuccessCount(), billerRecurring.getNumberOfTimes())) {
                    billerRecurring.setStatus("REPAID");
                    billerRecurringRepository.save(billerRecurring);
                    return payRRRResponse;
                } else {

                    String paymentFrequency = billerRecurring.getPaymentFrequency();

                    switch (paymentFrequency) {
                        case "DAILY":
                            billerRecurring.setNext(next.plusDays(1));
                            billerRecurring.setRetry(null);
                            System.out.println("DAILY recurring payment incremented");
                            break;
                        case "WEEKLY":
                            billerRecurring.setNext(next.plusDays(7));
                            billerRecurring.setRetry(null);
                            System.out.println("WEEKLY recurring payment incremented");
                            break;
                        case "MONTHLY":
                            billerRecurring.setNext(next.plusDays(30));
                            billerRecurring.setRetry(null);
                            System.out.println("MONTHLY recurring payment incremented");
                            break;
                        case "YEARLY":
                            billerRecurring.setNext(next.plusDays(365));
                            billerRecurring.setRetry(null);
                            System.out.println("YEARLY recurring payment incremented");
                            break;
                    }
                }

                billerRecurring.setRrr(null); //clear rrr after a successful payment
                log.info("Saving recurring bill after a successful payment ==> {}", billerRecurring);
                BillerRecurring recurring = billerRecurringRepository.save(billerRecurring);
                log.info("Saved recurring bill after a successful payment ==> {}", recurring);

            } else {
                billerRecurring.setRetry(LocalDate.now());
            }
            return payRRRResponse;
        }

        return new GenericResponseDTO("99", HttpStatus.PAYMENT_REQUIRED, "Error occurred");
    }

    @Override
    public GenericResponseDTO scheduleRecurring(InitiateBillerTransactionDTO initiateBillerTransactionDTO, User user) {


//        BillerValidateCustomer billerValidateCustomer =
//            validateCustomer(initiateBillerTransactionDTO.getBillPaymentProductId(), initiateBillerTransactionDTO.getCustomerId());

//        if (billerValidateCustomer.getBillPaymentProductId() != null && billerValidateCustomer.getCustomerId() != null) {
            InitiateBillerTransaction initiateBillerTransaction = billerInitiateTransactionMapper.toInitiateBillerTransaction(initiateBillerTransactionDTO);


            BillerInitiateTransactionResponse billerInitiateTransactionResponse = initiateTransaction(initiateBillerTransaction);

            BillerRecurring billerRecurring = new BillerRecurring();
            billerRecurring.setStart(LocalDate.parse(initiateBillerTransactionDTO.getStartDate()));
            billerRecurring.setEnd(LocalDate.parse(initiateBillerTransactionDTO.getEndDate()));
            billerRecurring.setRrr(billerInitiateTransactionResponse.getRrr());
            billerRecurring.setSuccessCount(0);
            billerRecurring.setUser(user);
            billerRecurring.setStatus("ACTIVE");
            billerRecurring.setNumberOfTimes(initiateBillerTransactionDTO.getNumberOfTimes());
            billerRecurring.setNext(LocalDate.parse(initiateBillerTransactionDTO.getStartDate()));
            billerRecurring.setAmountToPay(billerInitiateTransactionResponse.getAmount());
            billerRecurring.setAccountNumber(initiateBillerTransactionDTO.getAccountNumber());
            billerRecurring.setPaymentFrequency(initiateBillerTransactionDTO.getPaymentFrequency());
            billerRecurring.setNarration(initiateBillerTransactionDTO.getNarration());
            billerRecurring.setRedeemBonus(initiateBillerTransactionDTO.isRedeemBonus());
            billerRecurring.setTransRef(initiateBillerTransactionDTO.getTransactionRef());

            log.info("Scheduling recurring bill payment ==> {}\n", billerRecurring);

            BillerRecurring savedRecurringBill = billerRecurringRepository.save(billerRecurring);
            log.info("Scheduled recurring bill payment ==> {}\n", savedRecurringBill);


            //SAVE TRANSACTION
            InitiateBillerTransactionEntity billerTransactionEntity = new InitiateBillerTransactionEntity();
            billerTransactionEntity.setUserId(user.getId());
            billerTransactionEntity.setRecurringId(savedRecurringBill.getId());
            billerTransactionEntity.setSourceAccount(initiateBillerTransactionDTO.getAccountNumber());
            billerTransactionEntity.setNarration(initiateBillerTransactionDTO.getNarration());
            billerTransactionEntity.setRedeemBonus(initiateBillerTransactionDTO.isRedeemBonus());
            billerTransactionEntity.setBonusAmount(initiateBillerTransactionDTO.getBonusAmount());
            billerTransactionEntity.setInitiateBillerTransaction(new Gson().toJson(initiateBillerTransaction));
            billerTransactionRepository.save(billerTransactionEntity);

            LocalDate today = LocalDate.now();
            if (Objects.equals(savedRecurringBill.getStart(), today)) {
                return payRecurring(savedRecurringBill);
            }

            return new GenericResponseDTO("00", HttpStatus.OK, "Recurring payment set successfully");
//        }
//        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Error validating customer");
    }

    @Override
    public boolean isValidCustomer(BillerValidateCustomer billerValidateCustomer) {
        return billerValidateCustomer.getBillPaymentProductId() != null && billerValidateCustomer.getCustomerId() != null;
    }
}
