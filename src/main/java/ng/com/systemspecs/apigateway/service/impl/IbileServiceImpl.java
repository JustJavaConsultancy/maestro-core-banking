package ng.com.systemspecs.apigateway.service.impl;

import ng.com.systemspecs.apigateway.client.IbileRESTClient;
import ng.com.systemspecs.apigateway.config.AsyncConfiguration;
import ng.com.systemspecs.apigateway.domain.IbilePaymentDetails;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.WalletAccount;
import ng.com.systemspecs.apigateway.domain.enumeration.SpecificChannel;
import ng.com.systemspecs.apigateway.domain.enumeration.TransactionStatus;
import ng.com.systemspecs.apigateway.domain.enumeration.TransactionType;
import ng.com.systemspecs.apigateway.repository.IbileRepository;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.systemspecs.apigateway.service.exception.IbilePaymentDetailsException;
import ng.com.systemspecs.apigateway.service.kafka.producer.TransProducer;
import ng.com.systemspecs.apigateway.service.mapper.AddressMapper;
import ng.com.systemspecs.apigateway.util.Utility;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executor;

import static java.lang.System.out;

@Service
@Transactional
public class IbileServiceImpl implements IbileService {

    private final Logger log = LoggerFactory.getLogger(IbileServiceImpl.class);

    private final IbileRESTClient ibileRESTClient;
    private final Utility utility;
    private final TransProducer producer;
    private final ProfileService profileService;
    private final WalletAccountService walletAccountService;
    private final TransactionLogService transactionLogService;
    private final AddressService addressService;
    private final AddressMapper addressMapper;
    private final JournalService journalService;
    private final UserService userService;
    private final IbileRepository ibileRepository;
    private final AsyncConfiguration asyncConfiguration;
    private final Environment environment;
    private final PaymentTransactionService paymentTransactionService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");
    @Value("${app.ibile.clientId}")
    private String clientId;
    @Value("${app.constants.dfs.lirs-income-acct}")
    private String LIRS_INCOME_ACCT;
    @Value("${app.constants.dfs.ibile-hub-commission-acct}")
    private String IBILE_HUB_COMMISSION_ACCT;
    @Value("${app.charges.ibile-hub}")
    private double IBILE_HUB_CHARGES;
    @Value("${app.constants.dfs.ibile-hub-income-acct}")
    private String IBILE_HUB_INCOME_ACCT;
    @Value("${app.ibile.commission.wallency}")
    private double IBILE_COMMISSION;
    @Value("${app.percentage.vat}")
    private double vatFeePercentage;
    @Value("${app.ibile.state}")
    private String state;
    @Value("${app.ibile.key}")
    private String key;
    @Value("${app.scheme.ibile}")
    private String IBILE_SCHEME;

    public IbileServiceImpl(Utility utility, TransProducer producer, ProfileService profileService, WalletAccountService walletAccountService, IbileRESTClient ibileRESTClient, TransactionLogService transactionLogService, AddressService addressService, AddressMapper addressMapper, JournalService journalService, UserService userService, IbileRepository ibileRepository, AsyncConfiguration asyncConfiguration, Environment environment, PaymentTransactionService paymentTransactionService) {
        this.utility = utility;
        this.producer = producer;
        this.profileService = profileService;
        this.walletAccountService = walletAccountService;
        this.ibileRESTClient = ibileRESTClient;
        this.transactionLogService = transactionLogService;
        this.addressService = addressService;
        this.addressMapper = addressMapper;
        this.journalService = journalService;
        this.userService = userService;
        this.ibileRepository = ibileRepository;
        this.asyncConfiguration = asyncConfiguration;
        this.environment = environment;
        this.paymentTransactionService = paymentTransactionService;
    }

    public static void main(String[] args) {

        String key = "UVBEFPJ9ESALKAIBN002";
        String state = "LASG";
        String billReferenceNo = "65404551-1059088736237-414";
        String hash = new DigestUtils("SHA-512").digestAsHex(key + billReferenceNo + state);

        out.println("Hash ===> " + hash);
    }

    @Override
    public ReferenceVerificationResponseDTO referenceVerification() {
        return null;
    }

    @Override
    public PaymentNotificationResponseDTO paymentVerification() {
        return null;
    }

