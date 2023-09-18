package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.CorporateProfile;
import ng.com.justjava.corebanking.domain.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface CorporateProfileRepository extends JpaRepository<CorporateProfile, Long> {

    @Query("select c from CorporateProfile c where c.phoneNo = ?1")
    CorporateProfile findOneByPhoneNo(String phoneNo);

    @Query("select c from CorporateProfile c where c.profile.phoneNumber = ?1")
    CorporateProfile findOneByProfile_PhoneNumber(String phoneNumber);

    @Query("select c from CorporateProfile c where c.profile = ?1")
    CorporateProfile findOneByProfile(Profile profile);

    Page<CorporateProfile> findAllByCreatedDateBetween(Pageable pageable, Instant startDate, Instant endDate);

    Page<CorporateProfile> findAllByPhoneNoContainingIgnoreCaseOrNameIgnoreCase(String phoneNumber, String name, Pageable pageable);

    @Query("select corporateProfile from CorporateProfile corporateProfile where concat(corporateProfile.phoneNo, '') LIKE concat('%',:keyword, '%') or  lower(concat(corporateProfile.name, '')) LIKE lower(concat('%',:keyword, '%')) or  lower(corporateProfile.rcNO, '') LIKE lower(concat('%',:keyword, '%')) order by corporateProfile.createdDate")
    Page<CorporateProfile> searchProfileByKeyword(Pageable pageable, @Param("keyword") String keyword);


    @Query("select (count(c) > 0) from CorporateProfile c where c.phoneNo = ?1")
    boolean existsByPhoneNo(String phoneNumber);

    @Query("select (count(c) > 0) from CorporateProfile c where c.rcNO = ?1")
    boolean existsByRcNO(String rcNo);
}
