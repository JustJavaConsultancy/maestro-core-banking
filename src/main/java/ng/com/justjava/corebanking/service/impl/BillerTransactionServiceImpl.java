package ng.com.justjava.corebanking.service.impl;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ng.com.justjava.corebanking.repository.BillerTransactionRepository;
import ng.com.justjava.corebanking.repository.UserRepository;
import ng.com.justjava.corebanking.security.SecurityUtils;
import ng.com.justjava.corebanking.service.accounting.AccountingService;
import ng.com.justjava.corebanking.service.kafka.producer.TransProducer;
import ng.com.justjava.corebanking.service.mapper.BillerTransactionMapper;
import ng.com.justjava.corebanking.config.AsyncConfiguration;
import ng.com.justjava.corebanking.domain.Biller;
import ng.com.justjava.corebanking.domain.BillerPlatform;
import ng.com.justjava.corebanking.domain.BillerTransaction;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.Scheme;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.SpecificChannel;
import ng.com.justjava.corebanking.service.BillerPlatformService;
import ng.com.justjava.corebanking.service.BillerTransactionService;
import ng.com.justjava.corebanking.service.TransactionLogService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.AgentAirtimeDTO;
import ng.com.justjava.corebanking.service.dto.BillerTransactionDTO;
import ng.com.justjava.corebanking.service.dto.BuyAirtimeDTO;
import ng.com.justjava.corebanking.service.dto.BuyDataDTO;
import ng.com.justjava.corebanking.service.dto.CustomFieldDTO;
import ng.com.justjava.corebanking.service.dto.FundDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.PayRRRDTO;
import ng.com.justjava.corebanking.service.dto.PaymentResponseDTO;
import ng.com.justjava.corebanking.service.dto.UtilDTO;
import ng.com.justjava.corebanking.service.dto.ValidTransactionResponse;
import ng.com.justjava.corebanking.service.dto.ValidateMeterResponseDTO;
import ng.com.justjava.corebanking.util.Utility;
import ng.com.systemspecs.remitabillinggateway.billers.GetBillerResponse;
import ng.com.systemspecs.remitabillinggateway.billers.GetBillerResponseData;
import ng.com.systemspecs.remitabillinggateway.configuration.Credentials;
import ng.com.systemspecs.remitabillinggateway.customfields.GetCustomFieldResponse;
import ng.com.systemspecs.remitabillinggateway.generaterrr.GenerateResponse;
import ng.com.systemspecs.remitabillinggateway.generaterrr.ResponseData;
import ng.com.systemspecs.remitabillinggateway.notification.BillNotificationResponse;
import ng.com.systemspecs.remitabillinggateway.notification.BillRequest;
import ng.com.systemspecs.remitabillinggateway.paymentstatus.GetTransactionStatusResponse;
import ng.com.systemspecs.remitabillinggateway.rrrdetails.GetRRRDetailsResponse;
import ng.com.systemspecs.remitabillinggateway.service.RemitaBillingGatewayService;
import ng.com.systemspecs.remitabillinggateway.service.impl.RemitaBillingGatewayServiceImpl;
import ng.com.systemspecs.remitabillinggateway.servicetypes.GetServiceResponse;
import ng.com.systemspecs.remitabillinggateway.servicetypes.GetServiceResponseData;
import ng.com.systemspecs.remitabillinggateway.util.EnvironmentType;
import ng.com.systemspecs.remitabillinggateway.validate.CustomField;
import ng.com.systemspecs.remitabillinggateway.validate.ResponseDatum;
import ng.com.systemspecs.remitabillinggateway.validate.ValidateRequest;
import ng.com.systemspecs.remitabillinggateway.validate.ValidateResponse;
import ng.com.systemspecs.remitabillinggateway.validate.Value;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ng.com.justjava.corebanking.service.dto.stp.ExtendedConstants.ResponseCode.INVALID_ACCOUNT_STATUS;
import static ng.com.justjava.corebanking.service.dto.stp.ExtendedConstants.ResponseCode.REQUEST_VALIDATION_FAILED;


/**
 * Service Implementation for managing {@link BillerTransaction}.
 */
@Service
@Transactional
public class BillerTransactionServiceImpl implements BillerTransactionService {

    private final Logger log = LoggerFactory.getLogger(BillerTransactionServiceImpl.class);

    private static final long Lower_Bond = 1000000000L;
    private static final long Upper_Bond = 9000000000L;

    @org.springframework.beans.factory.annotation.Value("${app.constants.remita.public-key}")
    private String REMITA_PUBLIC_KEY;

    @org.springframework.beans.factory.annotation.Value("${app.constants.remita.billing.secret-key}")
    private String REMITA_BILLING_SECRET_KEY;

    @org.springframework.beans.factory.annotation.Value("${app.constants.remita.billing.payment-url}")
    private String REMITA_BILLING_PAYMENT_URL;

    @org.springframework.beans.factory.annotation.Value("${app.constants.remita.billing.payment-auth-code}")
    private String REMITA_PAYMENT_AUTH_CODE;

    @org.springframework.beans.factory.annotation.Value("${app.constants.remita.billing.payment-channel}")
    private String REMITA_BILLING_PAYMENT_CHANNEL;

    @org.springframework.beans.factory.annotation.Value("${app.constants.remita.billing.teller-name}")
    private String REMITA_BILLING_TELLER_NAME;

    @org.springframework.beans.factory.annotation.Value("${app.constants.remita.billing.income-account}")
    private String REMITA_BILLING_INCOME_ACCOUNT;

    @org.springframework.beans.factory.annotation.Value("${app.constants.remita.billing.funding-source}")
    private String REMITA_BILLING_FUNDING_SOURCE;

    @org.springframework.beans.factory.annotation.Value("${app.constants.remita.billing.branch-code}")
    private String REMITA_BILLING_BRANCH_CODE;

    @org.springframework.beans.factory.annotation.Value("${app.constants.remita.notify-billing.secret-key}")
    private String REMITA_NOTIFY_SECRET_KEY;

    @org.springframework.beans.factory.annotation.Value("${app.constants.dfs.biller-payment-account}")
    private String BILLER_PAYMENT_ACCOUNT;

    String kazeem = "akinrinde@justjava.com.ng";
    String tunji = "moronkola@systemspecs.com.ng";
    String bolaji = "ogeyingbo@systemspecs.com.ng";
    String demola = "igbalajobi@systemspecs.com.ng";
    String Ozioma = "enechukwu@systemspecs.com.ng";
    String Mike = "oshadami@systemspecs.com.ng";
    String Tokunbo = "omonubi@systemspecs.com.ng";
    String Ameze = "ogunfuwa@systemspecs.com.ng";
    String Maryam = "maliki@systemspecs.com.ng";
    String Seun = "adesanya@systemspecs.com.ng";
    String Adeibukun = "aadeniyi@systemspecs.com.ng";


    private final BillerTransactionRepository billerTransactionRepository;

    private final BillerTransactionMapper billerTransactionMapper;
    private final AsyncConfiguration asyncConfiguration;
    private final TransProducer producer;
    private final BillerPlatformService billerPlatformService;
    private final Utility utility;
    private final UserRepository userRepository;
    private final WalletAccountService walletAccountService;
    private final AccountingService accountingService;
    private final TransactionLogService transactionLogService;
    private User theUser;

    public BillerTransactionServiceImpl(BillerTransactionRepository billerTransactionRepository,
                                        BillerTransactionMapper billerTransactionMapper,
                                        AsyncConfiguration asyncConfiguration, TransProducer producer,
                                        BillerPlatformService billerPlatformService, @Lazy Utility utility,
                                        UserRepository userRepository,
                                        WalletAccountService walletAccountService, @Lazy AccountingService accountingService, TransactionLogService transactionLogService) {

        this.billerTransactionRepository = billerTransactionRepository;
        this.billerTransactionMapper = billerTransactionMapper;
        this.asyncConfiguration = asyncConfiguration;
        this.producer = producer;
        this.billerPlatformService = billerPlatformService;
        this.utility = utility;
        this.userRepository = userRepository;
        this.walletAccountService = walletAccountService;
        this.accountingService = accountingService;
        this.transactionLogService = transactionLogService;
    }

