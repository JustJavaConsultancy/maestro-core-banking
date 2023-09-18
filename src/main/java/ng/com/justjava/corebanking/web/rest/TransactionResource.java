package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.repository.ProfileRepository;
import ng.com.justjava.corebanking.service.JournalLineService;
import ng.com.justjava.corebanking.service.TransactionService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequestMapping("/api")
@RestController
public class TransactionResource {

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
    private final TransactionService transactionService;

    public TransactionResource(ProfileRepository profileRepository, JournalLineService journalLineService, WalletAccountService walletAccountService, TransactionService transactionService) {
        this.profileRepository = profileRepository;
        this.journalLineService = journalLineService;
        this.walletAccountService = walletAccountService;
        this.transactionService = transactionService;
    }

    @GetMapping("/summary")
    public ResponseEntity<GenericResponseDTO> getSummary() {
        GenericResponseDTO genericResponseDTO = transactionService.getSummary();
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/metrics")
    public ResponseEntity<GenericResponseDTO> getMetrics(@RequestParam(required = false, name = "sort") String sort) {
        GenericResponseDTO genericResponseDTO = transactionService.getMetrics();
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/end-of-day/{startDate}/{endDate}")
    public ResponseEntity<GenericResponseDTO> getEndOfDay(@PathVariable LocalDate startDate, @PathVariable LocalDate endDate ) {
        GenericResponseDTO genericResponseDTO = transactionService.getEndOfDay(startDate, endDate);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }
}
