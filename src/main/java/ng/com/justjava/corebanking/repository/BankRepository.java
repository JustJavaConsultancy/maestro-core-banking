package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.Bank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    Optional<Bank> findByBankCode(String bankCode);

    @Override
    Page<Bank> findAll(Pageable pageable);

    Page<Bank> findAllByOrderByBankName(Pageable pageable);


    Page<Bank> findAllByTypeAndBankCodeIsNotOrderByBankName(String type, String bankCode, Pageable pageable);

    List<Bank> findAllByTypeAndBankCodeIsNotOrderByBankName(String type, String bankCode);

    Page<Bank> findAllByBankCodeIsNotOrderByBankName(String bankCode, Pageable pageable);

    List<Bank> findAllByBankCodeIsNotOrderByBankName(String bankCode);

    Page<Bank> findAllByShortCodeIsNotNullOrderByBankName(Pageable pageable);

}
