package ng.com.systemspecs.apigateway.service.mapper;


import ng.com.systemspecs.apigateway.domain.SuperAgent;
import ng.com.systemspecs.apigateway.service.dto.SuperAgentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link SuperAgent} and its DTO {@link SuperAgentDTO}.
 */
@Mapper(componentModel = "spring", uses = {AgentMapper.class, SchemeMapper.class, ProfileMapper.class, UserMapper.class,
    WalletAccountMapper.class})
public interface SuperAgentMapper extends EntityMapper<SuperAgentDTO, SuperAgent> {

    @Mapping(source = "scheme.id", target = "schemeId")
    @Mapping(source = "scheme.schemeID", target = "schemeSchemeID")

    @Mapping(source = "agent.id", target = "agentId")
    @Mapping(source = "agent.profile.user.firstName", target = "firstName")
    @Mapping(source = "agent.profile.user.lastName", target = "lastName")
    @Mapping(source = "agent.profile.createdDate", target = "createdDate")
    @Mapping(source = "agent.profile.walletAccounts", target = "walletAccounts")
    SuperAgentDTO toDto(SuperAgent superAgent);

    @Mapping(source = "agentId", target = "agent")
    @Mapping(source = "schemeId", target = "scheme")
    SuperAgent toEntity(SuperAgentDTO superAgentDTO);

    default SuperAgent fromId(Long id) {
        if (id == null) {
            return null;
        }
        SuperAgent superAgent = new SuperAgent();
        superAgent.setId(id);
        return superAgent;
    }
}
