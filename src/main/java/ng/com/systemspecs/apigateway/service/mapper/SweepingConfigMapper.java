package ng.com.systemspecs.apigateway.service.mapper;


import ng.com.systemspecs.apigateway.domain.SweepingConfig;
import ng.com.systemspecs.apigateway.service.dto.SweepingConfigDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link SweepingConfig} and its DTO {@link SweepingConfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SweepingConfigMapper extends EntityMapper<SweepingConfigDTO, SweepingConfig> {


    default SweepingConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        SweepingConfig sweepingConfig = new SweepingConfig();
        sweepingConfig.setId(id);
        return sweepingConfig;
    }
}
