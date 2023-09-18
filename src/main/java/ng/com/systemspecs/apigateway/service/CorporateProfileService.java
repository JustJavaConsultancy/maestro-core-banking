package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.CorporateProfile;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.service.dto.CorporateProfileDto;
import ng.com.systemspecs.apigateway.service.dto.ProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface CorporateProfileService {

        /**
         * Save a corporate profile.
         *
         * @param CorporateProfileDto the entity to save.
         * @return the persisted entity.
         */
        CorporateProfileDto save(CorporateProfileDto CorporateProfileDto);

        CorporateProfile save(CorporateProfile profile);

        /**
         * Get all the CorporateProfiles.
         *
         * @param pageable the pagination information.
         * @return the list of entities.
         */
        Page<CorporateProfileDto> findAll(Pageable pageable);

        Page<CorporateProfileDto> findAllWithKeyword(Pageable pageable, String keyword);


        /**
         * Get the "id" CorporateProfile.
         *
         * @param id the id of the entity.
         * @return the entity.
         */
        Optional<CorporateProfileDto> findOne(Long id);

        /**
         * Delete the "id" CorporateProfile.
         *
         * @param id the id of the entity.
         */
        void delete(Long id);

        Optional<CorporateProfileDto> findOneByPhoneNumber(String phoneNumber);

        CorporateProfile findByPhoneNumber(String phoneNumber);

//        CorporateProfile findOneByProfileId(String profileId);

    CorporateProfile findOneByProfile(Profile profileI);

    CorporateProfile findOneByProfilePhoneNumber(String phoneNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByRcNO(String rcNo);

    Page<CorporateProfileDto> findAllByCreatedDateBetween(Pageable pageable, LocalDate fromDate, LocalDate toDate);
}
