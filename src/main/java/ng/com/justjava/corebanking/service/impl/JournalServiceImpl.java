package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.JournalRepository;
import ng.com.justjava.corebanking.service.accounting.AccountingService;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.kafka.producer.TransProducer;
import ng.com.justjava.corebanking.domain.Journal;
import ng.com.justjava.corebanking.domain.JournalLine;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.PaymentType;
import ng.com.justjava.corebanking.domain.enumeration.SpecificChannel;
import ng.com.justjava.corebanking.domain.enumeration.TransactionStatus;
import ng.com.justjava.corebanking.service.JournalLineService;
import ng.com.justjava.corebanking.service.JournalService;
import ng.com.justjava.corebanking.service.TransactionLogService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.System.out;

@Service
@Transactional
public class JournalServiceImpl implements JournalService {

    private final Logger log = LoggerFactory.getLogger(JournalServiceImpl.class);

    @Value("${app.constants.dfs.transit-account}")
    private String TRANSIT_ACCOUNT;

    @Value("${app.constants.dfs.biller-payment-account}")
    private String BILLER_PAYMENT_ACCOUNT;

    @Value("${app.constants.dfs.charge-account}")
    private String CHARGE_ACCOUNT;

    @Value("${app.constants.dfs.correspondence-account}")
    private String CORRESPONDENCE_ACCOUNT;


    private final JournalRepository journalRepository;
    private final JournalLineService journalLineService;
    private final WalletAccountService walletAccountService;
    private final TransactionLogService transactionLogService;
    private final AccountingService accountingService;
    private final TransProducer transProducer;

    public JournalServiceImpl(JournalRepository journalRepository,
                              JournalLineService journalLineService, WalletAccountService walletAccountService, TransactionLogService transactionLogService, @Lazy AccountingService accountingService, TransProducer transProducer) {
        this.journalRepository = journalRepository;
        this.journalLineService = journalLineService;
        this.walletAccountService = walletAccountService;
        this.transactionLogService = transactionLogService;
        this.accountingService = accountingService;
        this.transProducer = transProducer;
    }

    @Override
    public Journal save(Journal journal) {
        // TODO Auto-generated method stub
        return journalRepository.save(journal);
    }

    @Override
    public List<Journal> findAll() {
        // TODO Auto-generated method stub
        return journalRepository.findAll();
    }

    @Override
    public Optional<Journal> findOne(Long id) {
        // TODO Auto-generated method stub
        return journalRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
    }

    @Override
    public Optional<Journal> findByReference(String reference) {
        return journalRepository.findByReferenceIgnoreCase(reference);
    }

