package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.User;
import ng.com.systemspecs.apigateway.domain.WalletAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

/**
 * Spring Data  repository for the Profile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("select profile from Profile profile where profile.user.login = ?#{principal.username}")
    Profile findByUserIsCurrentUser();

    Profile findOneByPhoneNumber(String phoneNumber);

    Page<Profile> findAllByCreatedDateBetween(Pageable pageable, Instant startDate, Instant endDate);

    Page<Profile> findAllByPhoneNumberContainingIgnoreCaseOrUserEmailContainingIgnoreCaseOrUser_FirstNameContainingIgnoreCaseOrUser_LastNameContainingIgnoreCase(String phoneNumber, String email, String firstName, @Size(max = 50) String lastName, Pageable pageable);

    @Query("select profile from Profile profile where concat(profile.phoneNumber, '') LIKE concat('%',:keyword, '%') or  lower(concat(profile.user.email, '')) LIKE lower(concat('%',:keyword, '%')) or  lower(concat(profile.user.firstName, '')) LIKE lower(concat('%',:keyword, '%')) or  lower(profile.user.lastName) LIKE lower(concat('%',:keyword, '%')) order by profile.createdDate")
    Page<Profile> searchProfileByKeyword(Pageable pageable, @Param("keyword") String keyword);

    List<Profile> findAllByUserIn(List<User> user);

    List<Profile> findByDeviceNotificationToken(String deviceNotificationToken);


    @Query("select profile from Profile profile where profile.walletAccounts =:walletAccounts and concat(profile.phoneNumber, '') LIKE concat('%',:keyword, '%') or  lower(concat(profile.user.email, '')) LIKE lower(concat('%',:keyword, '%')) or  lower(concat(profile.user.firstName, '')) LIKE lower(concat('%',:keyword, '%')) or  lower(profile.user.lastName) LIKE lower(concat('%',:keyword, '%')) order by profile.createdDate")
    Page<Profile> findAllByWalletAccounts(@Param("walletAccounts") List<WalletAccount> walletAccounts, @Param("keyword") String keyword, Pageable pageable);

}
