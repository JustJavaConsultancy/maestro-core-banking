package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.ProfileTypeRepository;
import ng.com.justjava.corebanking.service.mapper.ProfileTypeMapper;
import ng.com.justjava.corebanking.service.ProfileTypeService;
import ng.com.justjava.corebanking.domain.ProfileType;
import ng.com.justjava.corebanking.service.dto.ProfileTypeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ProfileType}.
 */
@Service
@Transactional
public class ProfileTypeServiceImpl implements ProfileTypeService {

    private final Logger log = LoggerFactory.getLogger(ProfileTypeServiceImpl.class);

    private final ProfileTypeRepository profileTypeRepository;

    private final ProfileTypeMapper profileTypeMapper;

    public ProfileTypeServiceImpl(ProfileTypeRepository profileTypeRepository, ProfileTypeMapper profileTypeMapper) {
        this.profileTypeRepository = profileTypeRepository;
        this.profileTypeMapper = profileTypeMapper;
    }

    @Override
    public ProfileTypeDTO save(ProfileTypeDTO profileTypeDTO) {
        log.debug("Request to save ProfileType : {}", profileTypeDTO);
        ProfileType profileType = profileTypeMapper.toEntity(profileTypeDTO);
        profileType = profileTypeRepository.save(profileType);
        return profileTypeMapper.toDto(profileType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfileTypeDTO> findAll() {
        log.debug("Request to get all ProfileTypes");
        return profileTypeRepository.findAll().stream()
            .map(profileTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ProfileTypeDTO> findOne(Long id) {
        log.debug("Request to get ProfileType : {}", id);
        return profileTypeRepository.findById(id)
            .map(profileTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProfileType : {}", id);
        profileTypeRepository.deleteById(id);
    }

	@Override
	public ProfileType findByProfiletype(String profiletype) {
		// TODO Auto-generated method stub
		return profileTypeRepository.findByProfiletype(profiletype);
	}
}
