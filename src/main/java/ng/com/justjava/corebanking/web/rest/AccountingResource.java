package ng.com.justjava.corebanking.web.rest;

import io.github.jhipster.web.util.PaginationUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

import ng.com.justjava.corebanking.domain.Journal;
import ng.com.justjava.corebanking.domain.JournalLine;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.SpecificChannel;
import ng.com.justjava.corebanking.domain.enumeration.TransactionStatus;
import ng.com.justjava.corebanking.service.JournalLineService;
import ng.com.justjava.corebanking.service.JournalService;
import ng.com.justjava.corebanking.service.RITSService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.accounting.AccountingService;
import ng.com.justjava.corebanking.util.FlutterWaveUtil;
import ng.com.justjava.corebanking.service.dto.AccountVerificationDTO;
import ng.com.justjava.corebanking.service.dto.AccountVerificationResponseDTO;
import ng.com.justjava.corebanking.service.dto.ChargesDTO;
import ng.com.justjava.corebanking.service.dto.DayBookDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.ReverseDTO;
import ng.com.systemspecs.remitarits.accountenquiry.AccountEnqiryRequest;
import ng.com.systemspecs.remitarits.accountenquiry.AccountEnquiryResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * REST controller for managing {@link JournalLine Functions of the Application}.
 */
@RestController
@RequestMapping("/api")
public class AccountingResource {

    private final Logger log = LoggerFactory.getLogger(AccountingResource.class);
    private final JournalLineService journalLineService;
    private final JournalService journalService;
    private final RITSService rITSService;
    private final WalletAccountService walletAccountService;
    private final AccountingService accountingService;
    private final FlutterWaveUtil flutterWaveUtil;
    private Boolean useAlternateAccountLookup = false;


    public AccountingResource(JournalLineService journalLineService, RITSService rITSService,
        WalletAccountService walletAccountService,
        JournalService journalService, AccountingService accountingService,
        FlutterWaveUtil flutterWaveUtil) {
        this.journalLineService = journalLineService;
        this.rITSService = rITSService;
        this.walletAccountService = walletAccountService;
        this.journalService = journalService;

        this.accountingService = accountingService;
        this.flutterWaveUtil = flutterWaveUtil;
    }

