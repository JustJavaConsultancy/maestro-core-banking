package ng.com.justjava.corebanking.web.rest.navsa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ng.com.justjava.corebanking.domain.JournalLine;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.AccountStatus;
import ng.com.justjava.corebanking.service.*;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.dto.navsa.*;
import ng.com.justjava.corebanking.service.dto.stp.ExtendedConstants;
import ng.com.justjava.corebanking.service.kafka.producer.TransProducer;
import ng.com.justjava.corebanking.service.mapper.WalletAccountMapper;
import ng.com.justjava.corebanking.service.stp.RequestHelper;
import ng.com.justjava.corebanking.util.Utility;
import ng.com.justjava.corebanking.web.rest.remitastp.TransactionController;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.systemspecs.apigateway.service.dto.navsa.*;
import org.mapstruct.ap.internal.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequestMapping("/api/navsa")
public class NAVSAResource {

    final static Logger LOG = LoggerFactory.getLogger(NAVSAResource.class);
    private final Gson gson = new Gson();
    String defaultBankCode = "011";
    private final String defaultPass = "Default123$";
    String pin = "1234";


    @Autowired
    WalletAccountService walletAccountService;
    String schemeID = "4e41565341";
    @Autowired
    WalletAccountMapper walletAccountMapper;
    @Autowired
    JournalLineService journalLineService;
    @Autowired
    TransactionLogService transactionLogService;
    @Autowired
    JournalService journalService;
    @Autowired
    Utility utility;
    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ProfileService profileService;
    @Autowired
    IdempotentService idempotentService;
    @Autowired
    PaymentTransactionService paymentTransactionService;
    @Autowired
    private RequestHelper requestHelper;
    @Autowired
    private TransProducer producer;

    @Autowired
    private TransactionController transactionController;

    @Autowired
    private BillerTransactionService billerTransactionService;

    @Autowired
    private BankService bankService;

    @Autowired
    private BillerService billerService;
    @Autowired
    SchemeService schemeService;