    @Override
    public GenericResponseDTO getPaymentDetails(String billType, String billReferenceNo, HttpSession session) {
        if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
            return ibileStagingDetails(billType, billReferenceNo);
        }
        return ibileLiveDtails(billType, billReferenceNo);
    }

    private GenericResponseDTO ibileStagingDetails(String billType, String billReferenceNo) {
        Optional<ProfileDTO> profileDTOOptional = profileService.findByUserIsCurrentUser();
        out.println("profileDTOOptional ibile ====> " + profileDTOOptional);

        List<WalletAccountDTO> walletAccountDTOList = walletAccountService.findByUserIsCurrentUser();
        out.println("walletAccountDTOList ibile ====> " + walletAccountDTOList);

        if (walletAccountDTOList.isEmpty()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Failed to retrieve login user wallet", null);
        }

        if (!profileDTOOptional.isPresent()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User profile not found!", null);
        }

        String date = LocalDate.now().format(formatter);
        String hash = new DigestUtils("SHA-512").digestAsHex(key + billReferenceNo + state);

        ReferenceVerificationDTO referenceVerificationDTO = new ReferenceVerificationDTO();
        referenceVerificationDTO.setClientid(clientId);
        referenceVerificationDTO.setCurrency("NGN");
        referenceVerificationDTO.setDate(date);
        referenceVerificationDTO.setHash(hash);
        referenceVerificationDTO.setState(state);
        referenceVerificationDTO.setWebGuid(billReferenceNo);
        referenceVerificationDTO.setTellerID("774312");
        referenceVerificationDTO.setType("HARMONIZED");

        out.println("Ibile referenceVerificationDTO ==++> " + referenceVerificationDTO);

        List<IbilePaymentDTO> ibilePaymentDTOList = new ArrayList<>();

        IbilePaymentDTO ibilePaymentDTO = new IbilePaymentDTO();
        ibilePaymentDTO.setPayerName("Olamilekan Sola");
        ibilePaymentDTO.setWalletAccountNo(walletAccountDTOList.get(0).getAccountNumber());
        ibilePaymentDTO.setBillOutstandingAmt(100D);
        ibilePaymentDTO.setBillReferenceNo(billReferenceNo);
        ibilePaymentDTO.setBillType(billType);
        ibilePaymentDTO.setPayerId("23458");
        ibilePaymentDTO.setWalletBalance(walletAccountDTOList.get(0).getActualBalance());
        ibilePaymentDTO.setAmountToPay(0.00);
        ibilePaymentDTO.setCreditAccount("66774840280");
        ibilePaymentDTO.setTotalDue(2100D);
        ibilePaymentDTO.setWebGuid("222-0595995-201");
        ibilePaymentDTOList.add(ibilePaymentDTO);

        IbilePaymentDTO ibilePaymentDTO2 = new IbilePaymentDTO();
        ibilePaymentDTO2.setPayerName("Olamilekan Sola");
        ibilePaymentDTO2.setWalletAccountNo(walletAccountDTOList.get(0).getAccountNumber());
        ibilePaymentDTO2.setBillOutstandingAmt(2000D);
        ibilePaymentDTO2.setBillReferenceNo(billReferenceNo);
        ibilePaymentDTO2.setBillType(billType);
        ibilePaymentDTO2.setPayerId("23458");
        ibilePaymentDTO2.setWalletBalance(walletAccountDTOList.get(0).getActualBalance());
        ibilePaymentDTO2.setAmountToPay(0.00);
        ibilePaymentDTO2.setCreditAccount("66774840281");
        ibilePaymentDTO2.setTotalDue(2100D);
        ibilePaymentDTO2.setWebGuid("222-0595995-202");
        ibilePaymentDTOList.add(ibilePaymentDTO2);

        boolean isSuccessful = new Random().nextBoolean();
        if (isSuccessful) {
            return new GenericResponseDTO("00", HttpStatus.OK, "success", ibilePaymentDTOList);
        }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "fail", null);
    }

    private GenericResponseDTO ibileLiveDtails(String billType, String billReferenceNo) {
        GenericResponseDTO genericResponseDTO = new GenericResponseDTO();

        Optional<ProfileDTO> profileDTOOptional = profileService.findByUserIsCurrentUser();
        out.println("profileDTOOptional ibile ====> " + profileDTOOptional);

        List<WalletAccountDTO> walletAccountDTOList = walletAccountService.findByUserIsCurrentUser();
        out.println("walletAccountDTOList ibile ====> " + walletAccountDTOList);

        if (walletAccountDTOList.isEmpty()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Failed to retrieve login user wallet", null);
        }

        if (!profileDTOOptional.isPresent()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User profile not found!", null);
        }

        Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
        if (asyncExecutor != null) {
            out.println("Creating polaris account for Ibile agent ===> " + profileDTOOptional.get().getPhoneNumber());
            asyncExecutor.execute(() -> {
                out.println("Inside polaris creating nuban payment details");
                utility.createPolarisAccount(profileDTOOptional.get().getPhoneNumber(), IBILE_SCHEME);

            });
        }

        String date = LocalDate.now().format(formatter);
        String hash = new DigestUtils("SHA-512").digestAsHex(key + billReferenceNo + state);

        ReferenceVerificationDTO referenceVerificationDTO = new ReferenceVerificationDTO();
        referenceVerificationDTO.setClientid(clientId);
        referenceVerificationDTO.setCurrency("NGN");
        referenceVerificationDTO.setDate(date);
        referenceVerificationDTO.setHash(hash);
        referenceVerificationDTO.setState(state);
        referenceVerificationDTO.setWebGuid(billReferenceNo);
        referenceVerificationDTO.setTellerID("774312");
        referenceVerificationDTO.setType("HARMONIZED");

        out.println("Ibile referenceVerificationDTO ==++> " + referenceVerificationDTO);

        ReferenceVerificationResponseDTO result = null;

        int i = 0;
        while (i < 3) {
            try {
                result = ibileRESTClient.referenceVerification(referenceVerificationDTO);
                out.println("Reference  Verification response ==> " + result);
                if (result != null) {
                    break;
                }
            } catch (Exception exception) {
                int iteration = i + 1;
                out.println("Iteration ====> " + iteration);
                exception.printStackTrace();
                i++;
            }
            if (i == 3) {
                throw new IbilePaymentDetailsException("Fail to retrieve bill reference details from Ibile Server");
            }
        }

        out.println("Ibile Final result ===++> " + result);

        if (result != null && result.getBulkBill() != null) {
            List<Webguid> webguidList = result.getBulkBill().getWebguid();
            List<IbilePaymentDTO> ibilePaymentDTOList = new ArrayList<>();
            Double totalDue = result.getBulkBill().getTotalDue();

            out.println("Ibile webguidList ===++> " + webguidList);

            for (Webguid webguid : webguidList) {
                String pid = result.getBulkBill().getPid();
                IbilePaymentDTO ibilePaymentDTO = new IbilePaymentDTO();

                ibilePaymentDTO.setPayerName(result.getBulkBill().getPayerName());
                ibilePaymentDTO.setWalletAccountNo(walletAccountDTOList.get(0).getAccountNumber());
                ibilePaymentDTO.setBillOutstandingAmt(webguid.getAmountDue());
                ibilePaymentDTO.setBillReferenceNo(billReferenceNo);
                ibilePaymentDTO.setBillType(billType);
                ibilePaymentDTO.setPayerId(pid);
                ibilePaymentDTO.setWalletBalance(walletAccountDTOList.get(0).getActualBalance());
                ibilePaymentDTO.setAmountToPay(0.00);
                ibilePaymentDTO.setCreditAccount(webguid.getCreditAccount());
                ibilePaymentDTO.setTotalDue(totalDue);
                ibilePaymentDTO.setWebGuid(webguid.getWebguid());
                ibilePaymentDTOList.add(ibilePaymentDTO);
            }
            out.println("Ibile ibilePaymentDTOList ===++> " + ibilePaymentDTOList);
            return new GenericResponseDTO("00", HttpStatus.OK, "success", ibilePaymentDTOList);
        } else if (result != null) {
            Map<String, Object> additionalProperties = result.getAdditionalProperties();
            String statusMessage = (String) additionalProperties.get("statusMessage");
            genericResponseDTO.setMessage(statusMessage);
            if (StringUtils.isEmpty(statusMessage)) {
                genericResponseDTO.setMessage("Failed!");
            }
            genericResponseDTO.setCode("99");
            genericResponseDTO.setStatus(HttpStatus.BAD_REQUEST);
        } else {
            genericResponseDTO.setCode("99");
            genericResponseDTO.setMessage("Api failed!");
            genericResponseDTO.setStatus(HttpStatus.BAD_REQUEST);
        }
        return genericResponseDTO;
    }

    @Override
    public GenericResponseDTO pay(List<IbilePaymentDTO> ibilePaymentDTOs) {
        out.println("Payment DTOs ====> " + ibilePaymentDTOs);

        if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
            out.println("Payment DTOs Demo ====> " + ibilePaymentDTOs);
            return ibileStagingPay(ibilePaymentDTOs);
        }

        return ibileLivePay(ibilePaymentDTOs);
    }

    @NotNull
    private GenericResponseDTO ibileLivePay(List<IbilePaymentDTO> ibilePaymentDTOs) {
        ibilePaymentDTOs.sort(Comparator.comparingDouble(IbilePaymentDTO::getAmountToPay));

        out.println("sorted Payment DTOs ====> " + ibilePaymentDTOs);

        double totalTransactionAmount = ibilePaymentDTOs.stream().mapToDouble(IbilePaymentDTO::getAmountToPay).reduce(0,
            Double::sum);

        out.println("totalTransactionAmount =====-+?> " + totalTransactionAmount);

        for (IbilePaymentDTO ibilePaymentDTO : ibilePaymentDTOs) {
            try {
                if (ibilePaymentDTO.getBillOutstandingAmt() <= 0) {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST,
                        "No amount Outstanding " + ibilePaymentDTO.getBillOutstandingAmt());
                }
                Double amountToPay = ibilePaymentDTO.getAmountToPay();
                double commission = 0.0, vat = 0.0;

                if (amountToPay < 0 || amountToPay > ibilePaymentDTO.getBillOutstandingAmt()) {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST,
                        "Invalid amount/above amount outstanding ", null);
                }

                WalletAccount sourceAccount = walletAccountService.findOneByAccountNumber(String.valueOf(ibilePaymentDTO.getWalletAccountNo()));
                out.println("Ibile source account =-=++> " + sourceAccount);
                if (sourceAccount == null) {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid source account", null);
                }

                WalletAccount creditWalletAccount;

                if (amountToPay == IBILE_HUB_CHARGES) {
                    if (journalService.findByExternalRef(ibilePaymentDTO.getWebGuid()).isEmpty()) {
                        creditWalletAccount = walletAccountService.findOneByAccountNumber(IBILE_HUB_INCOME_ACCT);
                    } else {
                        creditWalletAccount = walletAccountService.findOneByAccountNumber(LIRS_INCOME_ACCT);
                        commission = IBILE_COMMISSION * amountToPay;
                        vat = commission * vatFeePercentage;
                    }
                } else {
                    creditWalletAccount = walletAccountService.findOneByAccountNumber(LIRS_INCOME_ACCT);
                    commission = IBILE_COMMISSION * amountToPay;
                    vat = commission * vatFeePercentage;
                }

                totalTransactionAmount = totalTransactionAmount + commission + vat;

                FundDTO fundDTO = buildFundDto(ibilePaymentDTO, commission, creditWalletAccount, sourceAccount);

                ValidTransactionResponse validTransaction = utility.isValidTransaction(
                    fundDTO.getChannel(),
                    fundDTO.getSourceAccountNumber(),
                    fundDTO.getAccountNumber(),
                    totalTransactionAmount,
                    fundDTO.getBonusAmount(),
                    false);

                log.debug("---44==4=> validTransaction  = " + validTransaction);

                if (!validTransaction.isValid()) {
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, validTransaction.getMessage(), null);
                }

                GenericResponseDTO checkSufficientFunds = utility.checkSufficientFunds(totalTransactionAmount,
                    fundDTO.getBonusAmount(), fundDTO.getSourceAccountNumber(), fundDTO.getCharges());

                if (checkSufficientFunds.getStatus().isError()) {
                    return checkSufficientFunds;
                }

                String destinationName = creditWalletAccount.getAccountOwner() != null ? creditWalletAccount.getAccountOwner().getFullName() : creditWalletAccount.getAccountName();

                String sourceName = sourceAccount.getAccountOwner() != null ?
                    sourceAccount.getAccountOwner().getFullName() :
                    sourceAccount.getAccountName();

                fundDTO.setBeneficiaryName(destinationName);
                fundDTO.setSourceAccountName(sourceName);

                try {
                    fundDTO = transactionLogService.save(fundDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Could not process transaction at this moment", null);
                }
                try {
                    if (fundDTO != null) {

                        producer.send(fundDTO);
                        Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
                        if (asyncExecutor != null) {
                            FundDTO finalFundDTO = fundDTO;
                            asyncExecutor.execute(() -> processIbileTransaction(ibilePaymentDTO, finalFundDTO));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Transaction failed", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new GenericResponseDTO("00", HttpStatus.OK, "success", ibilePaymentDTOs);
    }

    private GenericResponseDTO ibileStagingPay(List<IbilePaymentDTO> ibilePaymentDTOs) {
        out.println("Payment DTOs ====> " + ibilePaymentDTOs);

        ibilePaymentDTOs.sort(Comparator.comparingDouble(IbilePaymentDTO::getAmountToPay));

        out.println("sorted Payment DTOs ====> " + ibilePaymentDTOs);

        double totalTransactionAmount = ibilePaymentDTOs.stream().mapToDouble(IbilePaymentDTO::getAmountToPay).reduce(0,
            Double::sum);

        out.println("totalTransactionAmount =====-+?> " + totalTransactionAmount);

        for (IbilePaymentDTO ibilePaymentDTO : ibilePaymentDTOs) {
            if (ibilePaymentDTO.getBillOutstandingAmt() <= 0) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST,
                    "No amount Outstanding " + ibilePaymentDTO.getBillOutstandingAmt());
            }
            Double amountToPay = ibilePaymentDTO.getAmountToPay();
            double commission = 0.0, vat = 0.0;

            WalletAccount creditWalletAccount;

            if (amountToPay == IBILE_HUB_CHARGES) {
                if (journalService.findByExternalRef(ibilePaymentDTO.getWebGuid()).isEmpty()) {
                    creditWalletAccount = walletAccountService.findOneByAccountNumber(IBILE_HUB_INCOME_ACCT);
                } else {
                    creditWalletAccount = walletAccountService.findOneByAccountNumber(LIRS_INCOME_ACCT);
                    commission = IBILE_COMMISSION * amountToPay;
                    vat = commission * vatFeePercentage;
                }
            } else {
                creditWalletAccount = walletAccountService.findOneByAccountNumber(LIRS_INCOME_ACCT);
                commission = IBILE_COMMISSION * amountToPay;
                vat = commission * vatFeePercentage;
            }

            totalTransactionAmount = totalTransactionAmount + commission + vat;

            WalletAccount sourceAccount = walletAccountService.findOneByAccountNumber(String.valueOf(ibilePaymentDTO.getWalletAccountNo()));
            out.println("Ibile source account =-=++> " + sourceAccount);
            if (sourceAccount == null) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid source account", null);
            }
            FundDTO fundDTO = buildFundDto(ibilePaymentDTO, commission, creditWalletAccount, sourceAccount);

            ValidTransactionResponse validTransaction = utility.isValidTransaction(
                fundDTO.getChannel(),
                fundDTO.getSourceAccountNumber(),
                fundDTO.getAccountNumber(),
                totalTransactionAmount,
                fundDTO.getBonusAmount(),
                false);

            log.debug("---44==4=> validTransaction  = " + validTransaction);

            if (!validTransaction.isValid()) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, validTransaction.getMessage(), null);
            }

            GenericResponseDTO checkSufficientFunds = utility.checkSufficientFunds(totalTransactionAmount, fundDTO.getBonusAmount(), fundDTO.getSourceAccountNumber(), 0.0);

            if (checkSufficientFunds.getStatus().isError()) {
                return checkSufficientFunds;
            }

            String destinationName = creditWalletAccount.getAccountOwner() != null ? creditWalletAccount.getAccountOwner().getFullName() : creditWalletAccount.getAccountName();

            String sourceName = sourceAccount.getAccountOwner() != null ?
                sourceAccount.getAccountOwner().getFullName() :
                sourceAccount.getAccountName();

            fundDTO.setBeneficiaryName(destinationName);
            fundDTO.setSourceAccountName(sourceName);

            if (amountToPay < 0 || amountToPay > ibilePaymentDTO.getBillOutstandingAmt()) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid amount", null);
            }

            Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
            if (asyncExecutor != null) {
                asyncExecutor.execute(() -> {
                    try {
                        Thread.sleep(60000);
                        producer.send(fundDTO);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        out.println("Ibile payment Simulation failed ");
                    }
                });
            }
        }
        return new GenericResponseDTO("00", HttpStatus.OK, "success", ibilePaymentDTOs);
    }

    private FundDTO buildFundDto(IbilePaymentDTO ibilePaymentDTO, double commission, WalletAccount creditWalletAccount, WalletAccount sourceAccount) {
        String transRef = utility.getUniqueTransRef();

        FundDTO fundDTO = new FundDTO();
        fundDTO.setAccountNumber(creditWalletAccount.getAccountNumber());
        fundDTO.setAmount(ibilePaymentDTO.getAmountToPay());
        fundDTO.setChannel("walletToWallet");
        fundDTO.setSourceAccountNumber(ibilePaymentDTO.getWalletAccountNo());
        fundDTO.setSpecificChannel(SpecificChannel.IBILE_PAY.getName());
        fundDTO.setTransRef(transRef);
        fundDTO.setNarration("Ibile Pay/" + transRef + ": Bulk reference " + ibilePaymentDTO.getBillReferenceNo());
        fundDTO.setRrr(ibilePaymentDTO.getWebGuid());
        fundDTO.setShortComment(ibilePaymentDTO.getCreditAccount());
        fundDTO.setCharges(commission);
        fundDTO.setBulkTrans(false);
        fundDTO.setMultipleCredit(false);

        Profile sourceAccountOwner = sourceAccount.getAccountOwner();
        fundDTO.setPhoneNumber(sourceAccountOwner.getPhoneNumber());
        fundDTO.setSourceAccountName(sourceAccountOwner.getFullName());
        fundDTO.setStatus(TransactionStatus.PROCESSING);

        log.debug("\n\n\n\n\n\n\n\n fundDTO  = " + fundDTO);
        return fundDTO;
    }

    private void processIbileTransaction(IbilePaymentDTO ibilePaymentDTO, FundDTO fundDTO) {
        try {
            out.println("Entering Ibile process transaction  with argument " + ibilePaymentDTO);
            out.println("Entering Ibile process transaction with fund dto  " + fundDTO);
            Double amountToPay = ibilePaymentDTO.getAmountToPay();

            out.println("Payer ID: " + ibilePaymentDTO.getPayerId()+" for Reference: "+ibilePaymentDTO.getBillReferenceNo());
            String tellerId = StringUtils.substring(ibilePaymentDTO.getPayerId(), 2);
            String billReferenceNo = ibilePaymentDTO.getBillReferenceNo();
            out.println("Bill Reference for " + tellerId + " is ===> " + billReferenceNo);
            String hash = new DigestUtils("SHA-512").digestAsHex(key + ibilePaymentDTO.getWebGuid() + state + amountToPay);

            PaymentNotificationDTO paymentNotificationDTO = new PaymentNotificationDTO();
            paymentNotificationDTO.setAmountPaid(String.valueOf(amountToPay));
            paymentNotificationDTO.setBankNote(billReferenceNo);
            paymentNotificationDTO.setWebguid(ibilePaymentDTO.getWebGuid());
            paymentNotificationDTO.setPaymentRef(fundDTO.getTransRef());
            paymentNotificationDTO.setCreditAccount(ibilePaymentDTO.getCreditAccount());
            paymentNotificationDTO.setDate(LocalDate.now().format(formatter));
            paymentNotificationDTO.setHash(hash);
            paymentNotificationDTO.setState(state);
            paymentNotificationDTO.setClientId(clientId);
            paymentNotificationDTO.setPaymentChannel("BankBranch");
            paymentNotificationDTO.setTellerName(fundDTO.getSourceAccountName());
            paymentNotificationDTO.setTellerID(tellerId);
            paymentNotificationDTO.setTransRef(fundDTO.getTransRef());

            //Saving internal transaction call
            PaymentTransactionDTO savedTransaction;
            try {
                PaymentTransactionDTO paymentTransactionDTO = new PaymentTransactionDTO();
                paymentTransactionDTO.setAmount(BigDecimal.valueOf(amountToPay));
                paymentTransactionDTO.setCurrency("NGN");
                paymentTransactionDTO.setChannel("Ibile Hub API");
                paymentTransactionDTO.setTransactionDate(LocalDateTime.now());
                paymentTransactionDTO.setTransactionRef(fundDTO.getTransRef());
                paymentTransactionDTO.setDestinationAccount(ibilePaymentDTO.getCreditAccount());
                paymentTransactionDTO.setDestinationAccountBankCode("Fidelity");
                paymentTransactionDTO.setDestinationAccountName(fundDTO.getBeneficiaryName());
                paymentTransactionDTO.setDestinationNarration(fundDTO.getNarration());
                paymentTransactionDTO.setSourceAccount(fundDTO.getSourceAccountNumber());
                paymentTransactionDTO.setPaymenttransID(System.currentTimeMillis());
                paymentTransactionDTO.setSourceAccountName(fundDTO.getSourceAccountName());
                paymentTransactionDTO.setSourceAccountBankCode("Pouchii");
                paymentTransactionDTO.setTransactionOwnerPhoneNumber(fundDTO.getPhoneNumber());
                paymentTransactionDTO.setTransactionType(TransactionType.BANK_ACCOUNT_TRANSFER);
                paymentTransactionDTO.setSourceNarration(fundDTO.getNarration());
                savedTransaction = paymentTransactionService.save(paymentTransactionDTO);

                log.info("Saved payment transaction  ==> " + savedTransaction);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            out.println("PaymentNotificationDTO ===> " + paymentNotificationDTO);

            PaymentNotificationResponseDTO response = null;
            String status = "FAILED";
            int count = 0;
            while (count < 7) {
                out.println("Entering Iteration ====> " + count);
                try {
                    response = ibileRESTClient.paymentNotification(paymentNotificationDTO);
                    out.println("Payment  Notification response ==> " + response);
                    if (response != null) {
                        out.println("ibile transaction result for billReferenceNo " + billReferenceNo + " is " + response);
                        if ("SUCCESS".equalsIgnoreCase(response.getStatus())) {
                            out.println("Sending IBILE transaction to producer");
                            out.println("Transaction receipt for billReference " + billReferenceNo + " is ===> " + response.getReceiptNumber());
                            status = "SUCCESSFUL";
//                            count = 99;
                        } else {
                            out.println("ibile transaction result failed with error " + response);
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                count++;
                out.println("Payment Iteration ====> " + count);
//                if (count == 7) {
//                    out.println("Failed ibile payment notification");
//                    updatePaymentTransactionToIbile(savedTransaction, "FAILED");
//                }
            }
            updatePaymentTransactionToIbile(savedTransaction, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePaymentTransactionToIbile(PaymentTransactionDTO savedTransaction, String message) {
        try {
            savedTransaction.setDestinationNarration(savedTransaction.getDestinationNarration() +
                "/" + message);
            savedTransaction = paymentTransactionService.save(savedTransaction);
            log.info("Updated payment transaction  ==> " + savedTransaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public GenericResponseDTO generateAgentReceipt(String externalRef){
        try{
            FundDTO fundDTO = null;
            try {
                fundDTO = transactionLogService.findByRrr(externalRef);
                if(fundDTO == null)return new GenericResponseDTO("99", HttpStatus.EXPECTATION_FAILED,"Transaction Reference not Found!",null);
            }catch(Exception ex){
                return new GenericResponseDTO("99", HttpStatus.EXPECTATION_FAILED,"Transaction Reference not Found!",null);
            }

            if(!fundDTO.getStatus().getName().equalsIgnoreCase("COMPLETED")){
                return new GenericResponseDTO("99", HttpStatus.EXPECTATION_FAILED,"Transaction has not being processed!",null);
            }

            out.println("Entering Ibile process transaction with fund dto  " + fundDTO);
            String f[] = fundDTO.getNarration().split(" ");
            String d[] = f[4].split("/");
            String billReferenceNo = d[0];
            String l[] = billReferenceNo.split("-");
            String tellerId = l[0].substring(0, l[0].lastIndexOf("1"));
            out.println("Bill Reference for " + tellerId + " is ===> " + billReferenceNo);
            String hash = new DigestUtils("SHA-512").digestAsHex(key + externalRef + state + fundDTO.getAmount());

            PaymentNotificationDTO paymentNotificationDTO = new PaymentNotificationDTO();
            paymentNotificationDTO.setAmountPaid(String.valueOf(fundDTO.getAmount()));
            paymentNotificationDTO.setBankNote(billReferenceNo);
            paymentNotificationDTO.setWebguid(externalRef);
            paymentNotificationDTO.setPaymentRef(fundDTO.getTransRef());
            paymentNotificationDTO.setCreditAccount(fundDTO.getShortComment().trim());
            paymentNotificationDTO.setDate(LocalDate.now().format(formatter));
            paymentNotificationDTO.setHash(hash);
            paymentNotificationDTO.setState(state);
            paymentNotificationDTO.setClientId(clientId);
            paymentNotificationDTO.setPaymentChannel("BankBranch");
            paymentNotificationDTO.setTellerName(fundDTO.getSourceAccountName());
            paymentNotificationDTO.setTellerID(tellerId);
            paymentNotificationDTO.setTransRef(fundDTO.getTransRef());
            //Saving internal transaction call
            out.println("PaymentNotificationDTO ===> " + paymentNotificationDTO);

            PaymentNotificationResponseDTO response = null;

            int count = 0;
            while (count < 5) {
                out.println("Entering Iteration ====> " + count);
                try {
                    response = ibileRESTClient.paymentNotification(paymentNotificationDTO);
                    out.println("Payment  Notification response ==> " + response);
                    if (response != null) {
                        out.println("ibile transaction result for billReferenceNo " + billReferenceNo + " is " + response);
                        if ("SUCCESS".equalsIgnoreCase(response.getStatus())) {
                            out.println("Sending IBILE transaction to producer");
                            out.println("Transaction receipt for billReference " + billReferenceNo + " is ===> " + response.getReceiptNumber());
                            count = 99;
                        } else {
                            out.println("ibile transaction result failed with error " + response);
                            utility.sendEmail("temire@systemspecs.com.ng", "Consumer Processing Transaction ID "+fundDTO.getTransRef() , "Ibile failure response for Retry Count: "+count+" <br>  Bill Reference: "+billReferenceNo+ " <br> WebGuid: "+externalRef+" ==>> <br> "+response.toString());
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                count++;
            }
            return new GenericResponseDTO("00", HttpStatus.OK,"Success", response);

            }catch (Exception ex){
            ex.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.EXPECTATION_FAILED,"Failed",null);
        }
    }

    @Override
    public  GenericResponseDTO getWebguids(String pId) {
           List<FundDTO> fundDTOs = null;
            try {
                fundDTOs = transactionLogService.findAllByNarrationContains(pId);
                if(fundDTOs == null||fundDTOs.isEmpty())return new GenericResponseDTO("99", HttpStatus.EXPECTATION_FAILED,"Transactions for Id not Found!",null);
            }catch(Exception ex) {
                return new GenericResponseDTO("99", HttpStatus.EXPECTATION_FAILED, "Transactions not Found!", null);
            }
            List<String> webGuids = new ArrayList<>();
            for(FundDTO fundDTO:fundDTOs){
                webGuids.add(fundDTO.getRrr());
            }
            return new GenericResponseDTO("00", HttpStatus.OK,"Success", webGuids);
    }

    @Override
    public GenericResponseDTO getReceiptsByRef(String refId) {
        List<PaymentNotificationResponseDTO> receipts =  new ArrayList<>();
        if(refId.length()>9){
            GenericResponseDTO g = generateAgentReceipt(refId);
            if(g.getData()!=null) {
                PaymentNotificationResponseDTO paymentNotificationResponseDTO = (PaymentNotificationResponseDTO) g.getData();
                receipts.add(paymentNotificationResponseDTO);
            }
        }
        else{
            GenericResponseDTO gen = getWebguids(refId);
            if(gen.getData()!=null){
                List<String> webguids = (List<String>) gen.getData();
                for(String webguid : webguids){
                    GenericResponseDTO g = generateAgentReceipt(webguid);
                    if(g.getData()!=null) {
                        PaymentNotificationResponseDTO paymentNotificationResponseDTO = (PaymentNotificationResponseDTO) g.getData();
                        receipts.add(paymentNotificationResponseDTO);
                    }
                }
            }
        }

        return new GenericResponseDTO("00", HttpStatus.OK,"Available Transactions returned",receipts);
    }

    @Override
    public Optional<IbilePaymentDetails> findByReferenceNumber(String referenceNumber) {
        return ibileRepository.findOneByBillReferenceNumber(referenceNumber);
    }
}