    @Override
    public GenericResponseDTO changeTransactionStatus(String transRef, TransactionStatus status) {

        Optional<Journal> journalOptional = journalRepository.findByReferenceIgnoreCase(transRef);
        if (journalOptional.isPresent()) {
            Journal journal = journalOptional.get();
            journal.setTransactionStatus(status);
            Journal save = journalRepository.save(journal);
            log.info("Updated journal status ===> " + journal);

            List<JournalLine> journalLines = journalLineService.findByJounalReference(transRef);
            journalLines.forEach(journalLine -> {
                WalletAccount walletAccount = journalLine.getWalletAccount();
                log.info("Wallet Account ====> " + walletAccount);
                if (walletAccount != null) {
                    double currentBalance = Double.parseDouble(walletAccount.getCurrentBalance());
                    Double suspendedTransactionsAmount = journalLineService.sumOfSuspendedTransactionsAmount(walletAccount.getAccountNumber());
                    walletAccount.setActualBalance(String.valueOf(currentBalance - suspendedTransactionsAmount));
                    WalletAccount save1 = walletAccountService.save(walletAccount);
                    log.info("Updated waller account  ===> " + save1);
                }
            });
            return new GenericResponseDTO("00", HttpStatus.OK, "success", save);
        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Transaction reference", null);
    }

    @Override
    public GenericResponseDTO reverseTransaction(ReverseDTO reverseDTO) {

        FundDTO byTransRef = transactionLogService.findByTransRef(reverseDTO.getTransRef() + "_1");
        if (byTransRef != null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Transaction was already reversed", null);
        }

        FundDTO fundDTO = transactionLogService.findByTransRef(reverseDTO.getTransRef());

        out.println("Reversal : originalTransaction ==> " + fundDTO);

        if (fundDTO == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Transaction", null);
        }

        if (TransactionStatus.REVERSED.equals(fundDTO.getStatus())) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Transaction was already reversed", null);
        }

        String transRef = reverseDTO.getTransRef();

        out.println("Reversal ===> fundDTO " + fundDTO);

        FundDTO reversedFundDTO = reverseFundDTO(fundDTO);

        out.println("Reversal : reversed fundDTO retrieved ==> " + reversedFundDTO);

        if (reverseDTO.getStatus().equalsIgnoreCase("completed")) {

            out.println("Reversal ===> Entering complete block " + reversedFundDTO);


            if ("walletToWallet".equalsIgnoreCase(fundDTO.getChannel())
                && !fundDTO.getAccountNumber().equalsIgnoreCase(BILLER_PAYMENT_ACCOUNT)) {

                out.println("Reversal ===> complete block walletTowallet " + reversedFundDTO);


                reversedFundDTO.setSpecificChannel(SpecificChannel.FUND_WALLET_INTRA.getName());

                out.println("About to send " + reversedFundDTO);

                transProducer.send(reversedFundDTO);

                return new GenericResponseDTO("00", HttpStatus.OK, "success", null);

            } else if ("walletToBank".equalsIgnoreCase(fundDTO.getChannel())) {

                out.println("Reversal ===> complete block walletToBank " + reversedFundDTO);


                //TODO We need to get the bank to help us currently requires manual intervention

                reversedFundDTO.setChannel("WalletToBankReversal");

                out.println("Reversal ===> About to send " + reversedFundDTO);

                transProducer.send(reversedFundDTO);

                return new GenericResponseDTO("00", HttpStatus.OK, "success", null);

            } else if ("bankToWallet".equalsIgnoreCase(fundDTO.getStatus().getName())) {

                out.println("Reversal ===> complete block bankToWallet " + reversedFundDTO);

                //TODO We need IPG not to charge this reversal
                //TODO This fundDTO has to be updated with the customer bank details

                reversedFundDTO.setChannel("BankToWalletReversal");

                out.println("Reversal ===> About to send " + reversedFundDTO);


                transProducer.send(reversedFundDTO);

                return new GenericResponseDTO("00", HttpStatus.OK, "success", null);
            } else if ("INVOICE".equalsIgnoreCase(fundDTO.getChannel())) {

                out.println("Reversal ===> complete block INVOICE " + reversedFundDTO);

                reversedFundDTO.setSpecificChannel(SpecificChannel.FUND_WALLET_INTRA.getName());

                out.println("Reversal ===> About to send " + reversedFundDTO);

                transProducer.send(reversedFundDTO);

                return new GenericResponseDTO("00", HttpStatus.OK, "success", null);

            }

        } else if (reverseDTO.getStatus() != null && reverseDTO.getStatus().equalsIgnoreCase("incomplete")) {

            out.println("Reversal ===> Entering incomplete block " + reversedFundDTO);

            if ("wallettowallet".equalsIgnoreCase(fundDTO.getChannel())) {
                out.println("Reversal ===> incomplete block walletTowallet" + reversedFundDTO);
                out.println("Reversal ===> sending to reverseTransitTransaction" + reversedFundDTO);

                return reverseTransitTransaction(fundDTO.getTransRef());
            }

            if ("(bankToWallet".equalsIgnoreCase(fundDTO.getChannel())) {
                if ("bank".equalsIgnoreCase(reverseDTO.getPointOfFailure())) {

                    out.println("Reversal ===> incomplete block BankTowallet at bank" + reversedFundDTO);

                    reversedFundDTO.setChannel("BankToWalletIncompleteBank");
                    reversedFundDTO.setAccountNumber(CORRESPONDENCE_ACCOUNT);
                    reversedFundDTO.setSpecificChannel(SpecificChannel.FUND_WALLET.getName());

                    out.println("Reversal ===> About to Send " + reversedFundDTO);

                    transProducer.send(reversedFundDTO);

                    return new GenericResponseDTO("00", HttpStatus.OK, "success", null);


                } else if ("wallet".equalsIgnoreCase(reverseDTO.getPointOfFailure())) {

                    out.println("Reversal ===> incomplete block BankTowallet at wallet " + reversedFundDTO);

                    out.println("Reversal ===> About to call fundWalletExternal " + fundDTO.getTransRef());

                    ResponseEntity<PaymentResponseDTO> response = walletAccountService.fundWalletExternal(fundDTO.getTransRef());
                    out.println("Reversal ===> FundWalletExternal response ==> " + response);

                    if (response != null && response.getBody() != null) {
                        return new GenericResponseDTO(response.getBody().getMessage(), response.getStatusCode(), response.getBody().getMessage(), null);
                    }
                }
            }
            if ("(WalletToBank".equalsIgnoreCase(fundDTO.getChannel()) && !fundDTO.getAccountNumber().equalsIgnoreCase(BILLER_PAYMENT_ACCOUNT)) {

                out.println("Reversal ===> incomplete block walletToBank " + reversedFundDTO);

                List<JournalLine> journalLines = journalLineService.findByJounalReference(transRef);

                out.println("Reversal ===> Retrieved journallines ===> " + reversedFundDTO);


                if ("bank".equalsIgnoreCase(reverseDTO.getPointOfFailure())) {

                    out.println("Reversal ===> incomplete block walletToBank at bank" + reversedFundDTO);


                    if (journalLines.size() > 2) {

                        reversedFundDTO.setSourceAccountNumber(CORRESPONDENCE_ACCOUNT);
                        reversedFundDTO.setChannel("WalletToBankIncompleteBank");
                        reversedFundDTO.setSpecificChannel(SpecificChannel.SEND_MONEY.getName());

                        out.println("Reversal ===> Aboout to send " + reversedFundDTO);


                        transProducer.send(reversedFundDTO);

                        return new GenericResponseDTO("00", HttpStatus.OK, "success", null);
                    } else {
                        out.println("Reversal ===> About to call reverse transit transaction" + transRef);

                        return reverseTransitTransaction(transRef);
                    }

                } else if ("wallet".equalsIgnoreCase(reverseDTO.getPointOfFailure())) {

                    out.println("Reversal ===> incomplete block walletToBank at Wallet " + reversedFundDTO);

                    if (journalLines.size() > 2) {

                        fundDTO.setSpecificChannel(SpecificChannel.SEND_MONEY.getName());
                        fundDTO.setAccountNumber(CORRESPONDENCE_ACCOUNT);
                        fundDTO.setChannel("WalletToBankIncompleteWallet");

                        out.println("Reversal ===> About to send " + reversedFundDTO);

                        transProducer.send(fundDTO);

                        return new GenericResponseDTO("00", HttpStatus.OK, "success", null);
                    } else {

                        out.println("Reversal ===> About to call reverse TransitTransaction" + transRef);

                        return reverseTransitTransaction(transRef);
                    }

                }
            }
        }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Transaction not found", null);
    }

