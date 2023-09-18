package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.NotificationRepository;
import ng.com.justjava.corebanking.service.mapper.NotificationMapper;
import ng.com.justjava.corebanking.domain.Notification;
import ng.com.justjava.corebanking.service.NotificationService;
import ng.com.justjava.corebanking.service.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Notification}.
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public NotificationDTO save(NotificationDTO notificationDTO) {
        log.debug("Request to save Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable)
            .map(notificationMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOne(Long id) {
        log.debug("Request to get Notification : {}", id);
        return notificationRepository.findById(id)
            .map(notificationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }

    @Override
    public List<NotificationDTO> findCustomerNotifications() {
        String phoneNumber = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            phoneNumber = ((UserDetails) principal).getUsername();
        } else
            phoneNumber = principal.toString();

        return notificationRepository.findByRecipient(phoneNumber).stream().map(notificationMapper::toDto).collect(Collectors.toList());

    }
}
