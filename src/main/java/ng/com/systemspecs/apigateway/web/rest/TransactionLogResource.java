package ng.com.systemspecs.apigateway.web.rest;

import io.github.jhipster.web.util.PaginationUtil;
import ng.com.systemspecs.apigateway.service.TransactionLogService;
import ng.com.systemspecs.apigateway.service.dto.FundDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.TransactionLogDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionLogResource {

    private final TransactionLogService transactionLogService;

    public TransactionLogResource(TransactionLogService transactionLogService) {
        this.transactionLogService = transactionLogService;
    }


    @GetMapping("/transaction/logs/{fromDate}/{toDate}")
    public ResponseEntity<GenericResponseDTO> getAllTransactionLogsWithDateRange(@PathVariable LocalDate fromDate, @PathVariable LocalDate toDate, Pageable pageable) {

        Instant fromInstant = fromDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant end = toDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant toInstant = end.atOffset(ZoneOffset.UTC)
            .plusDays(1).with(LocalTime.of(23,59,59,end.getNano()))
            .toInstant();

        Page<FundDTO> fundDTOS = transactionLogService.findAllByCreatedDateBetween(fromInstant, toInstant, pageable);
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), fundDTOS);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", fundDTOS.getSize());
        metaMap.put("totalNumberOfRecords", fundDTOS.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", fundDTOS.getContent(), metaMap), headers, HttpStatus.OK);

    }

    @GetMapping("/transaction/get-logs/{fromDate}/{toDate}")
    public ResponseEntity<GenericResponseDTO> getTransactionLogsWithDateRange(@PathVariable LocalDate fromDate, @PathVariable LocalDate toDate, Pageable pageable) {

        Instant fromInstant = fromDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant toInstant = toDate.atStartOfDay().toInstant(ZoneOffset.UTC).atOffset(ZoneOffset.UTC).with(LocalTime.of(23,59,59,999999999))
            .toInstant();

        List<FundDTO> fundDTOS = transactionLogService.findAllByCreatedDateBetween(fromInstant, toInstant);
        return new ResponseEntity<>(new GenericResponseDTO("00","Success", fundDTOS),HttpStatus.OK);

    }

    @GetMapping("/transaction/logs/{scheme}/{fromDate}/{toDate}")
    public ResponseEntity<GenericResponseDTO> getSchemeTransactionLogsWithDateRange(@PathVariable String scheme, @PathVariable LocalDate fromDate, @PathVariable LocalDate toDate, Pageable pageable) {

        Instant fromInstant = fromDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant toInstant = toDate.atStartOfDay().toInstant(ZoneOffset.UTC).atOffset(ZoneOffset.UTC)
            .with(LocalTime.of(23,59,59,999999999))
            .toInstant();

        Page<FundDTO> fundDTOS = transactionLogService.findAllBySchemeAndCreatedDateBetween(scheme,fromInstant, toInstant, pageable);
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), fundDTOS);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", fundDTOS.getSize());
        metaMap.put("totalNumberOfRecords", fundDTOS.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", fundDTOS.getContent(), metaMap), headers, HttpStatus.OK);

    }

    @GetMapping("/transaction/logs")
    public ResponseEntity<GenericResponseDTO> getAllTransactionLogs(Pageable pageable) {

        Page<FundDTO> fundDTOS = transactionLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), fundDTOS);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", fundDTOS.getSize());
        metaMap.put("totalNumberOfRecords", fundDTOS.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", fundDTOS.getContent(), metaMap), headers, HttpStatus.OK);

    }

    @GetMapping("/transaction/logs/search")
    public ResponseEntity<GenericResponseDTO> getAllTransactionLogsByKeyword(Pageable pageable, @RequestParam String key) {
        Page<FundDTO> fundDTOS = transactionLogService.findAllByKeyword(pageable, key);
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), fundDTOS);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", fundDTOS.getSize());
        metaMap.put("totalNumberOfRecords", fundDTOS.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", fundDTOS.getContent(), metaMap), headers, HttpStatus.OK);


    }

    @GetMapping("/transaction/logs/{transRef}")
    public ResponseEntity<GenericResponseDTO> getTransactionLogByTransRef(@PathVariable String transRef) {
        FundDTO fundDTO = transactionLogService.findByTransRef(transRef);
        if (fundDTO != null) {
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", fundDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new GenericResponseDTO("99", "Transaction not found!", null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/transaction/logs/account/{accountNumber}")
    public ResponseEntity<GenericResponseDTO> getTransactionLogByAccountNumber(@PathVariable String accountNumber) {
        List<FundDTO> fundDTO = transactionLogService.findByAccountNumber(accountNumber);
        if (fundDTO != null) {
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", fundDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new GenericResponseDTO("99", "Transactions not found!", null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/transaction/logs/account/{accountNumber}/{fromDate}/{toDate}")
    public ResponseEntity<GenericResponseDTO> getTransactionLogByAccountNumber(@PathVariable String accountNumber, @PathVariable LocalDate fromDate, @PathVariable LocalDate toDate) {
        Instant fromInstant = fromDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant toInstant = toDate.atStartOfDay().toInstant(ZoneOffset.UTC).atOffset(ZoneOffset.UTC)
            .plusDays(1).with(LocalTime.of(23,59,59,999999999))
            .toInstant();

        List<FundDTO> fundDTO = transactionLogService.findByAccountNumberAndCreatedDateBetween(accountNumber, fromInstant, toInstant);
        if (fundDTO != null) {
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", fundDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new GenericResponseDTO("99", "Transactions not found!", null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/transaction/current-logs/account/{accountNumber}")
    public ResponseEntity<GenericResponseDTO> getTransactionCurrentDateLogByAccountNumber(@PathVariable String accountNumber) {

        Instant fromInstant = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant toInstant = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).atOffset(ZoneOffset.UTC)
            .plusDays(1).with(LocalTime.of(23,59,59,999999999))
            .toInstant();
        List<FundDTO> fundDTO = transactionLogService.findBySourceAccountNumberAndCreatedDateBetween(accountNumber, fromInstant, toInstant)
            .stream().filter(t -> t.getStatus().getName().equalsIgnoreCase("COMPLETED")).collect(Collectors.toList());
        if (fundDTO != null) {
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", fundDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new GenericResponseDTO("99", "Transactions not found!", null), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/transaction/logs/rrr/{rrr}")
    public ResponseEntity<GenericResponseDTO> getTransactionLogByRRR(@PathVariable String rrr) {
        FundDTO fundDTO = transactionLogService.findByRrr(rrr);
        if (fundDTO != null) {
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", fundDTO), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new GenericResponseDTO("99", "Transaction not found!", null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/transaction/logs/id/{id}")
    public ResponseEntity<GenericResponseDTO> getTransactionLog(@PathVariable Long id) {
        Optional<FundDTO> one = transactionLogService.findOne(id);
        return one.map(fundDTO ->
            new ResponseEntity<>(new GenericResponseDTO("00", "success", fundDTO), HttpStatus.OK))
            .orElseGet(() ->
                new ResponseEntity<>(new GenericResponseDTO("99", "Transaction not found", null), HttpStatus.BAD_REQUEST));

    }

    @PutMapping("/transaction/logs")
    public ResponseEntity<?> updateTransactionLogStatus(@RequestBody TransactionLogDto logDto){
        Optional<FundDTO> optionalFundDTO = transactionLogService.updateTransactionLogStatus(logDto);
        return optionalFundDTO.map(fundDTO ->
            new ResponseEntity<>(new GenericResponseDTO("00", "success", fundDTO), HttpStatus.OK))
            .orElseGet(() ->
                new ResponseEntity<>(new GenericResponseDTO("99", "Transaction not found", null), HttpStatus.BAD_REQUEST));
    }
}
