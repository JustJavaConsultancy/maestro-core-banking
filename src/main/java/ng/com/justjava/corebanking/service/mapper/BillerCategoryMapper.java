package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.BillerCategory;
import ng.com.justjava.corebanking.service.dto.BillerCategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link BillerCategory} and its DTO {@link BillerCategoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BillerCategoryMapper extends EntityMapper<BillerCategoryDTO, BillerCategory> {


    @Mapping(target = "billers", ignore = true)
    @Mapping(target = "removeBiller", ignore = true)
    BillerCategory toEntity(BillerCategoryDTO billerCategoryDTO);

    default BillerCategory fromId(Long id) {
        if (id == null) {
            return null;
        }
        BillerCategory billerCategory = new BillerCategory();
        billerCategory.setId(id);
        return billerCategory;
    }
}
