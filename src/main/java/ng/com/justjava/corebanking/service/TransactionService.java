package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;

import java.time.LocalDate;

public interface TransactionService {

    GenericResponseDTO getSummary();

    GenericResponseDTO getMetrics();

    GenericResponseDTO getEndOfDay(LocalDate fromDate, LocalDate toDate);
}
