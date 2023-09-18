package ng.com.systemspecs.apigateway.service.impl;

import ng.com.systemspecs.apigateway.service.AppUpdateService;
import ng.com.systemspecs.apigateway.domain.AppUpdate;
import ng.com.systemspecs.apigateway.repository.AppUpdateRepository;
import ng.com.systemspecs.apigateway.service.dto.AppUpdateDTO;
import ng.com.systemspecs.apigateway.service.mapper.AppUpdateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AppUpdate}.
 */
@Service
@Transactional
public class AppUpdateServiceImpl implements AppUpdateService {

    private final Logger log = LoggerFactory.getLogger(AppUpdateServiceImpl.class);

    private final AppUpdateRepository appUpdateRepository;

    private final AppUpdateMapper appUpdateMapper;

    public AppUpdateServiceImpl(AppUpdateRepository appUpdateRepository, AppUpdateMapper appUpdateMapper) {
        this.appUpdateRepository = appUpdateRepository;
        this.appUpdateMapper = appUpdateMapper;
    }

    @Override
    public AppUpdateDTO save(AppUpdateDTO appUpdateDTO) {
        log.debug("Request to save AppUpdate : {}", appUpdateDTO);
        AppUpdate previousUpdate;
        if(!appUpdateDTO.getDevice().toString().equals("ALL")) {
        	previousUpdate = appUpdateRepository.findLatestUpdate();
        	if(appUpdateDTO.getDevice().toString().equals("ANDROID")) {
        		appUpdateDTO.setIosurl(previousUpdate.getIosurl());
        		appUpdateDTO.setIosversion(previousUpdate.getIosversion());
        	}
        	else {
        		appUpdateDTO.setAndroidurl(previousUpdate.getAndroidurl());
        		appUpdateDTO.setAndroidversion(previousUpdate.getAndroidversion());
        	}
        }
        AppUpdate appUpdate = appUpdateMapper.toEntity(appUpdateDTO);
        appUpdate = appUpdateRepository.save(appUpdate);
        return appUpdateMapper.toDto(appUpdate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppUpdateDTO> findAll() {
        log.debug("Request to get all AppUpdates");
        return appUpdateRepository.findAll().stream()
            .map(appUpdateMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<AppUpdateDTO> findOne(Long id) {
        log.debug("Request to get AppUpdate : {}", id);
        return appUpdateRepository.findById(id)
            .map(appUpdateMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AppUpdate : {}", id);
        appUpdateRepository.deleteById(id);
    }

	@Override
	public AppUpdateDTO getLatestUpdate() {
		// TODO Auto-generated method stub
		AppUpdate theUpdate =  appUpdateRepository.findLatestUpdate();
		return appUpdateMapper.toDto(theUpdate);
		//return null;
	}
}
