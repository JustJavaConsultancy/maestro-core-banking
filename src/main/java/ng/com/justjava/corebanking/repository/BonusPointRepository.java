package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.BonusPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the BonusPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BonusPointRepository extends JpaRepository<BonusPoint, Long> {

    List<BonusPoint> findAllByProfile_PhoneNumber(String phoneNumber);
}
