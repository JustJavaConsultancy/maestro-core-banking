package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.UserCards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserCardsRepository extends JpaRepository<UserCards, Long>, JpaSpecificationExecutor<UserCards> {

    @Query("select u from UserCards u where u.owner.id = ?1")
    List<UserCards> findAllByOwner_Id(Long id);

    @Query("select u from UserCards u where u.owner.phoneNumber = ?1")
    List<UserCards> findAllByOwner_PhoneNumber(String phoneNumber);

//    @Query("select u from UserCards u where u.accountNumber = ?1")
//    UserCards findByAccountNumber(String accountNumber);

    @Query("select u from UserCards u where u.scheme = ?1 and u.owner.phoneNumber = ?2")
    List<UserCards> findAllBySchemeAndOwner_PhoneNumber(String scheme, String phoneNumber);

    Optional<UserCards> findOneByAccountNumber(String accountNumber);

    Optional<UserCards> findOneBySchemeAndOwner_PhoneNumber(String scheme, String phoneNumber);

    @Query("select u from UserCards u where u.scheme = ?1")
    List<UserCards> findAllByScheme(String scheme);

    @Query("select u from UserCards u where u.status = ?1")
    List<UserCards> findAllByStatus(String status);

    @Query("select count(u) from UserCards u where u.scheme = ?1")
    int countByScheme(String scheme);

    @Query("select count(u) from UserCards u where u.status = ?1")
    int countByStatus(String status);

    @Query("select count(u) from UserCards u where u.scheme = ?1 and u.status = ?2")
    int countBySchemeAndStatus(String scheme, String status);

    @Query("select u from UserCards u where u.accountNumber = ?1")
    List<UserCards> findAllByAccountNumber(String accountNumber);
}
