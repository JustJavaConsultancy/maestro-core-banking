package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.MyDevice;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.service.dto.ChangeMyDeviceStatusDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.MyDeviceDTO;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.MyDevice}.
 */
public interface MyDeviceService {

    /**
     * Save a myDevice.
     *
     * @param myDeviceDTO the entity to save.
     * @return the persisted entity.
     */
    MyDeviceDTO save(MyDeviceDTO myDeviceDTO);

    /**
     * Save a myDevice.
     *
     * @param myDeviceDTO the entity to save.
     * @return the persisted entity.
     */
    MyDeviceDTO save(MyDeviceDTO myDeviceDTO, Profile profile);

    /**
     * Get all the myDevices.
     *
     * @return the list of entities.
     */
    List<MyDeviceDTO> findAll();


    /**
     * Get the "id" myDevice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MyDeviceDTO> findOne(Long id);

    /**
     * Delete the "id" myDevice.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    GenericResponseDTO updateMyDeviceOrCreateNew(MyDeviceDTO myDeviceDTO, HttpSession session);

    GenericResponseDTO changeDeviceStatus(ChangeMyDeviceStatusDTO changeMyDeviceStatusDTO);

    Optional<MyDevice> findByDeviceId(@NotNull String deviceId);

    GenericResponseDTO getMyPersonalDevices();

    List<MyDevice> findByProfilePhoneNumber(String phoneNumber);

    Optional<MyDevice> findByProfilePhoneNumberAndDeviceId(String phoneNumber, String deviceId);


}
