package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.ProfileType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ProfileType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileTypeRepository extends JpaRepository<ProfileType, Long> {
	ProfileType findByProfiletype(String profiletype);
}
