package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.AccessItem;
import ng.com.systemspecs.apigateway.service.dto.AccessItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link AccessItem}.
 */
public interface AccessItemService {

    AccessItem save(AccessItem accessItem);

    AccessItem save(AccessItemDTO accessItemDTO);


    List<AccessItem> findAll();


    /**
     * Get the "id" bank.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccessItem> findOne(Long id);

    /**
     * Delete the "id" bank.
     *
     * @param id the id of the entity.
     */
    void delete(AccessItem id);


    Page<AccessItem> findAll(Pageable pageable);

    List<AccessItem> findAllByRightCode(String rightCode);

    List<AccessItem> findAllByRightCodeAndMakerFalse(String rightCode);

    List<AccessItem> findAllByRightCodeAndMakerTrue(String rightCode);

    List<AccessItem> findAllByPhoneNumber(String phoneNumber);

    List<AccessItem> findByPhoneNumberAndRightCodeAndIsMaker(String phoneNumber, String rightCode);

    List<AccessItem> findByPhoneNumberAndRightCodeAndIsChecker(String phoneNumber, String rightCode);
}
