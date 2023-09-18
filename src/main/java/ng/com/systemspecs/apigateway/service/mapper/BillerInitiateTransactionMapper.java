package ng.com.systemspecs.apigateway.service.mapper;

import ng.com.systemspecs.apigateway.service.dto.InitiateBillerTransaction;
import ng.com.systemspecs.apigateway.service.dto.InitiateBillerTransactionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillerInitiateTransactionMapper {

    InitiateBillerTransaction toInitiateBillerTransaction(InitiateBillerTransactionDTO initiateBillerTransactionDTO);

    InitiateBillerTransactionDTO toInitiateBillerTransactionDTO(InitiateBillerTransaction initiateBillerTransaction);
}
