package ng.com.systemspecs.apigateway.service.mapper;


import ng.com.systemspecs.apigateway.domain.Biller;
import ng.com.systemspecs.apigateway.domain.BillerServiceOption;
import ng.com.systemspecs.apigateway.service.dto.BillerDTO;
import ng.com.systemspecs.apigateway.service.dto.BillerServiceOptionDTO;
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
