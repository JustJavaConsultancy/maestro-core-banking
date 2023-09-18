package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.BillerPlatform;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.justjava.corebanking.service.dto.BillerPlatformDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BillerPlatform} and its DTO {@link BillerPlatformDTO}.
 */
@Mapper(componentModel = "spring", uses = {BillerMapper.class})
public interface BillerPlatformMapper extends EntityMapper<BillerPlatformDTO, BillerPlatform> {

    @Mapping(source = "biller.id", target = "billerId")
    BillerPlatformDTO toDto(BillerPlatform billerPlatform);

    @Mapping(source = "billerId", target = "biller")
    BillerPlatform toEntity(BillerPlatformDTO billerPlatformDTO);

    default BillerPlatform fromId(Long id) {
        if (id == null) {
            return null;
        }
        BillerPlatform billerPlatform = new BillerPlatform();
        billerPlatform.setId(id);
        return billerPlatform;
    }
}