    @Override
    public RemitaBillingGatewayService getRemitaBillingGatewayService(String transactionId) {
        if (transactionId == null) {
            transactionId = String.valueOf(System.currentTimeMillis());
        }
        Credentials credentials = new Credentials();
        credentials.setPublicKey(REMITA_PUBLIC_KEY);
        credentials.setEnvironment(EnvironmentType.LIVE);
        credentials.setTransactionId(transactionId);
        credentials.setSecretKey(REMITA_BILLING_SECRET_KEY);
        log.debug("Transaction Id = " + credentials.getTransactionId());
        return new RemitaBillingGatewayServiceImpl(credentials);
    }

    @Override
    public RemitaBillingGatewayService notifyBillingGatewayService(String transactionId) {
        if (transactionId == null) {
            transactionId = String.valueOf(System.currentTimeMillis());
        }
        Credentials credentials = new Credentials();
        credentials.setPublicKey(REMITA_PUBLIC_KEY);
        credentials.setSecretKey(REMITA_NOTIFY_SECRET_KEY);
        credentials.setTransactionId(transactionId);
        credentials.setEnvironment(EnvironmentType.LIVE);
        log.debug("Transaction Id = " + credentials.getTransactionId());

        return new RemitaBillingGatewayServiceImpl(credentials);
    }

