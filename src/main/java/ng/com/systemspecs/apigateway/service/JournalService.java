package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.Journal;
import ng.com.systemspecs.apigateway.domain.enumeration.TransactionStatus;
import ng.com.systemspecs.apigateway.service.dto.DayBookDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.ReverseDTO;

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
