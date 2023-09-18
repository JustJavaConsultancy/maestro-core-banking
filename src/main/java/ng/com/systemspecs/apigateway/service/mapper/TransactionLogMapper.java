package ng.com.systemspecs.apigateway.service.mapper;


import ng.com.systemspecs.apigateway.domain.TransactionLog;
import ng.com.systemspecs.apigateway.service.dto.FundDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link ng.com.systemspecs.apigateway.domain.TransactionLog} and its DTO {@link FundDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TransactionLogMapper extends EntityMapper<FundDTO, TransactionLog> {

    default TransactionLog fromId(Long id) {
        if (id == null) {
            return null;
        }
        TransactionLog transactionLog = new TransactionLog();
        transactionLog.setId(id);
        return transactionLog;
    }
}
