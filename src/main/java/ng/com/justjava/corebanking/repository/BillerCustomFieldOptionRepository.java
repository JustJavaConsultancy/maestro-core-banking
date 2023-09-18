package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.BillerCustomFieldOption;
import ng.com.justjava.corebanking.domain.BillerPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillerCustomFieldOptionRepository extends JpaRepository<BillerCustomFieldOption, Long> {

    Optional<BillerCustomFieldOption> findByBillerPlatform(BillerPlatform billerPlatform);

    List<BillerCustomFieldOption> findAllByBillerPlatformId(long billerPlatformId);

    List<BillerCustomFieldOption> findBillerCustomFieldOptionsByBillerPlatform(BillerPlatform billerPlatform);

}
