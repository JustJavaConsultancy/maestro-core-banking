package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.PolarisCardProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PolarisCardProfileRepository extends JpaRepository<PolarisCardProfile, Long> {


    Optional<PolarisCardProfile> findOneByScheme(String scheme);


    List<PolarisCardProfile> findAllByScheme(String scheme);

    Optional<PolarisCardProfile> findOneBySchemeAndCardType(String scheme, String cardType);

    @Query("select p from PolarisCardProfile p where p.scheme = ?1 and p.cardType = ?2 and p.cardName = ?3")
    Optional<PolarisCardProfile> findOneBySchemeAndCardTypeAndCardName(String scheme, String cardType, String cardName);
}
