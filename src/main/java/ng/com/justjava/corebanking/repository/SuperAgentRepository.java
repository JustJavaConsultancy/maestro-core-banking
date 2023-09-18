package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.Agent;
import ng.com.justjava.corebanking.domain.SuperAgent;
import ng.com.justjava.corebanking.domain.enumeration.AgentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the SuperAgent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SuperAgentRepository extends JpaRepository<SuperAgent, Long> {
    boolean existsByAgentProfilePhoneNumber(String phoneNumber);

    Optional<SuperAgent> findByAgentProfilePhoneNumber(String phoneNumber);

    Optional<SuperAgent> findByAgentProfilePhoneNumberAndStatus(String phoneNumber, AgentStatus status);

    List<SuperAgent> findAllByStatus(@NotNull AgentStatus status);

    Optional<SuperAgent> findByAgent(Agent agent);


}
