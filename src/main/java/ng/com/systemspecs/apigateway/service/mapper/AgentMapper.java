package ng.com.systemspecs.apigateway.service.mapper;


import ng.com.systemspecs.apigateway.domain.Agent;
import ng.com.systemspecs.apigateway.service.dto.AgentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Agent} and its DTO {@link AgentDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProfileMapper.class, WalletAccountMapper.class, UserMapper.class,
    SchemeMapper.class})
public interface AgentMapper extends EntityMapper<AgentDTO, Agent> {

    @Mapping(source = "profile.fullName", target = "fullName")
    @Mapping(source = "profile.phoneNumber", target = "phoneNumber")
    @Mapping(source = "profile.user.email", target = "email")

    @Mapping(source = "profile.user.firstName", target = "firstName")
    @Mapping(source = "profile.user.lastName", target = "lastName")
    @Mapping(source = "profile.createdDate", target = "createdDate")
    @Mapping(source = "profile.walletAccounts", target = "walletAccounts")

    @Mapping(source = "superAgent.profile.phoneNumber", target = "superAgentPhoneNumber")
    @Mapping(source = "superAgent.profile.fullName", target = "superAgentFullName")
    @Mapping(source = "superAgent.profile.user.email", target = "superAgentEmail")
    AgentDTO toDto(Agent Agent);

    Agent toEntity(AgentDTO agentDTO);

    default Agent fromId(Long id) {
        if (id == null) {
            return null;
        }
        Agent Agent = new Agent();
        Agent.setId(id);
        return Agent;
    }
}
