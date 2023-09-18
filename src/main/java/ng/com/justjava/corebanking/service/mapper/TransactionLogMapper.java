package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.TransactionLog;
import ng.com.justjava.corebanking.service.dto.FundDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link TransactionLog} and its DTO {@link FundDTO}.
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