    @Override
    public GenericResponseDTO reverseTransitTransaction(String transRef) {

        FundDTO byTransRef = transactionLogService.findByTransRef(transRef + "_1");
        if (byTransRef != null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Reversal failed", null);
        }

        FundDTO originalTransaction = transactionLogService.findByTransRef(transRef);

        if (TransactionStatus.REVERSED.equals(originalTransaction.getStatus())){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Transaction was already reversed", null);
        }

        Double amount = null;
        FundDTO fundDTO = transactionLogService.findByTransRef(transRef);
        List<JournalLine> byJounalReference = journalLineService.findByJounalReference(transRef);
        for (JournalLine journaline : byJounalReference) {
            if (TRANSIT_ACCOUNT.equalsIgnoreCase(journaline.getWalletAccount().getAccountNumber())) {
                amount = journaline.getAmount();
            }
        }

        if ("walletToBank".equalsIgnoreCase(fundDTO.getChannel())) {
            Double intraFee = accountingService.getTransactionFee(fundDTO.getAmount(),
                SpecificChannel.SEND_MONEY.getName(), null);

            amount = amount != null ? amount : fundDTO.getAmount() + intraFee;
            fundDTO.setAmount(amount);

        } else if ("bankToWallet".equalsIgnoreCase(fundDTO.getChannel())) {
            Double bankToWalletCharges = accountingService.getBankToWalletCharges(fundDTO.getAmount(), SpecificChannel.SEND_MONEY.getName());
            amount = amount != null ? amount : fundDTO.getAmount() + bankToWalletCharges;
            fundDTO.setAmount(amount);
        } else if ("walletToWallet".equalsIgnoreCase(fundDTO.getChannel())) {
            amount = amount != null ? amount : fundDTO.getAmount();
            fundDTO.setAmount(amount);
        }

        /*else if ("walletToWallet".equalsIgnoreCase(fundDTO.getChannel())

            && !SpecificChannel.FUND_WALLET_INTER.getName().equalsIgnoreCase(fundDTO.getSpecificChannel())
            && !SpecificChannel.SEND_MONEY_INTER.getName().equalsIgnoreCase(fundDTO.getSpecificChannel())) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null);

        }*/

        String sourceAccountNumber = fundDTO.getSourceAccountNumber();
        if (sourceAccountNumber != null) {

            FundDTO reverseFundDTO = reverseFundDTO(fundDTO);
            reverseFundDTO.setSourceAccountNumber(TRANSIT_ACCOUNT);
            reverseFundDTO.setChannel("WalletToWallet");
            reverseFundDTO.setSpecificChannel(SpecificChannel.FUND_WALLET_INTRA.getName());

            transProducer.send(reverseFundDTO);

            return new GenericResponseDTO("00", HttpStatus.OK, "success", null);

        }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null);


    }

    @Override
    public GenericResponseDTO reverseCharges(String transRef, double chargesAmount) {
        FundDTO byTransRef = transactionLogService.findByTransRef(transRef + "_1");
        if (byTransRef != null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Reversal failed", null);
        }

        FundDTO fundDTO = transactionLogService.findByTransRef(transRef);

        if (fundDTO.getSourceAccountNumber() != null) {

            FundDTO reverseFundDTO = reverseFundDTO(fundDTO);
            reverseFundDTO.setSourceAccountNumber(CHARGE_ACCOUNT);
            reverseFundDTO.setChannel("WalletToWallet");
            reverseFundDTO.setAmount(chargesAmount);
            fundDTO.setNarration(fundDTO.getNarration() + "(charges)");
            reverseFundDTO.setSpecificChannel(SpecificChannel.FUND_WALLET_INTRA.getName());

            transProducer.send(reverseFundDTO);

            return new GenericResponseDTO("00", HttpStatus.OK, "success", null);

        }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null);
    }

    @Override
    public List<Journal> findByExternalRef(String externalRef) {
        return journalRepository.findByExternalRef(externalRef);
    }

    private FundDTO reverseFundDTO(FundDTO fundDTO) {

        out.println("Reversal : Initial fundDTO ==> " + fundDTO);
        String accountNumber = fundDTO.getAccountNumber();
        String sourceAccountNumber = fundDTO.getSourceAccountNumber();
        String sourceBankCode = fundDTO.getSourceBankCode();
        String destBankCode = fundDTO.getDestBankCode();

        fundDTO.setAccountNumber(sourceAccountNumber);
        fundDTO.setSourceAccountNumber(accountNumber);
        fundDTO.setSourceBankCode(destBankCode);
        fundDTO.setDestBankCode(sourceBankCode);
        String narration = fundDTO.getNarration();
        fundDTO.setNarration(narration + "-rvsl");
        if (narration == null || "".equalsIgnoreCase(narration) || "null".equalsIgnoreCase(narration)) {

            fundDTO.setNarration("Transaction-rsvl");
        }
        fundDTO.setTransRef(fundDTO.getTransRef() + "_1");

        out.println("Reversal : reversed fundDTO ==> " + fundDTO);


        return fundDTO;
    }

    @Override
    public List<DayBookDTO> findDayBookDTOs() {
        // TODO Auto-generated method stub
        List<DayBookDTO> dayBookDTOs = new ArrayList<DayBookDTO>();

        List<Journal> journals = journalRepository.findAllByPaymentTypeOrderByTransDateDesc(PaymentType.PAYMENT);
        for (Journal journal : journals) {
            DayBookDTO dayBookDTO = new DayBookDTO();

            dayBookDTO.setJournal(journal);
            dayBookDTO.setJournalLines(journalLineService.findByJounalReferenceNotTransitAccount(journal.getReference().toUpperCase()));

            dayBookDTOs.add(dayBookDTO);
        }
        return dayBookDTOs;
    }

    public DayBookDTO findDaybookDTOByReference(String refrence) {
    	Journal journal = journalRepository.findByReferenceIgnoreCase_(refrence);
    	DayBookDTO dayBookDTO = new DayBookDTO();
    	dayBookDTO.setJournal(journal);
        dayBookDTO.setJournalLines(journalLineService.findByJounalReferenceNotTransitAccount(journal.getReference().toUpperCase()));
        return dayBookDTO;
    }


}