    @RequestMapping(value = {"/account/statement"}, method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DefaultApiResponse> getAccountStatement(@RequestParam String customerId,
                                                                  @RequestParam String accountNo,
                                                                  @RequestParam String bankCode,
                                                                  @RequestParam LocalDate startDate,
                                                                  @RequestParam LocalDate endDate) throws Exception {


        List<JournalLine> journalLines = journalLineService.findAllByWalletAccountAccountNumberAndJounalTransDateBetween(accountNo, startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));

        List<AccountStatement> statementLines = new ArrayList<>();
        for (JournalLine journalLine : journalLines) {

            String transactionRef = journalLine.getTransactionRef();

            FundDTO fundDTO = transactionLogService.findByTransRef(transactionRef);

            AccountStatement accountStatement = new AccountStatement();

            if (fundDTO != null) {
                String specificChannel = fundDTO.getSpecificChannel();
                accountStatement.setChannel(fundDTO.getChannel());
                accountStatement.setTransactionType(specificChannel);
            }

            accountStatement.setAmount(journalLine.getAmount());
            accountStatement.setCurrency("NGN");
            accountStatement.setNarration(journalLine.getMemo());
            accountStatement.setAccountNumber(accountNo);
            accountStatement.setDebitOrCredit(journalLine.getCreditDebit());
            accountStatement.setNarration(journalLine.getJounal().getNarration());
            accountStatement.setReferenceId(transactionRef);
            accountStatement.setTransactionTime(journalLine.getTransactionDate());
            accountStatement.setValueDate(journalLine.getTransactionDate().toLocalDate());
            accountStatement.setBalanceAfter(journalLine.getCurrentBalance());
            accountStatement.setExpansionFields(null);

            statementLines.add(accountStatement);
        }

        AccountStatementDTO accountStatementDTO = new AccountStatementDTO();
        accountStatementDTO.setTransactions(statementLines);

        DefaultApiResponse response = new DefaultApiResponse();
        response.setStatus("00");
        response.setMessage("The process was completed successully");
        response.setData(accountStatementDTO);

        LOG.info("ACCOUNT STATEMENT  ===> " + new ObjectMapper().writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/account/name/{customerId}/{bankCode}/{accountNumber}")
    public ResponseEntity<GenericNavsaResponse> getAccountName(
        @PathVariable String customerId,
        @PathVariable String bankCode,
        @PathVariable String accountNumber) throws Exception {


        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);

        GenericNavsaResponse genericNavsaResponse = new GenericNavsaResponse();

        if (walletAccount != null) {
            NavsaNameEnquiryResponse navsaNameEnquiryResponse = new NavsaNameEnquiryResponse();
            String accountName = walletAccount.getAccountOwner() != null ? walletAccount.getAccountOwner().getFullName() : walletAccount.getAccountName();

            navsaNameEnquiryResponse.setAccountName(accountName);
            navsaNameEnquiryResponse.setAccountNo(walletAccount.getAccountNumber());
            navsaNameEnquiryResponse.setBankCode(bankCode);

            genericNavsaResponse.setRespoincecode(ExtendedConstants.ResponseCode.SUCCESS.getCode());
            genericNavsaResponse.setResponseMsg("Account Found!");
            List<NavsaNameEnquiryResponse> responses = new ArrayList<>();
            responses.add(navsaNameEnquiryResponse);

            genericNavsaResponse.setResponseData(responses);

            String res = new Gson().toJson(navsaNameEnquiryResponse);
            LOG.info("Processing response {}", res);

            LOG.info("ACCOUNT NAME RESPONSE  ===> " + new ObjectMapper().writeValueAsString(navsaNameEnquiryResponse));

            return new ResponseEntity<>(genericNavsaResponse, HttpStatus.OK);
        }

        genericNavsaResponse.setRespoincecode(ExtendedConstants.ResponseCode.INVALID_ACCOUNT.getCode());
        genericNavsaResponse.setResponseMsg(ExtendedConstants.ResponseCode.INVALID_ACCOUNT.getDescription());

        LOG.info("ACCOUNT NAME FAIL RESPONSE  ===> " + new ObjectMapper().writeValueAsString(walletAccount));

        return new ResponseEntity<>(genericNavsaResponse, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/account/balance/{bankCode}/{accountNumber}")
    public ResponseEntity<GenericNavsaResponse> getAccountBalance(@PathVariable String bankCode,
                                                                  @PathVariable String accountNumber) throws Exception {

        GenericNavsaResponse response = new GenericNavsaResponse();

        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
        LOG.info("WALLET ACCOUNT  ===> " + walletAccount);


        if (walletAccount != null) {
            NavsaBalanceResponseDTO navsaBalanceResponseDTO = new NavsaBalanceResponseDTO();

            double totalDeposits;
            double totalWithdrawals;
            String profit;

            List<JournalLine> journalLines = journalLineService.findByWalletAccount_AccountNumber(walletAccount.getAccountNumber());
            totalWithdrawals = journalLines.stream().mapToDouble(JournalLine::getDebit).sum();
            totalDeposits = journalLines.stream().mapToDouble(JournalLine::getCredit).sum();

            String fullName = walletAccount.getAccountOwner() != null ? walletAccount.getAccountOwner().getFullName() : walletAccount.getAccountName();
            navsaBalanceResponseDTO.setAccountName(fullName);
            navsaBalanceResponseDTO.setBankCode(bankCode);
            navsaBalanceResponseDTO.setTotalInflowAmt(totalDeposits);
            navsaBalanceResponseDTO.setTotalOutflowAmt(totalWithdrawals);
            navsaBalanceResponseDTO.setLedgerBalance(walletAccount.getCurrentBalance());
            navsaBalanceResponseDTO.setNetBalance(0.0);
            navsaBalanceResponseDTO.setCurrency("NGN");
            navsaBalanceResponseDTO.setAccountNumber(walletAccount.getAccountNumber());
            navsaBalanceResponseDTO.setAvailableBalance(Double.valueOf(walletAccount.getActualBalance()));

            LOG.info("ACCOUNT BALANCE RESPONSE  ===> " + new ObjectMapper().writeValueAsString(navsaBalanceResponseDTO));

            List<NavsaBalanceResponseDTO> navsaBalanceResponseDTOS = new ArrayList<>();
            navsaBalanceResponseDTOS.add(navsaBalanceResponseDTO);


            response.setRespoincecode(ExtendedConstants.ResponseCode.SUCCESS.getCode());
            response.setResponseMsg("Successful");
            response.setResponseData(navsaBalanceResponseDTOS);

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.setRespoincecode(ExtendedConstants.ResponseCode.INVALID_ACCOUNT.getCode());
        response.setResponseMsg(ExtendedConstants.ResponseCode.INVALID_ACCOUNT.getDescription());

        LOG.info("ACCOUNT BALANCE FAIL RESPONSE  ===> " + new ObjectMapper().writeValueAsString(response));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/customer/accounts/{bankCode}/{accountNumber}")
    public ResponseEntity<GenericNavsaResponse> getVirtualAccountDetails(@PathVariable String bankCode,
                                                                         @PathVariable String accountNumber) {

        WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(accountNumber);

        GenericNavsaResponse response = new GenericNavsaResponse();

        if (walletAccount == null) {
            response.setRespoincecode(ExtendedConstants.ResponseCode.CUSTOMER_ACCOUNT_NOT_FOUND.getCode());
            response.setResponseMsg(ExtendedConstants.ResponseCode.CUSTOMER_ACCOUNT_NOT_FOUND.getDescription());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.setRespoincecode(ExtendedConstants.ResponseCode.SUCCESS.getCode());
        response.setResponseMsg("Remita Wallet Account Opened Ok");
        NavsaAccountOpenDTO navsaAccountOpenDTO = new NavsaAccountOpenDTO();

        String accountName = walletAccount.getAccountOwner() != null ? walletAccount.getAccountOwner().getFullName() : walletAccount.getAccountName();
        String phoneNumber = walletAccount.getAccountOwner() != null ? walletAccount.getAccountOwner().getPhoneNumber() : "";
        navsaAccountOpenDTO.setAccountName(accountName);
        navsaAccountOpenDTO.setAccountNo(walletAccount.getAccountNumber());
        navsaAccountOpenDTO.setBankCode(bankCode);
        navsaAccountOpenDTO.setCustomerId(phoneNumber);
        response.setResponseData(navsaAccountOpenDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "/account/open")
    public ResponseEntity<GenericNavsaResponse> openWalletAccount(@Valid @RequestBody NAVSAAccountDto accountDto,
                                                                  HttpSession session) {

        GenericNavsaResponse genericNavsaResponse = new GenericNavsaResponse();

        if (Strings.isEmpty(accountDto.getPhoneNumber())) {
            genericNavsaResponse.setRespoincecode("99");
            genericNavsaResponse.setResponseMsg("Phone Number Cannot be Empty");
            return new ResponseEntity<>(genericNavsaResponse, HttpStatus.BAD_REQUEST);
        }

        if (accountDto.getPhoneNumber().length() < 11 || accountDto.getPhoneNumber().length() > 15) {
            genericNavsaResponse.setRespoincecode("99");
            genericNavsaResponse.setResponseMsg("Phone number must be between 11 to 13 digits");
            return new ResponseEntity<>(genericNavsaResponse, HttpStatus.BAD_REQUEST);
        }

        String phoneNumber = utility.formatPhoneNumber(accountDto.getPhoneNumber());
        accountDto.setPhoneNumber(phoneNumber);

        String deviceNotificationToken = String.valueOf(UUID.randomUUID());
        String dateOfBirth = accountDto.getDateOfBirth().trim();
        LocalDate parseDateOfBirth;

        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            parseDateOfBirth = LocalDate.parse(dateOfBirth, formatter);
        } catch (Exception e) {
            e.printStackTrace();

            genericNavsaResponse.setRespoincecode("99");
            genericNavsaResponse.setResponseMsg("Invalid date of birth");
            return new ResponseEntity<>(genericNavsaResponse, HttpStatus.BAD_REQUEST);
        }

        WalletExternalDTO walletExternalDTO = new WalletExternalDTO();
        walletExternalDTO.setPhoneNumber(accountDto.getPhoneNumber());
        walletExternalDTO.setAccountName(accountDto.getAccountName());
        walletExternalDTO.setDateOfBirth(parseDateOfBirth);
        walletExternalDTO.setDeviceNotificationToken(deviceNotificationToken);
        walletExternalDTO.setEmail(accountDto.getEmail());
        walletExternalDTO.setFirstName(accountDto.getFirstName());
        walletExternalDTO.setGender(accountDto.getGender());
        walletExternalDTO.setLastName(accountDto.getLastName());
        walletExternalDTO.setOpeningBalance(0.0);
        walletExternalDTO.setPassword(defaultPass);
        walletExternalDTO.setPin(pin);
        walletExternalDTO.setScheme(schemeID);

        try {
            ResponseEntity<GenericResponseDTO> walletExternal = walletAccountService.createWalletExternal(walletExternalDTO, session);
            LOG.info("result ===> " + walletExternal);
            GenericResponseDTO body = walletExternal.getBody();
            LOG.info("result body ===> " + body);

            if (HttpStatus.OK.equals(walletExternal.getStatusCode()) && body != null) {
                List<WalletAccountDTO> result = (List<WalletAccountDTO>) body.getData();

                WalletAccountDTO walletAccountDTO = result.get(0);
                NavsaAccountOpenDTO navsaAccountOpenDTO = new NavsaAccountOpenDTO();
                navsaAccountOpenDTO.setCustomerId(utility.returnPhoneNumberFormat(phoneNumber));
                navsaAccountOpenDTO.setBankCode("598");
                navsaAccountOpenDTO.setAccountName(accountDto.getFirstName() + " " + accountDto.getLastName());
                navsaAccountOpenDTO.setAccountNo(walletAccountDTO.getAccountNumber());

                List<NavsaAccountOpenDTO> navsaAccountOpenDTOS = new ArrayList<>();
                navsaAccountOpenDTOS.add(navsaAccountOpenDTO);

                List<OtherAccount> otherAccounts = accountDto.getOtherAccounts();
                if (otherAccounts != null && !otherAccounts.isEmpty()) {
                    try {
                        for (OtherAccount otherAccount : otherAccounts) {

                            Long otherAccountNumber = utility.getUniqueAccountNumber();
                            Profile profile = profileService.findByPhoneNumber(phoneNumber);

                            if (profile != null) {
                                WalletAccountDTO otherWalletAccount = new WalletAccountDTO();
                                otherWalletAccount.setAccountNumber(String.valueOf(otherAccountNumber));
                                otherWalletAccount.setAccountOwnerPhoneNumber(phoneNumber);
                                otherWalletAccount.setAccountOwnerId(profile.getId());
                                otherWalletAccount.setAccountName(otherAccount.getAccountName());
                                otherWalletAccount.setDateOpened(LocalDate.now());
                                otherWalletAccount.setCurrentBalance(0.00);
                                otherWalletAccount.setActualBalance(otherWalletAccount.getCurrentBalance());
                                otherWalletAccount.setSchemeId(3L);
                                otherWalletAccount.setWalletAccountTypeId(1L);

                                if (otherAccount.getIsRestrictedWallet()) {
                                    otherWalletAccount.setStatus(AccountStatus.DEBIT_ON_HOLD);
                                } else {
                                    otherWalletAccount.setStatus(AccountStatus.ACTIVE);
                                }
                                otherWalletAccount.setAccountOwnerName(profile.getFullName());

                                WalletAccountDTO otherWallet = walletAccountService.save(otherWalletAccount);

                                if (otherWallet == null) {
                                    genericNavsaResponse.setRespoincecode("99");
                                    genericNavsaResponse.setResponseMsg("Failed to create a new account");
                                    return new ResponseEntity<>(genericNavsaResponse, HttpStatus.BAD_REQUEST);
                                }

                                NavsaAccountOpenDTO navsaAccountOpenDTO1 = new NavsaAccountOpenDTO();
                                navsaAccountOpenDTO1.setCustomerId(utility.returnPhoneNumberFormat(phoneNumber));
                                navsaAccountOpenDTO1.setBankCode("598");
                                navsaAccountOpenDTO1.setAccountName(accountDto.getFirstName() + " " + accountDto.getLastName());
                                navsaAccountOpenDTO1.setAccountNo(otherWallet.getAccountNumber());

                                navsaAccountOpenDTOS.add(navsaAccountOpenDTO1);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        genericNavsaResponse.setResponseMsg("Error creating other wallet accounts");
                        genericNavsaResponse.setRespoincecode("99");
                        return new ResponseEntity<>(genericNavsaResponse, HttpStatus.BAD_REQUEST);
                    }
                }

                genericNavsaResponse.setRespoincecode("00");
                genericNavsaResponse.setResponseMsg("Remita Wallet Account Opened Ok");
                genericNavsaResponse.setResponseData(navsaAccountOpenDTOS);

                return new ResponseEntity<>(genericNavsaResponse, HttpStatus.OK);

            }

            genericNavsaResponse.setRespoincecode("99");
            if (body != null) {
                genericNavsaResponse.setResponseMsg(body.getMessage());
            } else {
                genericNavsaResponse.setResponseMsg("Error while processing");
            }
            return new ResponseEntity<>(genericNavsaResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {

            e.printStackTrace();
            genericNavsaResponse.setResponseMsg(e.getLocalizedMessage());
            genericNavsaResponse.setRespoincecode("99");
            return new ResponseEntity<>(genericNavsaResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "/account/add")
    public ResponseEntity<GenericNavsaResponse> addNewWalletAccount(@Valid @RequestBody NavsaAddWalletDTO addWalletDTO,
                                                                    HttpSession session) {

        GenericNavsaResponse genericNavsaResponse = new GenericNavsaResponse();

        System.out.println("NavsaAddWalletDTO ===> " + addWalletDTO);

        Long accountNumber = utility.getUniqueAccountNumber();

        String accountName = addWalletDTO.getAccountName();
        String customerId = addWalletDTO.getCustomerId();
        customerId = utility.formatPhoneNumber(customerId);


        Profile profile = profileService.findByPhoneNumber(customerId);
        if (profile == null) {
            genericNavsaResponse.setRespoincecode("99");
            genericNavsaResponse.setResponseMsg("Invalid customerId");
            return new ResponseEntity<>(genericNavsaResponse, HttpStatus.BAD_REQUEST);
        }

        WalletAccountDTO walletAccountDTO = new WalletAccountDTO();
        walletAccountDTO.setAccountNumber(String.valueOf(accountNumber));
        walletAccountDTO.setAccountOwnerPhoneNumber(customerId);
        walletAccountDTO.setAccountOwnerId(profile.getId());
        walletAccountDTO.setAccountName(accountName);
        walletAccountDTO.setDateOpened(LocalDate.now());
        walletAccountDTO.setCurrentBalance(0.00);
        walletAccountDTO.setActualBalance(walletAccountDTO.getCurrentBalance());
        walletAccountDTO.setSchemeId(3L);
        walletAccountDTO.setWalletAccountTypeId(1L);

        if (addWalletDTO.getIsRestrictedWallet()) {
            walletAccountDTO.setStatus(AccountStatus.DEBIT_ON_HOLD);
        } else {
            walletAccountDTO.setStatus(AccountStatus.ACTIVE);
        }
        walletAccountDTO.setAccountOwnerName(profile.getFullName());

        WalletAccountDTO result = walletAccountService.save(walletAccountDTO);

        if (result == null) {

            genericNavsaResponse.setRespoincecode("99");
            genericNavsaResponse.setResponseMsg("Failed to create a new account");
            return new ResponseEntity<>(genericNavsaResponse, HttpStatus.BAD_REQUEST);
        }

        NavsaAccountOpenDTO navsaAccountOpenDTO = new NavsaAccountOpenDTO();
        navsaAccountOpenDTO.setAccountNo(result.getAccountNumber());
        navsaAccountOpenDTO.setAccountName(result.getAccountName());
        navsaAccountOpenDTO.setBankCode("598");
        navsaAccountOpenDTO.setCustomerId(utility.returnPhoneNumberFormat(customerId));

        genericNavsaResponse.setRespoincecode("00");
        genericNavsaResponse.setResponseMsg("Remita Wallet Account Opened Ok");
        List<NavsaAccountOpenDTO> navsaAccountOpenDTOList = new ArrayList<>();
        navsaAccountOpenDTOList.add(navsaAccountOpenDTO);
        genericNavsaResponse.setResponseData(navsaAccountOpenDTOList);

        return new ResponseEntity<>(genericNavsaResponse, HttpStatus.OK);

    }

   /* @PostMapping(path = "/single-transfer")
    public ResponseEntity<GenericNavsaResponse> navsaSingleTransfer(@Valid @RequestBody
                                                                            NavsaSingleTransferDTO navsaSingleTransferDTO) {

        new FundDTO();


    }*/

}
