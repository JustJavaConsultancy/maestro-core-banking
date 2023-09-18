package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.BillerCustomFieldOption;
import ng.com.justjava.corebanking.domain.BillerPlatform;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link BillerPlatform}.
 */
public interface BillerCustomFieldOptionService {

    Optional<BillerCustomFieldOption> findByBillerPlatform(BillerPlatform billerPlatform);

    List<BillerCustomFieldOption> findAllByBillerPlatformId(long billerPlatformId);

    List<BillerCustomFieldOption> findBillerCustomFieldOptionsByBillerPlatform(BillerPlatform billerPlatform);

    BillerCustomFieldOption save(BillerCustomFieldOption billerCustomFieldOption);

    Optional<BillerCustomFieldOption> findById(Long id);

}
