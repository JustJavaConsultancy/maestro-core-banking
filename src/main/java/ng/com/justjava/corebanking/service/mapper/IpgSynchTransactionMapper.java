package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.Address;
import ng.com.justjava.corebanking.domain.IpgSynchTransaction;
import ng.com.justjava.corebanking.service.dto.AddressDTO;
import ng.com.justjava.corebanking.service.dto.stp.IPGSynchTransactionDTO;
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
