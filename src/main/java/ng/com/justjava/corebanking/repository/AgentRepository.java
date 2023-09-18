package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.Agent;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.service.dto.SuperAgentDebitCreditDTO;
import ng.com.justjava.corebanking.service.dto.SuperAgentMetricsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface
AgentRepository extends JpaRepository<Agent, Long> {

    Boolean existsByProfilePhoneNumber(String phoneNumber);

    List<Agent> findAllBySuperAgent_Id(Long superAgentId);

    Optional<Agent> findByProfilePhoneNumber(String agentPhoneNumber);

    List<Agent> findByLocationLike(String location);

    List<Agent> findAllBySuperAgentProfilePhoneNumber(String phoneNumber);

    Optional<Agent> findByProfile(Profile profile);

    @Query(value = "SELECT jhi_user.login as phone, jhi_user.first_name, wallet_account_id,jhi_user.last_name, COUNT(wallet_account_id) as no_of_transactions  from jounal_line JOIN wallet_account on wallet_account_id = wallet_account.id JOIN profile on wallet_account.account_owner_id = profile.id JOIN jhi_user on profile.user_id = jhi_user.id WHERE wallet_account_id in (SELECT id from wallet_account WHERE account_owner_id in (SELECT profile_id from agent WHERE super_agent_id in (SELECT id FROM agent WHERE profile_id = ?1)))  GROUP BY wallet_account_id, jhi_user.login, jhi_user.first_name, jhi_user.last_name ORDER by no_of_transactions DESC",
    		nativeQuery = true)
    List<SuperAgentMetricsDTO> findAllSuperAgentsBestAgentByHighestTransaction(Long profileId);

    @Query(value = "SELECT COALESCE(sum(credit), 0) as total_credit from jounal_line WHERE credit > 0 and wallet_account_id in (SELECT id from wallet_account WHERE account_owner_id in (SELECT profile_id from agent WHERE super_agent_id in (SELECT id FROM agent WHERE profile_id = ?1)))",
    		nativeQuery = true)
    SuperAgentDebitCreditDTO findSuperAgentAgentsToTalDeposits(Long profileId);

    @Query(value = "SELECT COALESCE(sum(debit),0) as total_debit from jounal_line WHERE debit > 0 and wallet_account_id in (SELECT id from wallet_account WHERE account_owner_id in (SELECT profile_id from agent WHERE super_agent_id in (SELECT id FROM agent WHERE profile_id = ?1)))",
    		nativeQuery = true)
    SuperAgentDebitCreditDTO findSuperAgentAgentsToTalWithdrawals(Long profileId);

    @Query(value = "SELECT COUNT(wallet_account_id) as transaction_count from jounal_line WHERE wallet_account_id in (SELECT id from wallet_account WHERE account_owner_id in (SELECT profile_id from agent WHERE super_agent_id in (SELECT id FROM agent WHERE profile_id = ?1)))",
    		nativeQuery = true)
    SuperAgentDebitCreditDTO findSuperAgentAgentsToTalTransactions(Long profileId);
}
