package ng.com.justjava.corebanking.service.mapper;

import ng.com.justjava.corebanking.domain.UserCards;
import ng.com.justjava.corebanking.service.dto.UserCardsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface UserCardsMapper extends EntityMapper<UserCardsDTO, UserCards> {

    UserCardsDTO toDto(UserCards userCards);

    UserCards toEntity(UserCardsDTO userCardsDTO);
}
