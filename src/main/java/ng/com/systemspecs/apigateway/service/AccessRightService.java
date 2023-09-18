package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.AccessRight;
import ng.com.systemspecs.apigateway.service.dto.AccessRightDTO;
import ng.com.systemspecs.apigateway.service.dto.CreateAccessRightDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.AccessRight}.
 */
public interface AccessRightService {

    AccessRight save(AccessRight accessRight);


    List<AccessRight> findAll();


    /**
     * Get the "id" bank.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccessRight> findOne(Long id);

    /**
     * Delete the "id" bank.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


    Page<AccessRight> findAll(Pageable pageable);

    GenericResponseDTO createAccessRight(CreateAccessRightDTO createAccessRightDTO);

    Optional<AccessRight> findByPhoneNumber(String phoneNumber);

    List<AccessRightDTO> findAllAccessRights();

    GenericResponseDTO updateAccessRight(CreateAccessRightDTO createAccessRightDTO);
}
