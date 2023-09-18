package ng.com.systemspecs.apigateway.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import ng.com.systemspecs.apigateway.domain.PaymentTransaction;
import ng.com.systemspecs.apigateway.domain.enumeration.TransactionStatus;
import ng.com.systemspecs.apigateway.service.TransactionLogService;
import ng.com.systemspecs.apigateway.service.dto.TransactionLogDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TransactionLogServiceImplTest {
    private static final String transRef = "7890912345";

    @BeforeEach
    void setUp() {

    }

    @Autowired
    TransactionLogService transactionLogService;

    @Test
    void updateTransactionLogStatus() {
        TransactionLogDto logDto = new TransactionLogDto();
        logDto.setStatus(TransactionStatus.COMPLETED);
        logDto.setTransRef(transRef);

    }
}
