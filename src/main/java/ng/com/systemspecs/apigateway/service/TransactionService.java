package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;

import java.time.LocalDate;

public interface TransactionService {

    GenericResponseDTO getSummary();

    GenericResponseDTO getMetrics();

    GenericResponseDTO getEndOfDay(LocalDate fromDate, LocalDate toDate);
}
