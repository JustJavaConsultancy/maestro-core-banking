

package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.Loan;
import ng.com.justjava.corebanking.service.dto.LoanDTO;
import ng.com.justjava.corebanking.service.dto.LoansCreatedDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Loan} and its DTO {@link LoansCreatedDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class, LenderMapper.class})
public interface LoansCreatedMapper extends EntityMapper<LoanDTO, Loan> {

    @Mapping(source = "profile.id", target = "profileId")
    @Mapping(source = "lender.id", target = "lenderId")
    LoanDTO toDto(Loan loan);

    @Mapping(source = "profileId", target = "profile")
    @Mapping(source = "lenderId", target = "lender")
    Loan toEntity(LoanDTO loanDTO);

    default Loan fromId(Long id) {
        if (id == null) {
            return null;
        }
        Loan loan = new Loan();
        loan.setId(id);
        return loan;
    }
}

