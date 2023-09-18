package ng.com.systemspecs.apigateway.service.mapper;

import ng.com.systemspecs.apigateway.domain.UserCards;
import ng.com.systemspecs.apigateway.service.dto.UserCardsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface UserCardsMapper extends EntityMapper<UserCardsDTO, UserCards> {

    UserCardsDTO toDto(UserCards userCards);

    UserCards toEntity(UserCardsDTO userCardsDTO);
}
