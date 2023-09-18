package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.BillerCustomFieldOption;
import ng.com.justjava.corebanking.domain.BillerServiceOption;
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
