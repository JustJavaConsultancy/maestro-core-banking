package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.Kyclevel;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Kyclevel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KyclevelRepository extends JpaRepository<Kyclevel, Long> {
	Kyclevel findByKycLevel(Integer kycLevel);

    @Query("select kyclevel from Kyclevel kyclevel where kyclevel.kyc = ?1")
    Kyclevel findByKyc(String kyc);
}
