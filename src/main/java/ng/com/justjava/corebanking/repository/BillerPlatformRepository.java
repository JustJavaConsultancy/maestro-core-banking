package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.Biller;
import ng.com.justjava.corebanking.domain.BillerPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the BillerPlatform entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillerPlatformRepository extends JpaRepository<BillerPlatform, Long> {

    List<BillerPlatform> findAllByBiller(Biller biller);

    List<BillerPlatform> findByBillerBillerID(String billerId);

    List<BillerPlatform> findBillerPlatformsByBiller(Biller biller);

    Optional<BillerPlatform> findByBillerplatformID(Long billerPlatformID);

}
