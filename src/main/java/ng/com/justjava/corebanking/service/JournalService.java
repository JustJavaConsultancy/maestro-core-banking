package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.DayBookDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.ReverseDTO;
import ng.com.justjava.corebanking.domain.Journal;
import ng.com.justjava.corebanking.domain.enumeration.TransactionStatus;

import java.util.List;
import java.util.Optional;

public interface JournalService {
    Journal save(Journal journal);


    List<Journal> findAll();


    /**
     * Get the "id" address.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Journal> findOne(Long id);

    List<DayBookDTO> findDayBookDTOs();
    DayBookDTO findDaybookDTOByReference(String reference);

    /**
     * Delete the "id" address.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<Journal> findByReference(String reference);

    GenericResponseDTO changeTransactionStatus(String transRef, TransactionStatus status);

    GenericResponseDTO reverseTransaction(ReverseDTO reverseDTO);

    GenericResponseDTO reverseTransitTransaction(String transRef);


    GenericResponseDTO reverseCharges(String transRef, double chargesAmount);

    List<Journal> findByExternalRef(String externalRef);

}
