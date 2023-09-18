package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.Teller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TellerRepository extends JpaRepository<Teller, Long> {

    List<Teller> findAllByAgentProfilePhoneNumber(String agentPhoneNumber);

    List<Teller> findByLocationLike(String location);

    Optional<Teller> findByProfilePhoneNumber(String phoneNumber);

}
