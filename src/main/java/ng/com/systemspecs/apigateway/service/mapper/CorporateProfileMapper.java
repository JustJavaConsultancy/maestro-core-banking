package ng.com.systemspecs.apigateway.service.mapper;

import ng.com.systemspecs.apigateway.domain.CorporateProfile;
import ng.com.systemspecs.apigateway.service.dto.CorporateProfileDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ProfileMapper.class})
public interface CorporateProfileMapper extends EntityMapper<CorporateProfileDto, CorporateProfile>{

    CorporateProfileDto toDto(CorporateProfile corporateProfile);

    CorporateProfile toEntity(CorporateProfileDto corporateProfileDto);
}
