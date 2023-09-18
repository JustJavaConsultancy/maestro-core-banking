package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.BillerCustomFieldOption;
import ng.com.systemspecs.apigateway.domain.BillerServiceOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillerServiceOptionRepository extends JpaRepository<BillerServiceOption, Long> {

    Optional<BillerServiceOption> findByBillerCustomFieldOption(BillerCustomFieldOption billerCustomFieldOption);

    List<BillerServiceOption> findAllByBillerCustomFieldOptionId(long billerCustomeFieldOptionId);

    List<BillerServiceOption> findBillerServiceOptionByBillerCustomFieldOption(BillerCustomFieldOption billerCustomFieldOption);

    List<BillerServiceOption> findByServiceOptionId(Long serviceOptionId);

}
