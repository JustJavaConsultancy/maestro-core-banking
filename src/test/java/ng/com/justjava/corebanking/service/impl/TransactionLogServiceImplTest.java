package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.domain.enumeration.TransactionStatus;
import ng.com.justjava.corebanking.service.TransactionLogService;
import ng.com.justjava.corebanking.service.dto.TransactionLogDto;
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
