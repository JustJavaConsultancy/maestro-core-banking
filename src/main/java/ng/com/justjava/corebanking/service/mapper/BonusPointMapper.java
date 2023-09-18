package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.BonusPoint;
import ng.com.justjava.corebanking.service.dto.BonusPointDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link BonusPoint} and its DTO {@link BonusPointDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class})
public interface BonusPointMapper extends EntityMapper<BonusPointDTO, BonusPoint> {

    @Mapping(source = "profile.phoneNumber", target = "profilePhoneNumber")
    @Mapping(source = "profile.id", target = "profileId")
    BonusPointDTO toDto(BonusPoint bonusPoint);

    @Mapping(source = "profileId", target = "profile")
    BonusPoint toEntity(BonusPointDTO bonusPointDTO);

    default BonusPoint fromId(Long id) {
        if (id == null) {
            return null;
        }
        BonusPoint bonusPoint = new BonusPoint();
        bonusPoint.setId(id);
        return bonusPoint;
    }
}
