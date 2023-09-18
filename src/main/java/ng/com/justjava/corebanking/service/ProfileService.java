package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Profile}.
 */
public interface ProfileService {

    /**
     * Save a profile.
     *
     * @param profileDTO the entity to save.
     * @return the persisted entity.
     */
    ProfileDTO save(ProfileDTO profileDTO);

    Profile save(Profile profile);

    /**
     * Get all the profiles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProfileDTO> findAll(Pageable pageable);

    Page<ProfileDTO> findAllWithKeyword(Pageable pageable, String keyword);


    /**
     * Get the "id" profile.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProfileDTO> findOne(Long id);

    /**
     * Delete the "id" profile.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Optional<ProfileDTO> findOneByPhoneNumber(String phoneNumber);

    Profile findByPhoneNumber(String phoneNumber);

    Boolean canSpendOnAccount(String phoneNumber, String accountNumber, Double amount);

    Boolean canAccummulateOnAccount(String phoneNumber, String accountNumber, Double amount);

    Boolean shouldSendApproachLimitNotification(String phoneNumber, String accountNumber, Double amount);

    Optional<ProfileDTO> findByUserIsCurrentUser();

    Optional<Profile> findCurrentUserProfile();

    Page<ProfileDTO> findAllByCreatedDateBetween(Pageable pageable, LocalDate startDate, LocalDate endDate);

    Page<ProfileDTO> findAllWithKeywordWithScheme(Pageable pageable, String key, String schemeId);

    List<ProfileDTO> getUserListByAdminAndSuperAdminRole();

    List<Profile> findByDeviceNotificationToken(String deviceNotificationToken);

    Double getUserTotalBonus();

    GenericResponseDTO validateSecurityAnswer(ValidateSecretDTO validateSecretDTO);

    GenericResponseDTO updatePin(LostPinDTO lostPinDTO);

    GenericResponseDTO validateUserPin(LostPasswordDTO lostPasswordDTO);

    GenericResponseDTO updateSecurityQuestion(ValidateSecretDTO validateSecretDTO);

    GenericResponseDTO hmUpdateSecurityQuestion(ValidateSecretDTO validateSecretDTO);

    GenericResponseDTO validateNin(String nin, HttpSession session);

    GenericResponseDTO retrieveNINDetails(String nin, String otp, HttpSession session);

    NINDataResponseDTO getNINDetails(NINRequestDTO ninRequestDTO);

    ResponseEntity<PostResponseDTO> updateDeviceToken(DeviceTokenDTO deviceTokenDTO, HttpSession session);
}
