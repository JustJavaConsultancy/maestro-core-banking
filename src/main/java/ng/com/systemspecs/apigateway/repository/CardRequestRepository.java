package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.CardRequest;
import ng.com.systemspecs.apigateway.domain.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface CardRequestRepository extends JpaRepository<CardRequest, Long> {

    @Query("select c from CardRequest c where upper(c.cardtype) = upper(?1)")
    List<CardRequest> findByCardtypeEqualsIgnoreCase(String Cardtype);

    List<CardRequest> findAllByScheme(String scheme);

    List<CardRequest> findAllByStatusEqualsIgnoreCase(String status);

    List<CardRequest> findAllBySchemeAndCardtype(String scheme, String cardtype);

//    Page<CardRequest> findAllWithKeyword(Pageable pageable, @Param("keyword") String keyword);

    List<CardRequest> findAllBySchemeAndCardtypeAndCreatedDateBetween(String scheme, String cardtype, Instant startDate, Instant endDate);

    List<CardRequest> findAllBySchemeAndCreatedDateBetween(String scheme, Instant startDate, Instant endDate);

    List<CardRequest> findAllByCardtypeAndCreatedDateBetween(String cardtype, Instant startDate, Instant endDate);

    @Query("select c from CardRequest c where c.createdDate between ?1 and ?2")
    List<CardRequest> findAllByCreatedDateBetween(Instant startDate, Instant endDate);

    @Query("select c from CardRequest c where c.owner = ?1 and c.scheme = ?2 and c.status = ?3")
    CardRequest findOneByOwnerAndSchemeAndStatus(Profile owner, String scheme, String status);
}