    @Override
    public BillerTransactionDTO save(BillerTransactionDTO billerTransactionDTO) {
        log.debug("Request to save BillerTransaction : {}", billerTransactionDTO);
        BillerTransaction billerTransaction = billerTransactionMapper.toEntity(billerTransactionDTO);
        billerTransaction = billerTransactionRepository.save(billerTransaction);
        return billerTransactionMapper.toDto(billerTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BillerTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BillerTransactions");
        return billerTransactionRepository.findAll(pageable)
            .map(billerTransactionMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BillerTransactionDTO> findOne(Long id) {
        log.debug("Request to get BillerTransaction : {}", id);
        return billerTransactionRepository.findById(id)
            .map(billerTransactionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillerTransaction : {}", id);
        billerTransactionRepository.deleteById(id);
    }

    @Override
    public GetRRRDetailsResponse getRRRDetails(String rrr, String transId) {
        RemitaBillingGatewayService gatewayService = getRemitaBillingGatewayService(transId);
        return gatewayService.getRRRDetails(rrr);
    }

    @Override
    public ValidateResponse validate(ValidateRequest validateRequest, String transId) {
        RemitaBillingGatewayService gatewayService = getRemitaBillingGatewayService(transId);
        return gatewayService.validate(validateRequest);
    }

    @Override
    public GenerateResponse generateRRR(ValidateRequest validateRequest, String transId) {
        RemitaBillingGatewayService gatewayService = getRemitaBillingGatewayService(transId);
        return gatewayService.generateRRR(validateRequest);
    }

    @Override
    public GenericResponseDTO payRRR(PayRRRDTO payRRRDTO) {
        log.info("PAY RRR payload " + payRRRDTO);

        String sourceAccountNo = payRRRDTO.getSourceAccountNo();
        Double amount = payRRRDTO.getAmount();
        double bonusAmount = payRRRDTO.getBonusAmount();

        GenericResponseDTO balanceIsSuficientResponse = checkBalanceIsNotSufficient(sourceAccountNo, amount, bonusAmount, payRRRDTO, SpecificChannel.PAY_RRR.getName());
        if (balanceIsSuficientResponse != null) return balanceIsSuficientResponse;

        WalletAccount account = walletAccountService.findOneByAccountNumber(String.valueOf(payRRRDTO.getSourceAccountNo()));

        Profile sourceAccountOwner = null;
        if (account != null) {
            sourceAccountOwner = account.getAccountOwner();
        }

        FundDTO fundDTO = new FundDTO();
        fundDTO.setAccountNumber(BILLER_PAYMENT_ACCOUNT);
        fundDTO.setDestBankCode("ABC");
        fundDTO.setAmount(payRRRDTO.getAmount());
        fundDTO.setChannel("WalletToWallet");
        fundDTO.setSourceAccountNumber(payRRRDTO.getSourceAccountNo());
        fundDTO.setSpecificChannel(SpecificChannel.PAY_RRR.getName());
        fundDTO.setTransRef(payRRRDTO.getTransRef());
        fundDTO.setNarration(payRRRDTO.getNarration());
        fundDTO.setRrr(payRRRDTO.getRrr());
        fundDTO.setRedeemBonus(payRRRDTO.isRedeemBonus());
        fundDTO.setBonusAmount(payRRRDTO.getBonusAmount());
        if (sourceAccountOwner != null) {
            fundDTO.setPhoneNumber(sourceAccountOwner.getPhoneNumber());
        }

        //walletToWallet ==> within our scheme
        log.debug("\n\n\n\n\n\n\n\n fundDTO  = " + fundDTO);

        ValidTransactionResponse validTransaction = utility.isValidTransaction(fundDTO.getChannel(),
            fundDTO.getSourceAccountNumber(), fundDTO.getAccountNumber(),
            fundDTO.getAmount(), fundDTO.getBonusAmount(), false);
        if (validTransaction.isValid()) {

            WalletAccount destinationAcct = walletAccountService.findOneByAccountNumber(BILLER_PAYMENT_ACCOUNT);
            String destinationName = destinationAcct.getAccountOwner() != null ? destinationAcct.getAccountOwner().getFullName() : destinationAcct.getAccountName();
            fundDTO.setBeneficiaryName(destinationName);

            String sourceName = account != null && account.getAccountOwner() != null ? account.getAccountOwner().getFullName() : account.getAccountName();
            fundDTO.setSourceAccountName(sourceName);

            producer.send(fundDTO);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new GenericResponseDTO("00", HttpStatus.OK, "RRR payment successful", null);
        }
        return new GenericResponseDTO(INVALID_ACCOUNT_STATUS.getCode(), HttpStatus.OK, validTransaction.getMessage(), null);

    }

    @Override
    public boolean billNotification(FundDTO fundDTO) {

        String transRef = fundDTO.getTransRef();

        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(fundDTO.getSourceAccountNumber());

        if (walletAccount != null && walletAccount.getScheme() != null) {

            BillRequest billRequest = buildBillRequest(fundDTO.getRrr(), fundDTO.getAmount() + fundDTO.getBonusAmount(), walletAccount.getScheme());
            BillNotificationResponse notifyResposne = null;

            log.info("billRequest ===> " + billRequest);
            RemitaBillingGatewayService gatewayService = getRemitaBillingGatewayService(transRef);


//            Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
//            if (asyncExecutor != null) {
//                asyncExecutor.execute(() -> {
//                    try {
//                        BillNotificationResponse notificationResponse = gatewayService.billNotification(billRequest);
//                        log.info("BillnotificationResponse ===> " + notificationResponse);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//            }

                try {
                    notifyResposne = gatewayService.billNotification(billRequest);
                    log.info("BillnotificationResponse ===> " + notifyResposne);
                    if (!notifyResposne.getResponseCode().equalsIgnoreCase("00")){
                        throw new Exception();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    GetRRRDetailsResponse rrrDetails = getRRRDetails(billRequest.getRrr(), transRef);

                    log.info("RRR details retrieved ====>" + rrrDetails);
                    boolean firstCondition = rrrDetails != null && rrrDetails.getResponseData() != null && !rrrDetails.getResponseData().isEmpty();
                    if (firstCondition) {
                        String rrr = rrrDetails.getResponseData().get(0).getRrr();
                        boolean secondCondition = rrr == null;
                        int counter = 0;
                        while (!secondCondition && counter < 10) {
                            GetRRRDetailsResponse rrrDetails1 = getRRRDetails(billRequest.getRrr(), transRef);
                            log.info("RRR details retrieved in while loop ====>" + rrrDetails1);

                            System.out.println("Call Number " + counter);
                            if (rrrDetails1 != null && rrrDetails1.getResponseData() != null && !rrrDetails1.getResponseData().isEmpty()) {
                                rrr = rrrDetails1.getResponseData().get(0).getRrr();
                                secondCondition = rrr == null;
                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException ie) {
                                    ie.printStackTrace();
                                }
                                counter++;
                            }
                        }
                        if (secondCondition) {
                            notifyResposne = new BillNotificationResponse();
                            notifyResposne.setResponseCode("00");
                            notifyResposne.setResponseMsg("successful");
                        }
                    }
                }

//            GetRRRDetailsResponse rrrDetails = getRRRDetails(billRequest.getRrr(), transRef);
//
//            log.info("RRR details retrieved ====>" + rrrDetails);
//            boolean firstCondition = rrrDetails != null && rrrDetails.getResponseData() != null && !rrrDetails.getResponseData().isEmpty();
//            if (firstCondition) {
//                String rrr = rrrDetails.getResponseData().get(0).getRrr();
//                boolean secondCondition = rrr == null;
//                int counter = 0;
//                while (!secondCondition && counter < 10) {
//                    GetRRRDetailsResponse rrrDetails1 = getRRRDetails(billRequest.getRrr(), transRef);
//                    log.info("RRR details retrieved in while loop ====>" + rrrDetails1);
//
//                    System.out.println("Call Number " + counter);
//                    if (rrrDetails1 != null && rrrDetails1.getResponseData() != null && !rrrDetails1.getResponseData().isEmpty()) {
//                        rrr = rrrDetails1.getResponseData().get(0).getRrr();
//                        secondCondition = rrr == null;
//                        try {
//                            Thread.sleep(10000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        counter++;
//                    }
//                }
//                if (secondCondition) {
//                    notifyResposne = new BillNotificationResponse();
//                    notifyResposne.setResponseCode("00");
//                    notifyResposne.setResponseMsg("successful");
//                }
//            }

            boolean result = notifyResposne != null && notifyResposne.getResponseCode().equalsIgnoreCase("00");
            if (!result) {

                Map<String, String> emails = utility.getNotificationEmailMap();

                String message = "Error processing";
                if (notifyResposne != null) {
                    message = notifyResposne.getiResponseMessage();
                    message = message + " - " + notifyResposne.getResponseMsg();
                }

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                LocalDate format = LocalDate.now();
                String now = format.format(dateFormatter);

                for (Map.Entry<String, String> entry : emails.entrySet()) {

                    double transactionAmount = fundDTO.getAmount() + fundDTO.getBonusAmount();

                    String msg = "Dear " + entry.getKey() + "," +
                        "<br/>" +
                        "<br/>" +
                        "<br/>" +
                        "This is to notify you that the transaction referenced below has failed with the following error: " +
                        message +
                        "<br/>" +
                        "<br/>" +
                        "<br/>" +
                        "<b><u> TRANSACTION FAILURE: Bills Payment</u></b>" +
                        "<br/>" +
                        "<br/>" +
                        "<br/>" +
                        "<br/>" + "Transaction Date : " + now +
                        "<br/>" + "Transaction ID : " + transRef +
                        "<br/>" + "Transaction Amount : " + transactionAmount +
                        "<br/>" + "Remita Reference Number (RRR) : " + fundDTO.getRrr() +
                        "<br/>" + "Transaction Type : Bills Payment" +
                        "<br/>" + "Username : " + fundDTO.getSourceAccountName() +
                        "<br/>" + "Wallet ID : " + fundDTO.getSourceAccountNumber() +
                        "<br/>" + "Phone Number : " + fundDTO.getPhoneNumber() +
                        "<br/>" + "Failed System: Bill Notification API" +
                        "<br/>" + "Reporting Error: " + message +
                        "<br/>" + "Responsibility : Lara Olowolagba - Lead, Transaction Monitoring (+234 814 137 6819)" +
                        "<br/>" +
                        "<br/>" +
                        "<br/>" + "<p><i><b>NOTICE!! </b></i> - Please investigate and ensure resolution as soon as possible</p>";


                    utility.sendEmail(entry.getValue(), "TRANSACTION FAILURE", msg);

                }

            }

            return result;
        }

        return false;
    }

    @Override
    public GenericResponseDTO buyAirtimeForCustomer(AgentAirtimeDTO agentAirtimeDTO) {

        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                this.theUser = user;
            });

        if (theUser == null) {
            return new GenericResponseDTO("64", HttpStatus.BAD_REQUEST, "User session expired", null);
        }


        agentAirtimeDTO.setPhoneNumber(utility.formatPhoneNumber(agentAirtimeDTO.getPhoneNumber()));
        agentAirtimeDTO.setPhoneNumberToCredit(utility.formatPhoneNumber(agentAirtimeDTO.getPhoneNumberToCredit()));

        String uniqueTransRef = utility.getUniqueTransRef();
        BuyAirtimeDTO buyAirtimeDTO = new BuyAirtimeDTO();
        buyAirtimeDTO.setPhoneNumber(agentAirtimeDTO.getPhoneNumberToCredit());
        buyAirtimeDTO.setTransRef(agentAirtimeDTO.getTransRef());
        buyAirtimeDTO.setNarration(agentAirtimeDTO.getNarration());
        buyAirtimeDTO.setSourceAccountNo(agentAirtimeDTO.getAccountNumber());
        buyAirtimeDTO.setAmount(agentAirtimeDTO.getAmount());
        buyAirtimeDTO.setServiceId(agentAirtimeDTO.getServiceId());
        buyAirtimeDTO.setPin(agentAirtimeDTO.getPin());
        buyAirtimeDTO.setAgentRef(uniqueTransRef);
        buyAirtimeDTO.setCustomFieldId(agentAirtimeDTO.getCustomFieldId());

        GenericResponseDTO genericResponseDTO = buyAirtime(buyAirtimeDTO);

        if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
            return sendRequestMoney(agentAirtimeDTO, uniqueTransRef);
        }

        return genericResponseDTO;
    }

    @Override
    public GenericResponseDTO buyDataForCustomer(AgentAirtimeDTO agentAirtimeDTO) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                this.theUser = user;
            });

        if (theUser == null) {
            return new GenericResponseDTO("64", HttpStatus.BAD_REQUEST, "User session expired", null);
        }
        agentAirtimeDTO.setPhoneNumber(utility.formatPhoneNumber(agentAirtimeDTO.getPhoneNumber()));
        agentAirtimeDTO.setPhoneNumberToCredit(utility.formatPhoneNumber(agentAirtimeDTO.getPhoneNumberToCredit()));

        String uniqueTransRef = utility.getUniqueTransRef();
        BuyDataDTO buyDataDTO = new BuyDataDTO();
        buyDataDTO.setPhoneNumber(agentAirtimeDTO.getPhoneNumberToCredit());
        buyDataDTO.setTransRef(agentAirtimeDTO.getTransRef());
        buyDataDTO.setNarration(agentAirtimeDTO.getNarration());
        buyDataDTO.setSourceAccountNo(agentAirtimeDTO.getAccountNumber());
        buyDataDTO.setAmount(agentAirtimeDTO.getAmount());
        buyDataDTO.setServiceId(agentAirtimeDTO.getServiceId());
        buyDataDTO.setPin(agentAirtimeDTO.getPin());
        buyDataDTO.setAgentRef(uniqueTransRef);
        buyDataDTO.setDataPlanId(agentAirtimeDTO.getServiceId());

        GenericResponseDTO genericResponseDTO = buyData(buyDataDTO);
        if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
            return sendRequestMoney(agentAirtimeDTO, uniqueTransRef);
        }
        return genericResponseDTO;
    }

    private BillRequest buildBillRequest(String rrr, double amount, Scheme scheme) {
        BillRequest billRequest = new BillRequest();
        billRequest.setRrr(rrr);
        billRequest.setAmountDebitted(String.valueOf(amount));
        billRequest.setDebittedAccount(scheme.getAccountNumber());//TODO CHANGE LIVE PARAM
        billRequest.setBranchCode(REMITA_BILLING_BRANCH_CODE);
        billRequest.setFundingSource(REMITA_BILLING_FUNDING_SOURCE);
        billRequest.setIncomeAccount(REMITA_BILLING_INCOME_ACCOUNT);
        billRequest.setTellerName(REMITA_BILLING_TELLER_NAME);
        billRequest.setPaymentChannel(REMITA_BILLING_PAYMENT_CHANNEL);
        billRequest.setPaymentAuthCode(REMITA_PAYMENT_AUTH_CODE);

        return billRequest;
    }

    private WalletAccount getBillerWalletOrCreateNew(Biller biller) {
//        Optional<WalletAccount> billerWalletAccount = walletAccountService.findByAccountName(biller.getBillerID());
//        if (billerWalletAccount.isPresent()) {
//            log.info("Biller Wallet Account retrieved " + billerWalletAccount.get());
//            return billerWalletAccount.get();
//        }
//
//        WalletAccount walletAccount = new WalletAccount();
//        walletAccount.setCurrentBalance("0.00");
//        walletAccount.setDateOpened(LocalDate.now());
//        walletAccount.setAccountName(biller.getBiller());
//        long accountNumber = ThreadLocalRandom.current().nextLong(Lower_Bond, Upper_Bond);
//        walletAccount.setAccountNumber(String.valueOf(accountNumber));
//        WalletAccount save = walletAccountService.save(walletAccount);
//        log.info("Biller Wallet Account created " + save);
//        return save;

        return walletAccountService.findOneByAccountNumber(BILLER_PAYMENT_ACCOUNT);

    }

    private Biller getBillerByServiceTypeId(String serviceTypeId) {
        Optional<BillerPlatform> billerPlatformOptional = billerPlatformService.findByBillerplatformID(Long.valueOf(serviceTypeId));
        if (billerPlatformOptional.isPresent()) {
            BillerPlatform billerPlatform = billerPlatformOptional.get();
            return billerPlatform.getBiller();
        }
        return null;
    }

    @Override
    public GenericResponseDTO buyAirtime(BuyAirtimeDTO buyAirtimeDTO) {

        String sourceAccountNo = buyAirtimeDTO.getSourceAccountNo();
        Double amount = buyAirtimeDTO.getAmount();
        double bonusAmount = buyAirtimeDTO.getBonusAmount();

        GenericResponseDTO balanceIsSuficientResponse = checkBalanceIsNotSufficient(sourceAccountNo, amount, bonusAmount, buyAirtimeDTO, SpecificChannel.BUY_AIRTIME.getName());
        if (balanceIsSuficientResponse != null) return balanceIsSuficientResponse;

        ValidateRequest validateRequest = new ValidateRequest();

        if (!StringUtils.isEmpty(buyAirtimeDTO.getCustomFieldId())) {
            List<CustomField> customFieldList = new ArrayList<>();
            List<Value> valueList = new ArrayList<>();
            {
                Value value = new Value();
                CustomField customField = new CustomField();
                {
                    value.setAmount(BigDecimal.valueOf(0));
                    value.setQuantity(0);
                    value.setValue(String.valueOf(0));

                    valueList.add(value);
                    customField.setId(buyAirtimeDTO.getCustomFieldId());
                    customField.setValues(valueList);
                    customFieldList.add(customField);
                }
            }

            validateRequest.setCustomFields(customFieldList);
        }
        validateRequest.setAmount(BigDecimal.valueOf(amount + bonusAmount));
        validateRequest.setBillId(buyAirtimeDTO.getServiceId());
        validateRequest.setCurrency("NGN");

        String email = "ussd@system.com";
        if (this.theUser != null) {
            email = this.theUser.getEmail();
        }
        String fullName = "System ussd ";
        if (this.theUser == null) {
            WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(buyAirtimeDTO.getSourceAccountNo());
            if (walletAccount != null && walletAccount.getAccountOwner() != null) {
                fullName = walletAccount.getAccountOwner().getFullName();
            }
        } else {
            fullName = this.theUser.getFirstName() + " " + this.theUser.getLastName();
        }

        validateRequest.setPayerEmail(email);
        validateRequest.setPayerName(fullName);
        validateRequest.setPayerPhone(buyAirtimeDTO.getPhoneNumber());
        GenericResponseDTO genericResponseDTO = this.validateAndPayBiller(validateRequest,
            buyAirtimeDTO.getSourceAccountNo(),
            buyAirtimeDTO.getTransRef(),
            SpecificChannel.BUY_AIRTIME.getName(),
            buyAirtimeDTO.getNarration(),
            buyAirtimeDTO.getAgentRef(),
            buyAirtimeDTO.getBonusAmount(), buyAirtimeDTO.isRedeemBonus());
        if (genericResponseDTO.getCode().equalsIgnoreCase("00")) {
            log.info("Payment successful");
        }
        log.info(genericResponseDTO.getMessage());
        return genericResponseDTO;
    }

    @Override
    public GenericResponseDTO buyData(BuyDataDTO buyDataDTO) {

        String sourceAccountNo = buyDataDTO.getSourceAccountNo();
        Double amount = buyDataDTO.getAmount();
        double bonusAmount = buyDataDTO.getBonusAmount();

        GenericResponseDTO balanceIsSuficientResponse = checkBalanceIsNotSufficient(sourceAccountNo, amount, bonusAmount, buyDataDTO, SpecificChannel.BUY_DATA.getName());
        if (balanceIsSuficientResponse != null) return balanceIsSuficientResponse;


        String sourceAccountNumber = buyDataDTO.getSourceAccountNo();
        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(String.valueOf(sourceAccountNumber));
        Double currentBalance = Double.valueOf(walletAccount.getCurrentBalance());

        if (currentBalance <= buyDataDTO.getAmount()) {
            return new GenericResponseDTO("50", HttpStatus.BAD_REQUEST, "Insufficient balance!", buyDataDTO);
        }

        ValidateRequest validateRequest = new ValidateRequest();
        List<CustomField> customFieldList = new ArrayList<>();
        List<Value> valueList = new ArrayList<>();
        /*{
            Value value = new Value();
            CustomField customField = new CustomField();
            {
                value.setAmount(BigDecimal.valueOf(buyDataDTO.getAmount()));
                value.setQuantity(1);
                value.setValue(buyDataDTO.getDataPlanId());
                valueList.add(value);
                customField.setId(buyDataDTO.getServiceId());
                customField.setValues(valueList);
                customFieldList.add(customField);
            }
        }*/
        validateRequest.setCustomFields(customFieldList);
        validateRequest.setAmount(BigDecimal.valueOf(buyDataDTO.getAmount() + buyDataDTO.getBonusAmount()));
        validateRequest.setBillId(buyDataDTO.getDataPlanId());
        validateRequest.setCurrency("NGN");

        User currentUser = utility.getCurrentUser();

        String email = "ussd@system.com";
        if (currentUser != null) {
            email = currentUser.getEmail();
        }
        String fullName = "System ussd";

        if (currentUser == null) {
            WalletAccount sourceWalletAccount = walletAccountService.findOneByAccountNumber(buyDataDTO.getSourceAccountNo());
            if (sourceWalletAccount != null && sourceWalletAccount.getAccountOwner() != null) {
                fullName = sourceWalletAccount.getAccountOwner().getFullName();
            }
        } else {
            fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
        }

        validateRequest.setPayerEmail(email);
        validateRequest.setPayerName(fullName);
        validateRequest.setPayerPhone(buyDataDTO.getPhoneNumber());
        return this.validateAndPayBiller(validateRequest, buyDataDTO.getSourceAccountNo(), buyDataDTO.getTransRef(), SpecificChannel.BUY_DATA.getName(), buyDataDTO.getNarration(), buyDataDTO.getAgentRef(), buyDataDTO.getBonusAmount(), buyDataDTO.isRedeemBonus());

    }

    public GenericResponseDTO validateAndPayBiller(ValidateRequest validateRequest, String sourceAccountNumber, String transRef, String specificChannel, String narration, String agentRef, double bonusAmount, boolean redeemBonus) {

        ObjectMapper objectMapper = new ObjectMapper();
        BigDecimal totalAmount = validateRequest.getAmount();

        WalletAccount account = walletAccountService.findOneByAccountNumber(sourceAccountNumber);

        if (account == null) {

            return new GenericResponseDTO("25", HttpStatus.BAD_REQUEST, "Wallet account does not exist [ " + sourceAccountNumber + " ]", null);
        }

        log.error("VALIDATE REQUEST" + validateRequest);

        ValidateResponse validateResponse = this.validate(validateRequest, transRef);
        try {
            log.debug("validateResponse  = " + objectMapper.writeValueAsString(validateResponse));

            if (validateResponse != null && validateResponse.getResponseCode().equals("00") && "REQUEST_OK".equalsIgnoreCase(validateResponse.getResponseData().get(0).getStatus())) {
                GenerateResponse generateResponse = this.generateRRR(validateRequest, transRef);
                log.debug("generateResponse  = " + objectMapper.writeValueAsString(generateResponse));

                if (generateResponse != null && generateResponse.getResponseCode().equals("00")) {

                    //todo Resolution of amountDue
                    List<ResponseData> responseData = generateResponse.getResponseData();
                    ResponseData responseData1 = responseData.get(0);
                    String amountDue = responseData1.getAmountDue();

                    FundDTO fundDTO = new FundDTO();
                    fundDTO.setAccountNumber(BILLER_PAYMENT_ACCOUNT);
                    fundDTO.setAmount(Double.parseDouble(String.valueOf(totalAmount)) - bonusAmount);
                    if (SpecificChannel.PAY_ELECTRICITY.getName().equalsIgnoreCase(specificChannel)) {
                        fundDTO.setAmount(Double.parseDouble(String.valueOf(totalAmount)) - bonusAmount + Double.parseDouble(amountDue));
                    }
//                    fundDTO.setAmount(Double.parseDouble(String.valueOf(validateRequest.getAmount())));
                    fundDTO.setDestBankCode("ABC");
                    fundDTO.setSourceAccountNumber(sourceAccountNumber);
                    fundDTO.setChannel("WalletToWallet");
                    fundDTO.setTransRef(transRef);
                    fundDTO.setSpecificChannel(specificChannel);
                    fundDTO.setNarration(narration);
                    fundDTO.setPhoneNumber(validateRequest.getPayerPhone());
                    fundDTO.setRrr(generateResponse.getResponseData().get(0).getRrr());
                    fundDTO.setAgentRef(agentRef);
                    fundDTO.setBonusAmount(bonusAmount);
                    fundDTO.setRedeemBonus(redeemBonus);

                    Double intraFee =
                        accountingService.getTransactionFee(fundDTO.getAmount() + fundDTO.getBonusAmount(),
                            fundDTO.getSpecificChannel(), null);

//                    if (Double.parseDouble(amountDue) > fundDTO.getAmount()) {
//                        return new GenericResponseDTO("15", HttpStatus.BAD_REQUEST, "Amount to be debited [" + utility.formatMoney(totalAmount.doubleValue()) + "] is less than RRR Amount Due [" + utility.formatMoney(Double.valueOf(amountDue)) + "]", null);
//                    }

                    if (Double.parseDouble(account.getCurrentBalance()) < fundDTO.getAmount() + intraFee) {
                        return new GenericResponseDTO("15", HttpStatus.BAD_REQUEST, "Insufficient fund in wallet account [ " + sourceAccountNumber + " ]", null);
                    }

                    ValidTransactionResponse validTransaction = utility.isValidTransaction(fundDTO.getChannel(), fundDTO.getSourceAccountNumber(), fundDTO.getAccountNumber(), fundDTO.getAmount(), fundDTO.getBonusAmount(), false);
                    if (validTransaction.isValid()) {

                        WalletAccount destinationAcct = walletAccountService.findOneByAccountNumber(BILLER_PAYMENT_ACCOUNT);
                        String destinationName = destinationAcct.getAccountOwner() != null ? destinationAcct.getAccountOwner().getFullName() : destinationAcct.getAccountName();
                        fundDTO.setBeneficiaryName(destinationName);

                        WalletAccount sourceAcct = walletAccountService.findOneByAccountNumber(sourceAccountNumber);
                        String sourceName = sourceAcct.getAccountOwner() != null ? sourceAcct.getAccountOwner().getFullName() : sourceAcct.getAccountName();
                        fundDTO.setSourceAccountName(sourceName);

                        log.debug("fundDTO after generic response  \n\n\n\n\n\n = " +
                            fundDTO);

                        if (StringUtils.isEmpty(agentRef)) {
                            producer.send(fundDTO);
                            return new GenericResponseDTO("00", HttpStatus.OK, "Biller payment successful", null);
                        } else {
                            try {

                                FundDTO save = transactionLogService.save(fundDTO);
                                log.info("Saved Agent fundDTO ++++====> " + save);
                                return new GenericResponseDTO("00", HttpStatus.OK, "FundDTO saved successfully", save);

                            } catch (Exception e) {

                                e.printStackTrace();
                                log.error("Error Saving biller FundDTO");
                                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Error saving fundDTO", null);
                            }
                        }
                    }
                    return new GenericResponseDTO(INVALID_ACCOUNT_STATUS.getCode(),
                        HttpStatus.BAD_REQUEST, validTransaction.getMessage(), null);
                }
                assert generateResponse != null;
                return new GenericResponseDTO(REQUEST_VALIDATION_FAILED.getCode(),
                    HttpStatus.BAD_REQUEST, generateResponse.getResponseMsg(), null);
            }

            assert validateResponse != null;
            return new GenericResponseDTO(REQUEST_VALIDATION_FAILED.getCode(),
                HttpStatus.BAD_REQUEST, validateResponse.getResponseMsg(), null);
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return new GenericResponseDTO("78", HttpStatus.BAD_REQUEST, "failed", null);
    }

    private GenericResponseDTO validateAndPayFixedPrice(User theUser, ValidateRequest validateRequest, String sourceAccountNumber, String transRef, String narration) {
        ObjectMapper objectMapper = new ObjectMapper();
        //String   billerServiceId =  validateRequest.getBillId();
        ValidateResponse validateResponse = this.validate(validateRequest, transRef);
        try {
            log.debug("validateResponse  = " + objectMapper.writeValueAsString(validateResponse));

            if (validateResponse != null && validateResponse.getResponseCode().equals("00")) {
                GenerateResponse generateResponse = this.generateRRR(validateRequest, transRef);
                log.debug("generateResponse  = " + objectMapper.writeValueAsString(generateResponse));
                if (generateResponse != null && generateResponse.getResponseCode().equals("00")) {

                    WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(sourceAccountNumber);

                    if (walletAccount != null && walletAccount.getScheme() != null) {

                        BillRequest billRequest = new BillRequest();
                        billRequest.setRrr((generateResponse.getResponseData().get(0)).getRrr());
                        // billRequest.setAmountDebitted((generateResponse.getResponseData().get(0)).getAmountDebitted());
                        billRequest.setAmountDebitted(String.valueOf(validateRequest.getAmount()));
                        billRequest.setDebittedAccount(walletAccount.getScheme().getAccountNumber());//TODO SET LIVE PARAMS
                        billRequest.setBranchCode(REMITA_BILLING_BRANCH_CODE);
                        billRequest.setFundingSource(REMITA_BILLING_FUNDING_SOURCE);
                        billRequest.setIncomeAccount(REMITA_BILLING_INCOME_ACCOUNT);
                        billRequest.setTellerName(REMITA_BILLING_TELLER_NAME);
                        billRequest.setPaymentChannel(REMITA_BILLING_PAYMENT_CHANNEL);
                        billRequest.setPaymentAuthCode(REMITA_PAYMENT_AUTH_CODE);
//                    GenericResponseDTO genericResponseOne = this.billNotification(billRequest, validateRequest.getBillId(), false, sourceAccountNumber, transRef, narration);
//                    if (genericResponseOne.getCode().equals("00")) {
//                        return new GenericResponseDTO("00", HttpStatus.OK, "Biller payment successfull", null);
//                    }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        assert validateResponse != null;
        return new GenericResponseDTO("78", HttpStatus.BAD_REQUEST, validateResponse.getResponseMsg(), true);
    }

    @Override
    public List<GetBillerResponseData> getBillers() {
        RemitaBillingGatewayService remitaBillingGatewayService = getRemitaBillingGatewayService(String.valueOf(System.currentTimeMillis()));
        if (remitaBillingGatewayService != null) {
            GetBillerResponse billers = remitaBillingGatewayService.getBillers();
            if (billers.getResponseCode().equalsIgnoreCase("00")) {
                return billers.getResponseData();
            }
        }
        return null;
    }

    @Override
    public List<GetServiceResponseData> getService(String billerId) {
        RemitaBillingGatewayService remitaBillingGatewayService = getRemitaBillingGatewayService(String.valueOf(System.currentTimeMillis()));
        log.info("remitaBillingGatewayService ===++++==== " + remitaBillingGatewayService);
        if (remitaBillingGatewayService == null) {
            log.info("ERROR +++++ =============== ERROOR");
        }

        GetServiceResponse serviceResponse = Objects.requireNonNull(remitaBillingGatewayService).getService(billerId);
        log.info(String.valueOf(serviceResponse));

        if (serviceResponse.getResponseCode().equals("00")) {
            return serviceResponse.getResponseData().get(0);
        }

        return null;
    }

    @Override
    public GetCustomFieldResponse getCustomField(String serviceTypeId) throws Exception {
        RemitaBillingGatewayService remitaBillingGatewayService = getRemitaBillingGatewayService(String.valueOf(System.currentTimeMillis()));
        if (remitaBillingGatewayService != null) {
            GetCustomFieldResponse customFieldResponse = remitaBillingGatewayService.getCustomField(serviceTypeId);
            log.info("CUSTOM SERVICE " + customFieldResponse);

            if (customFieldResponse != null && customFieldResponse.getResponseCode().equalsIgnoreCase("00")) {
                return customFieldResponse;
            } else {
                return null;
            }
        }

        return null;
    }

    @Override
    public GetTransactionStatusResponse getTransactionStatus(String transactionId) {
        RemitaBillingGatewayService gatewayService = getRemitaBillingGatewayService(String.valueOf(System.currentTimeMillis()));
        return gatewayService.getTransactionStatus(transactionId);
    }

    @Override
    public GenericResponseDTO payForUtils(UtilDTO utilDTO) {

        String sourceAccountNo = utilDTO.getSourceAccountNo();
        Double amount = utilDTO.getAmount();
        double bonusAmount = utilDTO.getBonusAmount();

        GenericResponseDTO balanceIsSuficientResponse = checkBalanceIsNotSufficient(sourceAccountNo, amount, bonusAmount, utilDTO, SpecificChannel.PAY_BILLS.getName());
        if (balanceIsSuficientResponse != null) return balanceIsSuficientResponse;

        ValidateRequest validateRequest = new ValidateRequest();

        if (!utilDTO.getCustomField().isEmpty()) {
            List<CustomField> customFieldList = new ArrayList<>();

            for (CustomFieldDTO customFieldDTO : utilDTO.getCustomField()) {
                List<Value> valueList = new ArrayList<>();
                Value value = new Value();
                CustomField customField = new CustomField();
                customField.setId(customFieldDTO.getCustomFieldId());

                value.setAmount(BigDecimal.valueOf(0));
                value.setQuantity(0);
                value.setValue(customFieldDTO.getValue());
                valueList.add(value);

                customField.setValues(valueList);
                customFieldList.add(customField);

            }
            validateRequest.setCustomFields(customFieldList);
        }

        validateRequest.setAmount(BigDecimal.valueOf(amount + bonusAmount));
        validateRequest.setBillId(utilDTO.getServiceId());
        validateRequest.setCurrency("NGN");
        User currentUser = utility.getCurrentUser();
        if (currentUser != null) {
            validateRequest.setPayerEmail(currentUser.getEmail());
            validateRequest.setPayerName(currentUser.getFirstName() + " " + currentUser.getLastName());
        } else {
            validateRequest.setPayerEmail("example@gmail.com");
            validateRequest.setPayerName("ussd");
        }
        validateRequest.setPayerPhone(utilDTO.getPhoneNumber());
        log.info("VALIDATE REQUEST BUILD " + validateRequest);
        return this.validateAndPayBiller(validateRequest, sourceAccountNo, utilDTO.getTransRef(), SpecificChannel.PAY_BILLS.getName(), utilDTO.getNarration(), utilDTO.getAgentRef(), utilDTO.getBonusAmount(), utilDTO.isRedeemBonus());
    }

    @Override
    public GenericResponseDTO validateMeter(UtilDTO utilDTO, HttpSession session) throws JsonProcessingException {

        ValidateRequest validateRequest = new ValidateRequest();

        if (!utilDTO.getCustomField().isEmpty()) {
            List<CustomField> customFieldList = new ArrayList<>();

            for (CustomFieldDTO customFieldDTO : utilDTO.getCustomField()) {
                List<Value> valueList = new ArrayList<>();
                Value value = new Value();
                CustomField customField = new CustomField();
                customField.setId(customFieldDTO.getCustomFieldId());

                value.setQuantity(0);
                value.setValue(customFieldDTO.getValue());
                valueList.add(value);

                customField.setValues(valueList);
                customFieldList.add(customField);

            }

            validateRequest.setCustomFields(customFieldList);

        }

        validateRequest.setAmount(BigDecimal.valueOf(utilDTO.getAmount() + utilDTO.getBonusAmount()));
        validateRequest.setBillId(utilDTO.getServiceId());
        validateRequest.setCurrency("NGN");
        validateRequest.setPayerPhone(utilDTO.getPhoneNumber());


        User currentUser = utility.getCurrentUser();

        if (currentUser != null) {
            validateRequest.setPayerEmail(currentUser.getEmail());
            validateRequest.setPayerName(currentUser.getFirstName() + " " + currentUser.getLastName());
        } else {
            validateRequest.setPayerEmail("example@gmail.com");
            validateRequest.setPayerName("Ussd");
        }

        String IBEDCPostpaid = "4111452907"; //IBEDC (Postpaid)
        String IBEDCPostpaidBal = "4127916937"; //IBEDC (Postpaid)
        String IBEDCPrepaid = "4111452350"; //IBEDC (Prepaid)

        String IBEDCPrepaidMinimumVend = "4127916940"; //IBEDC (Prepaid)

        String IkejaPrepaidAddr = "4127916935";
        String IkejaPrepaid = "4111432182";
        String IkejaPostPaid = "4111452350";
        String IkejaPostPaidAddr = "4127916932";


        String EkoElectricOrder = "3310501567";
        String EkoElectricOrderAddr = "3310508316";
        String EkoPostpaid = "3310478764";
        String EkoPostpaidAddr = "3310482884";
        String EkoPostpaidMinimumAmount = "3310481338";

        String EkoPrepaid = "3310524079";
        String EkoPrepaidAddr = "3310531564";
        String EkoPrepaidMinimumAmount = "3310531544";

        String EEDCPostpaid = "4111465265";
        String EEDCPostpaidAddr = "4127916943";

        String EEDCPrepaid = "4111465504";
        String EEDCPrepaidAddress = "4127916945";

        String AEDCPostpaid = "201907091130";
        String AEDCPostpaidAddr = "201907091130"; //TODO

        String AEDCPrepaid = "2237112949";
        String AEDCPrepaidAddr = "2237112949";

        String JEDCElectric = "2997091595";
        String JEDCElectricAddr = "3177190513";


        String JEDCPostpaid = "3170022104";
        String JEDCPostpaidAddr = "3177190513";

        HashMap<String, String> map = new HashMap<>();

        map.put(IkejaPostPaid, IkejaPostPaidAddr);
        map.put(IkejaPrepaid, IkejaPrepaidAddr);

        map.put(EkoElectricOrder, EkoElectricOrderAddr);
        map.put(EkoPrepaid, EkoPrepaidAddr);
        map.put(EkoPostpaid, EkoPostpaidAddr);

        map.put(IBEDCPostpaid, IBEDCPostpaidBal);
        map.put(IBEDCPrepaid, IBEDCPrepaidMinimumVend);

//        map.put(IBEDCPrepaid, IBEDCPrepaidMinimumVend);

        map.put(EEDCPostpaid, EEDCPostpaidAddr);
        map.put(EEDCPrepaid, EEDCPrepaidAddress);

        map.put(AEDCPostpaid, AEDCPostpaidAddr);
        map.put(AEDCPrepaid, AEDCPrepaidAddr);

        map.put(JEDCElectric, JEDCElectricAddr);
        map.put(JEDCPostpaid, JEDCPostpaidAddr);

        ValidateResponse validateResponse = this.validateRequest(validateRequest, session, utilDTO.getTransRef());
        if (validateResponse.getResponseCode().equalsIgnoreCase("00") && validateResponse.getResponseData().get(0).getStatus().equalsIgnoreCase("REQUEST_OK")) {

            ResponseDatum responseDatum = validateResponse.getResponseData().get(0);
            ValidateMeterResponseDTO validateMeterResponseDTO = new ValidateMeterResponseDTO();

            validateMeterResponseDTO.setName(responseDatum.getPayerName());
            validateMeterResponseDTO.setAmountDue(String.valueOf(responseDatum.getAmountDue()));
            List<CustomField> customFields = responseDatum.getCustomFields();

            if (IBEDCPrepaid.equalsIgnoreCase(responseDatum.getBillId())) {
                customFields.forEach(customField -> {
                    if (customField.getId().equalsIgnoreCase(IBEDCPrepaidMinimumVend)) {
                        validateMeterResponseDTO.setVendingAmount(customField.getValues().get(0).getValue());
                    }
                });
            } else if (IBEDCPostpaid.equalsIgnoreCase(responseDatum.getBillId())) {
                customFields.forEach(customField -> {
                    if (customField.getId().equalsIgnoreCase(map.get(IBEDCPostpaid))) {
                        validateMeterResponseDTO.setBalance(customField.getValues().get(0).getValue());
                    }
                });
            } else if (IkejaPrepaid.equalsIgnoreCase(responseDatum.getBillId())) {
                customFields.forEach(customField -> {
                    if (customField.getId().equalsIgnoreCase(map.get(IkejaPrepaid))) {
                        validateMeterResponseDTO.setAddress(customField.getValues().get(0).getValue());
                    }
                });
            } else if (IkejaPostPaid.equalsIgnoreCase(responseDatum.getBillId())) {
                customFields.forEach(customField -> {
                    if (customField.getId().equalsIgnoreCase(map.get(IkejaPostPaid))) {
                        validateMeterResponseDTO.setAddress(customField.getValues().get(0).getValue());
                    }
                });
            } else if (EkoElectricOrder.equalsIgnoreCase(responseDatum.getBillId())) {
                customFields.forEach(customField -> {
                    if (customField.getId().equalsIgnoreCase(map.get(EkoElectricOrder))) {
                        validateMeterResponseDTO.setAddress(customField.getValues().get(0).getValue());
                    }
                });
            } else if (EkoPostpaid.equalsIgnoreCase(responseDatum.getBillId())) {
                customFields.forEach(customField -> {
                    if (customField.getId().equalsIgnoreCase(map.get(EkoPostpaid))) {
                        validateMeterResponseDTO.setAddress(customField.getValues().get(0).getValue());
                    }
                    if (customField.getId().equalsIgnoreCase(EkoPostpaidMinimumAmount)) {
                        validateMeterResponseDTO.setMinimumAmount(customField.getValues().get(0).getValue());
                    }
                });
            } else if (EkoPrepaid.equalsIgnoreCase(responseDatum.getBillId())) {
                customFields.forEach(customField -> {
                    if (customField.getId().equalsIgnoreCase(map.get(EkoPrepaid))) {
                        validateMeterResponseDTO.setAddress(customField.getValues().get(0).getValue());
                    }
                    if (customField.getId().equalsIgnoreCase(EkoPrepaidMinimumAmount)) {
                        validateMeterResponseDTO.setMinimumAmount(customField.getValues().get(0).getValue());
                    }
                });
            } else if (EEDCPostpaid.equalsIgnoreCase(responseDatum.getBillId())) {
                customFields.forEach(customField -> {
                    if (customField.getId().equalsIgnoreCase(map.get(EEDCPostpaid))) {
                        validateMeterResponseDTO.setAddress(customField.getValues().get(0).getValue());
                    }
                });
            } else if (EEDCPrepaid.equalsIgnoreCase(responseDatum.getBillId())) {
                customFields.forEach(customField -> {
                    if (customField.getId().equalsIgnoreCase(map.get(EEDCPrepaid))) {
                        validateMeterResponseDTO.setAddress(customField.getValues().get(0).getValue());
                    }
                });
            } else if (AEDCPostpaid.equalsIgnoreCase(responseDatum.getBillId())) {
                customFields.forEach(customField -> {
                    if (customField.getId().equalsIgnoreCase(map.get(AEDCPostpaid))) {
                        validateMeterResponseDTO.setAddress(customField.getValues().get(0).getValue());
                    }
                });
            } else if (AEDCPrepaid.equalsIgnoreCase(responseDatum.getBillId())) {
                customFields.forEach(customField -> {
                    if (customField.getId().equalsIgnoreCase(map.get(AEDCPrepaid))) {
                        validateMeterResponseDTO.setAddress(customField.getValues().get(0).getValue());
                    }
                });
            } else if (JEDCElectric.equalsIgnoreCase(responseDatum.getBillId())) {
                customFields.forEach(customField -> {
                    if (customField.getId().equalsIgnoreCase(map.get(JEDCElectric))) {
                        validateMeterResponseDTO.setAddress(customField.getValues().get(0).getValue());
                    }
                });
            } else if (JEDCPostpaid.equalsIgnoreCase(responseDatum.getBillId())) {
                customFields.forEach(customField -> {
                    if (customField.getId().equalsIgnoreCase(map.get(JEDCPostpaid))) {
                        validateMeterResponseDTO.setAddress(customField.getValues().get(0).getValue());
                    }
                });
            }

            if (currentUser != null && validateMeterResponseDTO.getName().equalsIgnoreCase(currentUser.getFirstName() + " " + currentUser.getLastName())) {
                validateMeterResponseDTO.setName(null);
            }

            return new GenericResponseDTO("00", HttpStatus.OK, "Success", validateMeterResponseDTO);
        }

        return new GenericResponseDTO("115", HttpStatus.BAD_REQUEST,
            validateResponse.getResponseMsg().equalsIgnoreCase("101-The Smart card number does not exist.") ? "Invalid smart card" : "Validation failed",
            null);
    }

    @Override
    public ValidateResponse validateRequest(ValidateRequest validateRequest, HttpSession session, String transRef) throws JsonProcessingException {

        try {
            log.error("VALIDATE REQUEST ===> " + new ObjectMapper().writeValueAsString(validateRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        ValidateResponse validateResponse = this.validate(validateRequest, transRef);

        log.error("VALIDATE RESPONSE  ===> " + new ObjectMapper().writeValueAsString(validateResponse));


        if (validateResponse.getResponseCode().equalsIgnoreCase("00") && validateResponse.getResponseData().get(0).getStatus().equalsIgnoreCase("REQUEST_OK")) {
            List<ResponseDatum> responseData = validateResponse.getResponseData();
            ResponseDatum responseDatum = responseData.get(0);
            if (responseDatum == null) {
                return null;
            }
            if (validateResponse.getResponseCode().equalsIgnoreCase("00")) {
                List<CustomField> customFields = responseDatum.getCustomFields();
                List<CustomField> collect = customFields.stream().distinct().collect(Collectors.toList());
                responseDatum.setCustomFields(collect);
            }

            if (validateResponse.getResponseCode().equalsIgnoreCase("00")) {
                ValidateRequest validateRequest1 = new ValidateRequest();
                validateRequest1.setAmount(responseDatum.getAmount());
                validateRequest1.setBillId(responseDatum.getBillId());
                validateRequest1.setCurrency(responseDatum.getCurrency());
                validateRequest1.setCustomFields(responseDatum.getCustomFields());
                validateRequest1.setDescription(validateRequest.getDescription());
                validateRequest1.setPayerEmail(responseDatum.getPayerEmail());
                validateRequest1.setPayerName(responseDatum.getPayerName());
                validateRequest1.setPayerPhone(responseDatum.getPayerPhone());

                session.setAttribute("validateRequest", validateRequest);
                session.getServletContext().setAttribute("validateRequest", validateRequest);
            }

            log.error("RESPONSE DATUM ===> " + new ObjectMapper().writeValueAsString(responseDatum));

            return validateResponse;
        }
        return validateResponse;
    }

    private BillNotificationResponse billerPaymentNotifyByForeignEndPoint(BillRequest billRequest) {
        ObjectMapper objectMapper = new ObjectMapper();

        BillNotificationResponse billPayNotifyResponse = null;

        String transactionId = String.valueOf(System.currentTimeMillis());
        String publicKey = REMITA_PUBLIC_KEY;
        String secretKey = REMITA_NOTIFY_SECRET_KEY;
        String trxnHash = new DigestUtils("SHA-512").digestAsHex(billRequest.getRrr() + billRequest.getAmountDebitted()
            + billRequest.getFundingSource() + billRequest.getDebittedAccount()
            + billRequest.getPaymentAuthCode() + secretKey);

        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, String> params = new HashMap<String, String>();
            params.put("transactionId", transactionId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("publicKey", publicKey);
            headers.set("TXN_HASH", trxnHash);
            headers.set("transactionId", transactionId);

            log.debug("PUBLIC KEY = " + publicKey);
            log.debug("transactionId = " + transactionId);
            log.debug("HASH KEY = " + trxnHash);

            HttpEntity<String> clientRequest =
                new HttpEntity<String>(objectMapper.writeValueAsString(billRequest), headers);

            billPayNotifyResponse = restTemplate.postForObject(REMITA_BILLING_PAYMENT_URL, clientRequest, BillNotificationResponse.class, BillNotificationResponse.class);

            log.debug("BillPayNotify Response  = " + objectMapper.writeValueAsString(billPayNotifyResponse));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return billPayNotifyResponse;
    }

    public GetBillerResponse getAllBillersFromIpg() {
        return getRemitaBillingGatewayService(String.valueOf(System.currentTimeMillis())).getBillers();
    }

    public GetServiceResponse getServicesFromIpg(String serviceTypeId) {
        return getRemitaBillingGatewayService(String.valueOf(System.currentTimeMillis())).getService(serviceTypeId);
    }

    @Override
    public GenericResponseDTO payElectricity(UtilDTO utilDTO, ValidateRequest validateRequest) {

        String sourceAccountNo = utilDTO.getSourceAccountNo();
        Double amount = utilDTO.getAmount();
        double bonusAmount = utilDTO.getBonusAmount();

        GenericResponseDTO balanceIsSuficientResponse = checkBalanceIsNotSufficient(sourceAccountNo, amount, bonusAmount, utilDTO, SpecificChannel.PAY_ELECTRICITY.getName());
        if (balanceIsSuficientResponse != null) return balanceIsSuficientResponse;

        if (validateRequest != null) {
            validateRequest.setAmount(BigDecimal.valueOf(utilDTO.getAmount() + utilDTO.getBonusAmount()));
            User currentUser = utility.getCurrentUser();
            validateRequest.setPayerName(currentUser.getFirstName() + " " + currentUser.getLastName());
        } else {
            validateRequest = new ValidateRequest();

            if (!utilDTO.getCustomField().isEmpty()) {
                List<CustomField> customFieldList = new ArrayList<>();

                for (CustomFieldDTO customFieldDTO : utilDTO.getCustomField()) {
                    List<Value> valueList = new ArrayList<>();
                    Value value = new Value();
                    CustomField customField = new CustomField();
                    customField.setId(customFieldDTO.getCustomFieldId());

                    value.setQuantity(0);
                    value.setValue(customFieldDTO.getValue());
                    valueList.add(value);

                    customField.setValues(valueList);
                    customFieldList.add(customField);

                }

                validateRequest.setCustomFields(customFieldList);

            }

            validateRequest.setAmount(BigDecimal.valueOf(utilDTO.getAmount() + utilDTO.getBonusAmount()));
            validateRequest.setBillId(utilDTO.getServiceId());
            validateRequest.setCurrency("NGN");
            validateRequest.setPayerPhone(utilDTO.getPhoneNumber());

            User currentUser = utility.getCurrentUser();

            if (currentUser != null) {
                validateRequest.setPayerEmail(currentUser.getEmail());
                validateRequest.setPayerName(currentUser.getFirstName() + " " + currentUser.getLastName());
            } else {
                validateRequest.setPayerEmail("example@gmail.com");
                validateRequest.setPayerName("Ussd");
            }
        }

        return this.validateAndPayBiller(validateRequest, utilDTO.getSourceAccountNo(), utilDTO.getTransRef(), SpecificChannel.PAY_ELECTRICITY.getName(),
            utilDTO.getNarration(), utilDTO.getAgentRef(), utilDTO.getBonusAmount(), utilDTO.isRedeemBonus());
    }


    private GenericResponseDTO checkBalanceIsNotSufficient(String sourceAccountNo, double amount, double bonusAmount, Object obj, String specificChannel) {

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
    public GenericResponseDTO validateCheck(ValidateRequest validateRequest) {
        RemitaBillingGatewayService gatewayService = getRemitaBillingGatewayService(String.valueOf(System.currentTimeMillis()));
        ValidateResponse validate = gatewayService.validate(validateRequest);
        if (validate != null) {
            return new GenericResponseDTO("00", HttpStatus.OK, "success", validate);
        }
        return new GenericResponseDTO("123", HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", validate);
    }

    private GenericResponseDTO sendRequestMoney(AgentAirtimeDTO agentAirtimeDTO, String uniqueTransRef) {
        FundDTO fundDTO = new FundDTO();
        fundDTO.setAccountNumber(agentAirtimeDTO.getAccountNumber()); //requesterAccount
        fundDTO.setAmount(agentAirtimeDTO.getAmount());
        fundDTO.setChannel(agentAirtimeDTO.getChannel());
        fundDTO.setSourceAccountNumber(agentAirtimeDTO.getSourceAccountNumber());
        fundDTO.setPhoneNumber(agentAirtimeDTO.getPhoneNumber());
        fundDTO.setPin(agentAirtimeDTO.getPin());
        fundDTO.setTransRef(uniqueTransRef);
        fundDTO.setAgentRef(agentAirtimeDTO.getTransRef());

        List<WalletAccount> walletAccounts = walletAccountService.findByAccountOwnerPhoneNumber(agentAirtimeDTO.getPhoneNumber());
        if (!walletAccounts.isEmpty()) {
            Profile accountOwner = walletAccounts.get(0).getAccountOwner();
            if (accountOwner != null) {
                String narration = "Request for " + utility.formatMoney(agentAirtimeDTO.getAmount()) + " from " + agentAirtimeDTO.getPhoneNumber()
                    + "(" + accountOwner.getFullName() + ") by " + this.theUser.getFirstName() + " " + this.theUser.getLastName();
                fundDTO.setNarration(narration);

                PaymentResponseDTO paymentResponseDTO = walletAccountService.requestMoney(fundDTO);
                if (paymentResponseDTO.getError()) {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, paymentResponseDTO.getMessage(), paymentResponseDTO);
                } else {
                    return new GenericResponseDTO("00", HttpStatus.OK, paymentResponseDTO.getMessage(), paymentResponseDTO);
                }
            }
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Account owner not found for " + agentAirtimeDTO.getPhoneNumber());
        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Wallet Account not found for " + agentAirtimeDTO.getPhoneNumber());

    }

}
