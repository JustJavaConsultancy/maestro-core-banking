package ng.com.justjava.corebanking.service.mapper;

import ng.com.justjava.corebanking.domain.CorporateProfile;
import ng.com.justjava.corebanking.service.dto.CorporateProfileDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ProfileMapper.class})
public interface CorporateProfileMapper extends EntityMapper<CorporateProfileDto, CorporateProfile>{

    CorporateProfileDto toDto(CorporateProfile corporateProfile);

    CorporateProfile toEntity(CorporateProfileDto corporateProfileDto);
}
