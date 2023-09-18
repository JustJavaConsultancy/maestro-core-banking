package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.BillerCustomFieldOption;
import ng.com.systemspecs.apigateway.domain.BillerPlatform;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.BillerPlatform}.
 */
public interface BillerCustomFieldOptionService {

    Optional<BillerCustomFieldOption> findByBillerPlatform(BillerPlatform billerPlatform);

    List<BillerCustomFieldOption> findAllByBillerPlatformId(long billerPlatformId);

    List<BillerCustomFieldOption> findBillerCustomFieldOptionsByBillerPlatform(BillerPlatform billerPlatform);

    BillerCustomFieldOption save(BillerCustomFieldOption billerCustomFieldOption);

    Optional<BillerCustomFieldOption> findById(Long id);

}
