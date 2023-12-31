package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.Biller;
import ng.com.justjava.corebanking.domain.BillerCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Biller entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillerRepository extends JpaRepository<Biller, Long> {


    List<Biller> findAllByBillerCategory(BillerCategory billerCategory);

    List<Biller> findAllByBillerCategoryAndBillerCategory(BillerCategory cat1, BillerCategory cat2);

    Optional<Biller> findByBillerID(String billerId);


    List<Biller> findAllByisPopular(boolean isPopular);
}
