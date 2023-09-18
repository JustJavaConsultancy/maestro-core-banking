package ng.com.systemspecs.apigateway.service.dto;

import lombok.Data;
import ng.com.systemspecs.apigateway.domain.enumeration.TransactionStatus;

@Data
public class TransactionLogDto {

    private TransactionStatus status;

    private String transRef;
}
