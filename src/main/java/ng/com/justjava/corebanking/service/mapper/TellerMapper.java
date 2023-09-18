package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.Teller;
import ng.com.justjava.corebanking.service.dto.TellerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Teller} and its DTO {@link TellerDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class, AgentMapper.class})
public interface TellerMapper extends EntityMapper<TellerDTO, Teller> {

    @Mapping(source = "profile.fullName", target = "fullName")
    @Mapping(source = "profile.phoneNumber", target = "phoneNumber")
    @Mapping(source = "agent.profile.phoneNumber", target = "agentPhoneNumber")
    TellerDTO toDto(Teller teller);

    Teller toEntity(TellerDTO tellerDTO);

    default Teller fromId(Long id) {
        if (id == null) {
            return null;
        }
        Teller Teller = new Teller();
        Teller.setId(id);
        return Teller;
    }
}
