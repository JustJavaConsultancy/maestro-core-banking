package ng.com.systemspecs.apigateway.service.mapper;

import ng.com.systemspecs.apigateway.domain.CardRequest;
import ng.com.systemspecs.apigateway.service.dto.CardRequestDTO;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ProfileMapper.class})
public interface CardRequestMapper extends EntityMapper<CardRequestDTO, CardRequest>{

    CardRequestDTO toDto(CardRequest cardRequest);

}
