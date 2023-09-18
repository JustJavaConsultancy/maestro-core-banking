package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.Biller;
import ng.com.justjava.corebanking.domain.BillerServiceOption;
import ng.com.justjava.corebanking.service.dto.BillerDTO;
import ng.com.justjava.corebanking.service.dto.BillerServiceOptionDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Biller} and its DTO {@link BillerDTO}.
 */
@Mapper(componentModel = "spring", uses = {BillerPlatformMapper.class})
public interface BillerServiceOptionMapper extends EntityMapper<BillerServiceOptionDTO, BillerServiceOption> {

    default BillerServiceOption fromId(Long id) {
        if (id == null) {
            return null;
        }
        BillerServiceOption billerServiceOption = new BillerServiceOption();
        billerServiceOption.setId(id);
        return billerServiceOption;
    }
}
