package ng.com.justjava.corebanking.service.dto;

import lombok.Data;
import ng.com.justjava.corebanking.domain.enumeration.TransactionStatus;

@Data
public class TransactionLogDto {

    private TransactionStatus status;

    private String transRef;
}
