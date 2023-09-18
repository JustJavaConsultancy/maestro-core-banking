package ng.com.justjava.corebanking.service.mapper;

import ng.com.justjava.corebanking.domain.CardRequest;
import ng.com.justjava.corebanking.service.dto.CardRequestDTO;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ProfileMapper.class})
public interface CardRequestMapper extends EntityMapper<CardRequestDTO, CardRequest>{

    CardRequestDTO toDto(CardRequest cardRequest);

}
