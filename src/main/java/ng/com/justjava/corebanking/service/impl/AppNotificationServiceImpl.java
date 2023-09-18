package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.AppNotificationRepository;
import ng.com.justjava.corebanking.service.dto.AppNotificationDTO;
import ng.com.justjava.corebanking.service.dto.AppNotificationResponseDTO;
import ng.com.justjava.corebanking.service.dto.PushNotificationRequest;
import ng.com.justjava.corebanking.service.mapper.AppNotificationMapper;
import ng.com.justjava.corebanking.domain.AppNotification;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.domain.enumeration.AppNotificationType;
import ng.com.justjava.corebanking.service.AppNotificationService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.fcm.PushNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link AppNotification}.
 */
@Service
@Transactional
public class AppNotificationServiceImpl implements AppNotificationService {

    private final Logger log = LoggerFactory.getLogger(AppNotificationServiceImpl.class);

    private final AppNotificationRepository appNotificationRepository;
    private final AppNotificationMapper appNotificationMapper;
    private final PushNotificationService pushNotificationService;
    private final WalletAccountService walletAccountService;

    public AppNotificationServiceImpl(AppNotificationRepository appNotificationRepository, AppNotificationMapper appNotificationMapper, PushNotificationService pushNotificationService, WalletAccountService walletAccountService) {
        this.appNotificationRepository = appNotificationRepository;
        this.appNotificationMapper = appNotificationMapper;
        this.pushNotificationService = pushNotificationService;
        this.walletAccountService = walletAccountService;
    }

    @Override
    public Optional<AppNotificationDTO> findById(Long id) {
        return appNotificationRepository.findById(id).map(appNotificationMapper::toDto);
    }

    @Override
    public AppNotificationDTO findOneById(Long id) {
        AppNotification a = appNotificationRepository.findOneById(id);
        return appNotificationMapper.toDto(a);
    }

    @Override
    public List<AppNotificationDTO> findAllByScheme(String scheme) {
        return appNotificationRepository.findAllByScheme(scheme).stream().map(appNotificationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AppNotificationDTO> findAllByAudience(String audience) {
        return appNotificationRepository.findAllByAudience(audience);
    }

    @Override
    public List<AppNotificationDTO> findAllByNotificationType(AppNotificationType notificationtype) {
        return appNotificationRepository.findAllByNotificationType(notificationtype).stream().map(appNotificationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AppNotificationDTO> findAllByCreatedDateBetween(Instant startDate, Instant endDate) {
        return appNotificationRepository.findAllByCreatedDateBetween(startDate, endDate).stream().map(appNotificationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<AppNotificationDTO> findAllBySchemeAndCreatedDateBetween(String scheme, Instant startDate, Instant endDate) {
        return appNotificationRepository.findAllBySchemeAndCreatedDateBetween(scheme, startDate, endDate).stream().map(appNotificationMapper::toDto).collect(Collectors.toList());
    }


    @Override
    public AppNotificationDTO save(AppNotificationDTO notificationDTO) {
        AppNotification appNotification = appNotificationMapper.toEntity(notificationDTO);
        return appNotificationMapper.toDto(appNotificationRepository.save(appNotification));
    }

    @Override
    public Page<AppNotificationDTO> findAll(Pageable pageable) {
        return appNotificationRepository.findAll(pageable).map(appNotificationMapper::toDto);
    }

    @Override
    public AppNotificationResponseDTO findCustomerAppNotifications(String scheme) {
        List<AppNotificationDTO> in_app = appNotificationRepository.findAllByNotificationType(AppNotificationType.IN_APP).stream().filter(p -> p.getScheme().equalsIgnoreCase(scheme)||p.getAudience().equalsIgnoreCase("ALL")).map(appNotificationMapper::toDto).collect(Collectors.toList());
        List<AppNotificationDTO> push_notification = appNotificationRepository.findAllByNotificationType(AppNotificationType.PUSH_NOTIFICATION).stream().filter(p -> p.getScheme().equalsIgnoreCase(scheme)||p.getAudience().equalsIgnoreCase("ALL")).map(appNotificationMapper::toDto).collect(Collectors.toList());
        in_app =  in_app.stream().filter(p->p.isDisplay()).collect(Collectors.toList());
        push_notification =  push_notification.stream().filter(p->p.isDisplay()).collect(Collectors.toList());
        return new AppNotificationResponseDTO(in_app, push_notification);
    }

    @Override
    public List<AppNotification> findAllByNotificationTypeAndAudience(String notificationType, String audience) {
        return appNotificationRepository.findAllByNotificationTypeAndAudience(notificationType,audience);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AppNotificationDTO> findOne(Long id) {
        log.debug("Request to get App Notification : {}", id);
        return appNotificationRepository.findById(id)
            .map(appNotificationMapper::toDto);
    }

    @Override
    public Page<AppNotificationDTO> findAllWithKeyword(Pageable pageable, String keyword) {
        log.debug("Request to get all Corporate Profiles with keyword");
        return appNotificationRepository.findAllWithKeyword(pageable, keyword).map(appNotificationMapper::toDto);
    }

    @Override
    public List<AppNotificationDTO> findAllByNotificationTypeAndScheme(AppNotificationType appNotificationType, String scheme) {
        return appNotificationRepository.findAllByNotificationTypeAndScheme(appNotificationType, scheme).stream().map(appNotificationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void sendAppSystemMessage(AppNotificationDTO notificationDTO) {
        PushNotificationRequest request = new PushNotificationRequest();
        List<WalletAccount> schemeAccts = walletAccountService.findAllByAccountOwnerIsNotNullAndScheme(notificationDTO.getScheme());
        for(WalletAccount w: schemeAccts) {
            Profile accountOwner = w.getAccountOwner();
            request.setMessage(notificationDTO.getContent());
            request.setTitle(notificationDTO.getTitle());
            request.setToken(accountOwner.getDeviceNotificationToken());
            request.setRecipient(accountOwner.getPhoneNumber());
            pushNotificationService.sendPushNotificationToToken(request);
        }
    }

}
