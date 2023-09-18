package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.BillerCustomFieldOption;
import ng.com.systemspecs.apigateway.domain.BillerPlatform;
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
