package ng.com.systemspecs.apigateway.service.mapper;


import ng.com.systemspecs.apigateway.domain.Address;
import ng.com.systemspecs.apigateway.domain.IpgSynchTransaction;
import ng.com.systemspecs.apigateway.service.dto.AddressDTO;
import ng.com.systemspecs.apigateway.service.dto.stp.IPGSynchTransactionDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IpgSynchTransactionMapper extends EntityMapper<IPGSynchTransactionDTO, IpgSynchTransaction> {

    default IpgSynchTransaction fromId(Long id) {
        if (id == null) {
            return null;
        }
        IpgSynchTransaction ipgSynchTransaction = new IpgSynchTransaction();
        ipgSynchTransaction.setId(id);
        return ipgSynchTransaction;
    }
}