    @GetMapping("/debit/{accountNumber}")
    public ResponseEntity<List<JournalLine>> getFundTransaction(
        @PathVariable String accountNumber) {
        log.debug("REST request to get PaymentTransaction : {}", accountNumber);
        List<JournalLine> creditLines = journalLineService
            .findByWalletAccountAccountNumberAndDebitGreaterThan(accountNumber, 0D);
        return new ResponseEntity<>(creditLines, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/receipt")
    public ResponseEntity<List<JournalLine>> getSendMoneyTransaction(HttpSession session) {
        log.debug("REST request to get Receipt :");
        List<JournalLine> creditLines = journalLineService.findCustomerReceipt(session);

        return new ResponseEntity<>(creditLines, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/receipts")
    public ResponseEntity<LinkedHashSet<Journal>> getReceipts(HttpSession session) {
        log.debug("REST request to get Receipt :");
        LinkedHashSet<Journal> creditLines = journalLineService.findCustomerReceipts(session);
//        List<JournalLine> creditLines = journalLineService.findCustomerReceipts(session);

        return new ResponseEntity<>(creditLines, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/customer_debit_credit")
    public ResponseEntity<List<JournalLine>> getAllMyTransaction(HttpSession session) {
        log.debug("REST request to get PaymentTransaction : {}", "");
        List<JournalLine> creditLines = journalLineService
            .findByUserIsCurrentUserOrderByTransactionDate(session);
        return new ResponseEntity<>(creditLines, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/customer_debit_credit/{fromDate}/{toDate}")
    public ResponseEntity<List<JournalLine>> getAllMyTransactionDateRange(
        @PathVariable LocalDate fromDate, @PathVariable LocalDate toDate, HttpSession session) {
        log.debug("REST request to get PaymentTransaction : {}", "");
        List<JournalLine> creditLines = journalLineService
            .findByUserIsCurrentUserOrderByTransactionDateDateRange(fromDate.atStartOfDay(),
                toDate.atTime(LocalTime.MAX), session);
        return new ResponseEntity<>(creditLines, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/customer/{phoneNumber}/transactions")
    public ResponseEntity<List<JournalLine>> getAllCustomerTransactions(
        @PathVariable String phoneNumber, HttpSession session) {
        log.debug("REST request to get PaymentTransaction : {}", "");
        List<JournalLine> creditLines = journalLineService
            .findByWalletAccountAccountOwnerPhoneNumber(phoneNumber, session);
        return new ResponseEntity<>(creditLines, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/customer/{phoneNumber}/transactions/{fromDate}/{toDate}")
    public ResponseEntity<List<JournalLine>> getCustomerTransactionsWithDateRange(
        @PathVariable String phoneNumber, HttpSession session) {
        log.debug("REST request to get PaymentTransaction with date range : {}", "");
        List<JournalLine> creditLines = journalLineService
            .findByWalletAccountAccountOwnerPhoneNumber(phoneNumber, session);
        return new ResponseEntity<>(creditLines, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/customer_debit_credit/airtime_data")
    public ResponseEntity<List<JournalLine>> getAllMyAirtimeAndDataTransactions(
        HttpSession session) {
        log.debug("REST request to get Airtime data history : {}", "");
        List<JournalLine> creditLines = journalLineService
            .findByUserIsCurrentUserOrderByTransactionDateAirtimeData(session);
        return new ResponseEntity<>(creditLines, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/account_debit_credit/{accountNumber}")
    public ResponseEntity<List<JournalLine>> getAllAccountTransaction(
        @PathVariable String accountNumber) {
        log.debug("REST request to get PaymentTransaction : {}", "");
        List<JournalLine> creditLines = journalLineService
            .findByWalletAccount_AccountNumber(accountNumber);
        return new ResponseEntity<>(creditLines, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/account_debit_credit/{accountNumber}/{fromDate}/{toDate}")
    public ResponseEntity<List<JournalLine>> getAllAccountTransactionWithDateRange(
        @PathVariable String accountNumber, @PathVariable LocalDate fromDate,
        @PathVariable LocalDate toDate) {
        log.debug("REST request to get PaymentTransaction : {}", "");
        List<JournalLine> creditLines = journalLineService
            .findByWalletAccount_AccountNumberWithDateRange(accountNumber, fromDate.atStartOfDay(),
                toDate.atTime(LocalTime.MAX));
        return new ResponseEntity<>(creditLines, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/myinvoices")
    public ResponseEntity<List<JournalLine>> getMyInvoices() {
        log.debug("REST request to get PaymentTransaction : {}", "");
        List<JournalLine> creditLines = journalLineService.findCustomerInvoice();
        return new ResponseEntity<>(creditLines, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/daybook")
    public ResponseEntity<List<DayBookDTO>> getDayBook() {
        log.debug("REST request to get PaymentTransaction : {}", "");
        List<DayBookDTO> dayBookDTOs = journalService.findDayBookDTOs();
        return new ResponseEntity<>(dayBookDTOs, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/daybook/{reference}")
    public ResponseEntity<DayBookDTO> getDayBookByRefrence(@PathVariable String reference) {
        DayBookDTO dayBook = journalService.findDaybookDTOByReference(reference);
        return new ResponseEntity<>(dayBook, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping({"/verify-account"})
    public AccountVerificationResponseDTO getAccountEnquiry(
        @RequestBody AccountVerificationDTO accountVerificationDTO) {

        AccountVerificationResponseDTO accountVerificationResponseDTO = new AccountVerificationResponseDTO();
        accountVerificationResponseDTO.setAccountNumber(accountVerificationDTO.getAccountNumber());
        if ("wallet".equalsIgnoreCase(accountVerificationDTO.getAccountType())) {
            WalletAccount account = walletAccountService
                .findOneByAccountNumber(accountVerificationDTO.getAccountNumber());
            if (account == null) {
                accountVerificationResponseDTO.setAccountName("Account Not Found");
            } else {
                accountVerificationResponseDTO
                    .setAccountName(account.getAccountOwner().getFullName());
            }
        } else if ("bank".equalsIgnoreCase(accountVerificationDTO.getAccountType())) {
            AccountEnqiryRequest accountEnqiryRequest = new AccountEnqiryRequest();
            accountEnqiryRequest.setAccountNo(accountVerificationDTO.getAccountNumber());
            accountEnqiryRequest.setBankCode(accountVerificationDTO.getBankCode());

            AccountEnquiryResponse accountEnquiryResponse;
            accountEnquiryResponse =
                useAlternateAccountLookup ? flutterWaveUtil.resolveAccounts(accountEnqiryRequest)
                    : rITSService.getAccountEnquiry(accountEnqiryRequest);

            if ("success".equalsIgnoreCase(accountEnquiryResponse.getStatus())) {
                accountVerificationResponseDTO
                    .setAccountName(accountEnquiryResponse.getData().getAccountName());
            } else {
                accountVerificationResponseDTO.setAccountName("Account Not Found");
            }
        }
        return accountVerificationResponseDTO;
    }

    @PutMapping("/switch/avs")
    public ResponseEntity<?> switchBoolean(@RequestParam Boolean switched){
        this.useAlternateAccountLookup = switched;
        return ResponseEntity.ok(this.useAlternateAccountLookup);
    }

    @GetMapping("/account-statement/{accountNumber}/{fromDate}/{toDate}")
    public ResponseEntity<List<JournalLine>> getAccountStatement(@PathVariable String accountNumber,
        @PathVariable LocalDate fromDate,
        @PathVariable LocalDate toDate) {
        log.debug(
            "REST request to get getAccountStatement : {} " + accountNumber + " " + fromDate + " "
                + toDate);
        List<JournalLine> statements = journalLineService.
            findAllByWalletAccountAccountNumberAndJounalTransDateBetween(accountNumber,
                fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX));
        return new ResponseEntity<>(statements, new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/account-statement/{accountNumber}/{fromDate}/{toDate}/{schemeId}")
    public ResponseEntity<GenericResponseDTO> getAccountStatementByScheme(
        @PathVariable String accountNumber,
        @PathVariable LocalDate fromDate,
        @PathVariable LocalDate toDate,
        @PathVariable String schemeId) {
        log.debug(
            "REST request to get getAccountStatement : {} " + accountNumber + " " + fromDate + " "
                + toDate);
        List<JournalLine> statements = journalLineService.
            findAllByWalletAccountAccountNumberAndJounalTransDateBetweenAndScheme(accountNumber,
                fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX), schemeId);

        if (statements != null) {
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", statements),
                HttpStatus.OK);
        }

        return new ResponseEntity<>(
            new GenericResponseDTO("99", "You don't have access to view this report", null),
            new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/account-statement/{accountNumber}/{fromDate}/{toDate}/search")
    public ResponseEntity<List<JournalLine>> getAccountStatement(@PathVariable String accountNumber,
        @PathVariable LocalDate fromDate,
        @PathVariable LocalDate toDate,
        @RequestParam String key) {
        log.debug(
            "REST request to get getAccountStatement : {} " + accountNumber + " " + fromDate + " "
                + toDate);
        List<JournalLine> statements = journalLineService.
            findAllByWalletAccountAccountNumberAndJounalTransDateBetweenSearchByKey(accountNumber,
                fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX), key);
        return new ResponseEntity<>(statements, new HttpHeaders(), HttpStatus.OK);
    }


    @GetMapping("/account-statement/{fromDate}/{toDate}")
    public ResponseEntity<GenericResponseDTO> getAllAccountStatementsByDate(Pageable pageable,
        @PathVariable LocalDate fromDate, @PathVariable LocalDate toDate) {
        log.debug(
            "REST request to get getAllAccountStatementsByDate : {} " + fromDate + " " + toDate);

        Page<JournalLine> statements = journalLineService.
            findAllByJounalTransDateBetween(pageable, fromDate.atStartOfDay(),
                toDate.atTime(LocalTime.MAX));
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
                statements);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", statements.getSize());
        metaMap.put("totalNumberOfRecords", statements.getTotalElements());

        return new ResponseEntity<>(
            new GenericResponseDTO("00", "success", statements.getContent(), metaMap), headers,
            HttpStatus.OK);
    }

    @GetMapping("/account-statement/scheme/{fromDate}/{toDate}/{schemeId}")
    public ResponseEntity<GenericResponseDTO> getAllAccountStatementsByDate(
        @PathVariable LocalDate fromDate,
        @PathVariable LocalDate toDate,
        @PathVariable String schemeId) {
        log.debug(
            "REST request to get getAllAccountStatementsByDate : {} " + fromDate + " " + toDate
                + " " + schemeId);

        List<JournalLine> statements = journalLineService.
            findAllByJounalTransDateBetweenAndScheme(fromDate.atStartOfDay(),
                toDate.atTime(LocalTime.MAX), schemeId);
        if (statements != null) {

            Map<String, Object> metaMap = new LinkedHashMap<>();
            metaMap.put("totalNumberOfRecords", statements.size());

            return new ResponseEntity<>(
                new GenericResponseDTO("00", "success", statements, metaMap), HttpStatus.OK);
        }
        return new ResponseEntity<>(
            new GenericResponseDTO("99", "You don't have access to view this report", null),
            HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/account-statement")
    public ResponseEntity<GenericResponseDTO> getAllAccountStatements(Pageable pageable) {
        log.debug("REST request to get getAllAccountStatements : {} ");
        Page<JournalLine> statements = journalLineService
            .findAllByWalletAccount_AccountOwnerIsNotNull(pageable);
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
                statements);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", statements.getSize());
        metaMap.put("totalNumberOfRecords", statements.getTotalElements());

        return new ResponseEntity<>(
            new GenericResponseDTO("00", "success", statements.getContent(), metaMap), headers,
            HttpStatus.OK);
    }

    @GetMapping("/account-statement/search")
    public ResponseEntity<GenericResponseDTO> getAllAccountStatements(Pageable pageable,
        @RequestParam(required = false) String key) {
        log.debug("REST request to get getAllAccountStatements : {} " + key);
        Page<JournalLine> statements = journalLineService
            .findAllByWalletAccount_AccountOwnerIsNotNullSearch(key, pageable);
        List<JournalLine> content = statements.getContent();

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
                statements);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", statements.getSize());
        metaMap.put("totalNumberOfRecords", statements.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", content, metaMap),
            headers, HttpStatus.OK);
    }

    @GetMapping("/account-statement/wallet/{accountNumber}/search")
    public ResponseEntity<GenericResponseDTO> getWalletAccountStatements(Pageable pageable,
        @PathVariable String accountNumber, @RequestParam String key) {
        log.debug("REST request to get getAllAccountStatements of a wallet : {} " + accountNumber);
        Page<JournalLine> statements = journalLineService
            .findAllByWalletAccount_AccountOwnerIsNotNullAndWalletAccountNumberSearch(accountNumber,
                key, pageable);
        List<JournalLine> content = statements.getContent();

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
                statements);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", statements.getSize());
        metaMap.put("totalNumberOfRecords", statements.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", content, metaMap),
            headers, HttpStatus.OK);
    }

    @GetMapping("/account-statement/{schemeId}/search")
    public ResponseEntity<GenericResponseDTO> getAllAccountStatementsByScheme(
        @RequestParam String key, @PathVariable String schemeId, Pageable pageable) {
        log.debug("REST request to get getAllAccountStatements by scheme : {} " + schemeId);

        Page<JournalLine> journalLines = journalLineService
            .findAllByWalletAccount_AccountOwnerIsNotNullSearchAndScheme(key, schemeId, pageable);

        List<JournalLine> content = journalLines.getContent();

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
                journalLines);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", journalLines.getSize());
        metaMap.put("totalNumberOfRecords", journalLines.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", content, metaMap),
            headers, HttpStatus.OK);
    }

    @GetMapping("/journals")
    public ResponseEntity<GenericResponseDTO> getAllJournals(Pageable pageable) {
        log.debug("REST request to get getAllAccountStatements : {} ");
        List<Journal> all = journalService.findAll();

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", all.size());
        metaMap.put("totalNumberOfRecords", all.size());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", all, metaMap),
            HttpStatus.OK);
    }

    @GetMapping(path = "/inline-payment", produces = MediaType.TEXT_HTML_VALUE)
    public String getInlineHTML(Map<String, Object> model) {

        try {
            FileInputStream fis = new FileInputStream("src/main/resources/static/inline.html");
            return IOUtils.toString(fis, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/journal/hold/{transRef}")
    public ResponseEntity<GenericResponseDTO> holdTransactionStatus(@PathVariable String transRef) {
        GenericResponseDTO genericResponseDTO = journalService
            .changeTransactionStatus(transRef, TransactionStatus.SUSPENDED);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("/journal/unhold/{transRef}")
    public ResponseEntity<GenericResponseDTO> unHoldTransactionStatus(
        @PathVariable String transRef) {
        GenericResponseDTO genericResponseDTO = journalService
            .changeTransactionStatus(transRef, TransactionStatus.COMPLETED);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("/journal/{transRef}/{status}")
    public ResponseEntity<GenericResponseDTO> unHoldTransactionStatus(@PathVariable String transRef,
        @PathVariable String status) {

        try {
            TransactionStatus transactionStatus = TransactionStatus.valueOf(status.toUpperCase());

            GenericResponseDTO genericResponseDTO = journalService
                .changeTransactionStatus(transRef, transactionStatus);
            return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(
            new GenericResponseDTO("99", "Invalid transaction status", null),
            HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/reverse")
    public ResponseEntity<GenericResponseDTO> reverseTransaction(
        @RequestBody ReverseDTO reverseDTO) {
        log.info("Reverse transaction DTO {} ", reverseDTO);
        GenericResponseDTO genericResponseDTO = journalService.reverseTransaction(reverseDTO);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @GetMapping("/charges/{amount}/{specificChannel}")
    public ResponseEntity<GenericResponseDTO> getSendMoneyCharges(@PathVariable Double amount,
        @PathVariable String specificChannel) {
        double charges = 0.0;
        try {
            if (!amount.isNaN() && amount > 0) {
                charges = accountingService.getCharges(amount, specificChannel);
                return new ResponseEntity<>(new GenericResponseDTO("00", "successful", charges),
                    HttpStatus.OK);
            }
        } catch (Exception ex) {
            return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid request", null),
                HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new GenericResponseDTO("00", "successful", charges),
            HttpStatus.OK);
    }

    @GetMapping("/get-charges/{amount}/{specificChannel}")
    public ResponseEntity<GenericResponseDTO> getTxnFees
        (@PathVariable Double amount, @PathVariable String specificChannel) {
        if (!amount.isNaN() && amount > 0) {
            Double intraFee = 0.0;
            intraFee = accountingService.getTransactionFee(amount, specificChannel, null);
            Double vat = accountingService.getVATFee(intraFee);
            if (SpecificChannel.POLARIS_CARD_REQUEST.getName().equalsIgnoreCase(specificChannel)) {
                vat = accountingService.getVATFee(amount);
            }
            ChargesDTO charges = new ChargesDTO();
            charges.setTransactionFee(intraFee);
            charges.setVat(vat);
            return new ResponseEntity<>(new GenericResponseDTO("00", "successful", charges),
                HttpStatus.OK);
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid request", null),
            HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/account-statements/transit/debits")
    public ResponseEntity<GenericResponseDTO> getAllTransitAccountDebits(Pageable pageable) {
        Page<JournalLine> transitAccountDebits = journalLineService
            .getAllTransitAccountDebitTransactions(pageable);

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
                transitAccountDebits);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", transitAccountDebits.getSize());
        metaMap.put("totalNumberOfRecords", transitAccountDebits.getTotalElements());

        return new ResponseEntity<>(
            new GenericResponseDTO("00", "Success", transitAccountDebits.getContent(), metaMap),
            headers, HttpStatus.OK);
    }

    @GetMapping("/account-statements/transit/credits")
    public ResponseEntity<GenericResponseDTO> getAllTransitAccountCredits(Pageable pageable) {
        Page<JournalLine> transitAccountCredits = journalLineService
            .getAllTransitAccountCreditTransactions(pageable);

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
                transitAccountCredits);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", transitAccountCredits.getSize());
        metaMap.put("totalNumberOfRecords", transitAccountCredits.getTotalElements());

        return new ResponseEntity<>(
            new GenericResponseDTO("00", "Success", transitAccountCredits.getContent(), metaMap),
            headers, HttpStatus.OK);
    }

    @GetMapping("/account-statements/transit")
    public ResponseEntity<GenericResponseDTO> getAllTransitAccountTransactions(Pageable pageable) {
        Page<JournalLine> transitAccountTransactions = journalLineService
            .getAllTransitAccountTransactions(pageable);

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
                transitAccountTransactions);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", transitAccountTransactions.getSize());
        metaMap.put("totalNumberOfRecords", transitAccountTransactions.getTotalElements());

        return new ResponseEntity<>(
            new GenericResponseDTO("00", "Success", transitAccountTransactions.getContent(),
                metaMap), headers, HttpStatus.OK);
    }

    @PostMapping("/reverse/transit/{transRef}")
    public ResponseEntity<GenericResponseDTO> reverseTransitTransaction(
        @PathVariable String transRef) {
        GenericResponseDTO genericResponseDTO = journalService.reverseTransitTransaction(transRef);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("/reverse/charges/{transRef}/{chargesAmount}")
    public ResponseEntity<GenericResponseDTO> reverseCharges(@PathVariable String transRef,
        @PathVariable double chargesAmount) {
        GenericResponseDTO genericResponseDTO = journalService
            .reverseCharges(transRef, chargesAmount);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }


    @GetMapping("/account-statements/{accountNumber}/{status}/{startDate}/{endDate}")
    public ResponseEntity<GenericResponseDTO> getStatementsByStatusAccountNumberAndDateRange(
        Pageable pageable,
        @PathVariable String accountNumber,
        @PathVariable String status,
        @PathVariable LocalDate startDate,
        @PathVariable LocalDate endDate) {

        TransactionStatus transactionStatus;

        try {
            transactionStatus = TransactionStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", "Invalid status", null),
                HttpStatus.BAD_REQUEST);

        }
        Page<JournalLine> journalLines = journalLineService
            .getAllJournalLinesByStatusAndAccountNumberDateRange(pageable, transactionStatus,
                accountNumber,
                startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(),
                journalLines);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", journalLines.getSize());
        metaMap.put("totalNumberOfRecords", journalLines.getTotalElements());

        return new ResponseEntity<>(
            new GenericResponseDTO("00", "success", journalLines.getContent(), metaMap), headers,
            HttpStatus.OK);
    }

    @GetMapping("/account-statements/agent/{superAgentPhoneNumber}/{fromDate}/{toDate}")
    public ResponseEntity<GenericResponseDTO> getSuperAgentStatement(Pageable pageable,
        @PathVariable String superAgentPhoneNumber,
        @PathVariable LocalDate fromDate,
        @PathVariable LocalDate toDate) {

        log.debug(
            "REST request to get getAllAccountStatementsByDate : {} " + fromDate + " " + toDate,
            superAgentPhoneNumber);

        List<JournalLine> statements = journalLineService.
            findAllSuperAgentJounalTransDateBetween(pageable, superAgentPhoneNumber,
                fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX));

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", statements),
            HttpStatus.OK);
    }

  /*  @GetMapping("/account-statements/scheme/{schemeId}/{fromDate}/{toDate}/search")
    public ResponseEntity<GenericResponseDTO> getTransactionsByScheme(Pageable pageable,
                                                                     @PathVariable String schemeId,
                                                                     @PathVariable LocalDate fromDate,
                                                                     @PathVariable LocalDate toDate) {

        log.debug("REST request to get getAllAccountStatementsByDate and Scheme: {} " + fromDate + " " + toDate, schemeId);

        List<JournalLine> statements = journalLineService.
            findAllSuperAgentJounalTransDateBetween(pageable, superAgentPhoneNumber, fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX));

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", statements), HttpStatus.OK);
    }*/

    @GetMapping("/bonus-points/customer/{phoneNumber}")
    public ResponseEntity<GenericResponseDTO> getCustomerBonusPoints(
        @PathVariable String phoneNumber) {
        log.debug("REST request to get login customer BonusPoints");
        List<JournalLine> journalLines = journalLineService.getCustomerBonusPoints(phoneNumber);

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", journalLines),
            HttpStatus.OK);
    }

    @GetMapping("/statements/{transRef}")
    public ResponseEntity<GenericResponseDTO> getTransactionStatements(
        @PathVariable String transRef) {
        log.debug("REST request to get login customer BonusPoints");

        List<JournalLine> journalLines = journalLineService.getTransactionStatements(transRef);

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", journalLines),
            HttpStatus.OK);
    }

    @GetMapping("/unique/transRef")
    public ResponseEntity<GenericResponseDTO> getUniqueTransRef() {
        log.debug("REST request to get login customer BonusPoints");

        String accountNumber = journalLineService.getUniqueTransRef();

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", accountNumber),
            HttpStatus.OK);
    }

    @GetMapping("/all-agents/transactions")
    public ResponseEntity<GenericResponseDTO> getSuperAgentAgentsTransactions(Pageable pageable) {

        log.debug("REST request to get login customer BonusPoints");

        GenericResponseDTO genericResponseDTO = journalLineService
            .getSuperAgentAgentsTransactions(pageable);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());

    }

    @GetMapping("/all-tellers/transactions")
    public ResponseEntity<GenericResponseDTO> getSuperAgentTellersTransactions(Pageable pageable) {

        log.debug("REST request to get login customer BonusPoints");

        GenericResponseDTO genericResponseDTO = journalLineService
            .getSuperAgentTellersTransactions(pageable);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());

    }

//    @GetMapping("/admin/wallets/summary/{schemeId}/{fromDate}/{toDate}")
//    public ResponseEntity<GenericResponseDTO> getAllWalletsSummary(
//        @PathVariable String schemeId,
//        @PathVariable LocalDate fromDate,
//        @PathVariable LocalDate toDate) {
//
//        log.debug("REST request to fetch customer wallets summary by Admin,{}", schemeId + " ==== "+ fromDate + " " +
//            "====-= " + toDate);
//
//        GenericResponseDTO genericResponseDTO = journalLineService.getAllWalletsSummary(schemeId,
//            fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX));
//
//        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
//    }

}
