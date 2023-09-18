package ng.com.justjava.corebanking.service.mapper;

import ng.com.justjava.corebanking.service.dto.InitiateBillerTransaction;
import ng.com.justjava.corebanking.service.dto.InitiateBillerTransactionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillerInitiateTransactionMapper {

    InitiateBillerTransaction toInitiateBillerTransaction(InitiateBillerTransactionDTO initiateBillerTransactionDTO);

    InitiateBillerTransactionDTO toInitiateBillerTransactionDTO(InitiateBillerTransaction initiateBillerTransaction);
}
