package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.Beneficiary;
import ng.com.justjava.corebanking.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    List<Beneficiary> findAllByAccountOwner_PhoneNumber(String phoneNumber);

    Optional<Beneficiary> findByAccountOwnerAndAccountNumberAndBankCode(Profile accountOwner, String accountNumber, String bankCode);

    Optional<Beneficiary> findByAccountOwnerAndAccountNumber(Profile accountOwner, String accountNumber);

}
