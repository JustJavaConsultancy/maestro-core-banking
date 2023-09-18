package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.CountrolAccount;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.justjava.corebanking.service.dto.CountrolAccountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CountrolAccount} and its DTO {@link CountrolAccountDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CountrolAccountMapper extends EntityMapper<CountrolAccountDTO, CountrolAccount> {



    default CountrolAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        CountrolAccount countrolAccount = new CountrolAccount();
        countrolAccount.setId(id);
        return countrolAccount;
    }
}
