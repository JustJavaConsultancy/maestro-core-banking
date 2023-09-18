package ng.com.systemspecs.apigateway.service.impl;

import ng.com.systemspecs.apigateway.domain.JournalLine;
import ng.com.systemspecs.apigateway.domain.WalletAccount;
import ng.com.systemspecs.apigateway.repository.ProfileRepository;
import ng.com.systemspecs.apigateway.service.JournalLineService;
import ng.com.systemspecs.apigateway.service.TransactionService;
import ng.com.systemspecs.apigateway.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.dto.EndOfDayDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.SummaryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Value("${app.constants.dfs.biller-payment-account}")
    private String BILLER_PAYMENT_ACCOUNT;
    @Value("${app.constants.dfs.transit-account}")
    private String TRANSIT_ACCOUNT;
    @Value("${app.constants.dfs.correspondence-account}")
    private String CORRESPONDENCE_ACCOUNT;
    @Value("${app.constants.dfs.charge-account}")
    private String CHARGE_ACCOUNT;
    @Value("${app.constants.dfs.remita-income-account}")
    private String REMITA_INCOME_ACCOUNT;

    private final ProfileRepository profileRepository;
    private final JournalLineService journalLineService;
    private final WalletAccountService walletAccountService;

    public TransactionServiceImpl(ProfileRepository profileRepository,
                                  JournalLineService journalLineService,
                                  WalletAccountService walletAccountService) {

        this.profileRepository = profileRepository;
        this.journalLineService = journalLineService;
        this.walletAccountService = walletAccountService;
    }

    @Override
    public GenericResponseDTO getSummary() {
        SummaryDTO summaryDTO = new SummaryDTO();
        summaryDTO.setCustomerCount(profileRepository.count());
        summaryDTO.setAgentTransactionCount(0L);
        summaryDTO.setTransactionCount(journalLineService.count());

        double totalDeposits;
        double totalWithdrawals;
        String profit;

        List<JournalLine> journalLines = journalLineService.findByWalletAccount_AccountNumber("1000000000");
        totalDeposits = journalLines.stream().mapToDouble(JournalLine::getDebit).sum();
        totalWithdrawals = journalLines.stream().mapToDouble(JournalLine::getCredit).sum();

        WalletAccount cashInAccount = walletAccountService.findOneByAccountNumber("1000000001");
        profit = cashInAccount.getCurrentBalance();


        summaryDTO.setTotalDeposits(new BigDecimal(totalDeposits));
        summaryDTO.setTotalWithdrawals(new BigDecimal(totalWithdrawals));
        summaryDTO.setWalletCount(walletAccountService.count());
        summaryDTO.setProfit(new BigDecimal(profit));

        GenericResponseDTO genericResponseDTO = new GenericResponseDTO();
        genericResponseDTO.setCode("00");
        genericResponseDTO.setMessage("success");
        genericResponseDTO.setData(summaryDTO);
        return genericResponseDTO;
    }

    @Override
    public GenericResponseDTO getMetrics() {
        return getSummary();
    }

    @Override
    public GenericResponseDTO getEndOfDay(LocalDate fromDate, LocalDate toDate) {
        EndOfDayDTO endOfDayDTO = new EndOfDayDTO();

        double totalCreditToBank;
        double totalDebitToBank;

        List<JournalLine> journalLines = journalLineService.findByWalletAccount_AccountNumberWithDateRange(CORRESPONDENCE_ACCOUNT, fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX));
        totalCreditToBank = journalLines.stream().mapToDouble(JournalLine::getDebit).sum();
        totalDebitToBank = journalLines.stream().mapToDouble(JournalLine::getCredit).sum();

        List<JournalLine> journalLinesTransit = journalLineService.findByWalletAccount_AccountNumberWithDateRange(TRANSIT_ACCOUNT, fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX));
        double totalCreditToTransit = journalLinesTransit.stream().mapToDouble(JournalLine::getCredit).sum();
        double totalDebitToTransit = journalLinesTransit.stream().mapToDouble(JournalLine::getDebit).sum();

        WalletAccount cashInAccount = walletAccountService.findOneByAccountNumber(CHARGE_ACCOUNT);
        double cashInAccountCurrentBalance = Double.parseDouble(cashInAccount.getCurrentBalance());

        WalletAccount remitaIncomeAccount = walletAccountService.findOneByAccountNumber(REMITA_INCOME_ACCOUNT);
        double remitaIncomeAccountBalance = Double.parseDouble(remitaIncomeAccount.getCurrentBalance());

        endOfDayDTO.setCreditToBank(journalLineService.sumOfAllDebitsToAnAccount(CORRESPONDENCE_ACCOUNT, fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX)));
        endOfDayDTO.setDebitToBank(journalLineService.sumOfAllCreditsToAnAccount(CORRESPONDENCE_ACCOUNT, fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX)));
        endOfDayDTO.setCreditToTransit(journalLineService.sumOfAllCreditsToAnAccount(TRANSIT_ACCOUNT, fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX)));
        endOfDayDTO.setDebitToTransit(journalLineService.sumOfAllDebitsToAnAccount(TRANSIT_ACCOUNT, fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX)));
        endOfDayDTO.setWalletChargesBalance(walletAccountService.getActualBalance(CHARGE_ACCOUNT));
        endOfDayDTO.setRemitaChargesBalance(walletAccountService.getActualBalance(REMITA_INCOME_ACCOUNT));
        endOfDayDTO.setCreditToWallets(journalLineService.sumOfAllCreditsToAllCustomerAccounts(fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX)));
        endOfDayDTO.setDebitToWallets(journalLineService.sumOfAllDebitsToAllCustomerAccounts(fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX)));

        return new GenericResponseDTO("00", HttpStatus.OK, "success", endOfDayDTO);
    }
}
