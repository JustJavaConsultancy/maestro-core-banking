package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.MyDeviceRepository;
import ng.com.justjava.corebanking.service.mapper.MyDeviceMapper;
import ng.com.justjava.corebanking.config.AsyncConfiguration;
import ng.com.justjava.corebanking.domain.MyDevice;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.domain.enumeration.DeviceStatus;
import ng.com.justjava.corebanking.service.MyDeviceService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.dto.ChangeMyDeviceStatusDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.LostPasswordDTO;
import ng.com.justjava.corebanking.service.dto.MyDeviceDTO;
import ng.com.justjava.corebanking.service.dto.ValidateSecretDTO;
import ng.com.justjava.corebanking.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MyDevice}.
 */
@Service
@Transactional
public class MyDeviceServiceImpl implements MyDeviceService {

    private final Logger log = LoggerFactory.getLogger(MyDeviceServiceImpl.class);

    private final MyDeviceRepository myDeviceRepository;
    private final ProfileService profileService;
    private final Utility utility;
    private final AsyncConfiguration asyncConfiguration;

    private final MyDeviceMapper myDeviceMapper;

    public MyDeviceServiceImpl(MyDeviceRepository myDeviceRepository, ProfileService profileService, @Lazy Utility utility, AsyncConfiguration asyncConfiguration, MyDeviceMapper myDeviceMapper) {
        this.myDeviceRepository = myDeviceRepository;
        this.profileService = profileService;
        this.utility = utility;
        this.asyncConfiguration = asyncConfiguration;
        this.myDeviceMapper = myDeviceMapper;
    }

    @Override
    public MyDeviceDTO save(MyDeviceDTO myDeviceDTO) {
        log.debug("Request to save MyDevice : {}", myDeviceDTO);
        MyDevice myDevice = myDeviceMapper.toEntity(myDeviceDTO);
        myDevice = myDeviceRepository.save(myDevice);
        return myDeviceMapper.toDto(myDevice);
    }

    @Override
    public MyDeviceDTO save(MyDeviceDTO myDeviceDTO, Profile profile) {
        log.debug("Request to save MyDevice : {}", myDeviceDTO);
        MyDevice myDevice = myDeviceMapper.toEntity(myDeviceDTO);
        myDevice.setProfile(profile);
        myDevice = myDeviceRepository.save(myDevice);
        return myDeviceMapper.toDto(myDevice);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MyDeviceDTO> findAll() {
        log.debug("Request to get all MyDevices");
        return myDeviceRepository.findAll().stream()
            .map(myDeviceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<MyDeviceDTO> findOne(Long id) {
        log.debug("Request to get MyDevice : {}", id);
        return myDeviceRepository.findById(id)
            .map(myDeviceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete MyDevice : {}", id);
        myDeviceRepository.deleteById(id);
    }

    @Override
    public GenericResponseDTO updateMyDeviceOrCreateNew(MyDeviceDTO myDeviceDTO, HttpSession session) {
        log.debug("Request to update or create MyDevice : {}", myDeviceDTO);
        String phoneNumber = myDeviceDTO.getPhoneNumber();
        myDeviceDTO.setPhoneNumber(utility.formatPhoneNumber(phoneNumber));

        Profile profile = profileService.findByPhoneNumber(phoneNumber);
        profile.setDeviceNotificationToken(myDeviceDTO.getDeviceNotificationToken());
        profile = profileService.save(profile);
        log.debug("Saved profile device token: {} ", profile);

        Optional<MyDevice> deviceOptional = myDeviceRepository.findByDeviceId(myDeviceDTO.getDeviceId());
        if (deviceOptional.isPresent()) {
            myDeviceDTO.setId(deviceOptional.get().getId());
            log.debug("Request to update MyDevice with Id : {}", myDeviceDTO.getId());
        } else {
            myDeviceDTO.setStatus(DeviceStatus.ACTIVE);
            log.debug("Request to create a new Device with device Id : {}", myDeviceDTO.getDeviceId());
        }

        myDeviceDTO.setLast_login_date(ZonedDateTime.now());

        MyDeviceDTO save = save(myDeviceDTO, profile);
        log.debug("Saved my device : {}", save);
        return new GenericResponseDTO("00", HttpStatus.OK, "success", save);


    }


    @Override
    public GenericResponseDTO changeDeviceStatus(ChangeMyDeviceStatusDTO changeMyDeviceStatusDTO) {
        String phoneNumber = utility.formatPhoneNumber(changeMyDeviceStatusDTO.getPhoneNumber());
        String answer = changeMyDeviceStatusDTO.getAnswer();
        String question = changeMyDeviceStatusDTO.getQuestion();
        String pin = changeMyDeviceStatusDTO.getPin();

        LostPasswordDTO lostPasswordDTO = new LostPasswordDTO();
        lostPasswordDTO.setPhoneNumber(phoneNumber);
        lostPasswordDTO.setPin(pin);

        GenericResponseDTO genericResponseDTO = profileService.validateUserPin(lostPasswordDTO);
        if (genericResponseDTO != null) {
            if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                ValidateSecretDTO validateSecretDTO = new ValidateSecretDTO();
                validateSecretDTO.setAnswer(answer);
                validateSecretDTO.setQuestion(question);
                validateSecretDTO.setPhoneNumber(phoneNumber);
                genericResponseDTO = profileService.validateSecurityAnswer(validateSecretDTO);

                if (genericResponseDTO != null && HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                    String status = changeMyDeviceStatusDTO.getStatus().toUpperCase();
                    String deviceId = changeMyDeviceStatusDTO.getDeviceId();
                    try {
                        DeviceStatus deviceStatus = DeviceStatus.valueOf(status);
                        Optional<MyDevice> deviceOptional = findByDeviceId(deviceId);
                        if (deviceOptional.isPresent()) {
                            MyDevice myDevice = deviceOptional.get();
                            myDevice.setStatus(deviceStatus);
                            MyDeviceDTO save = save(myDeviceMapper.toDto(myDevice));
                            log.debug("Saved Device status  ===> " + save);

                            if (DeviceStatus.INACTIVE.equals(save.getStatus())) {
                                Profile profile = myDevice.getProfile();
                                if (profile != null) {
                                    User user = profile.getUser();
                                    String email = "";
                                    if (user != null) {
                                        email = user.getEmail();
                                    }
                                    utility.sendToNotificationConsumer(profile.getDeviceNotificationToken(),
                                        "This device has been deactivated", "Device deactivation", "",
                                        "", "", "", profile.getPhoneNumber());
                                }
                            }

                            Profile profile = myDevice.getProfile();
                            if (profile != null) {
                                User user = profile.getUser();
                                if (user != null) {
                                    utility.sendAlertEmailForAction(user, "Activate/Deactivate Device", LocalDateTime.now());
                                }
                            }


                            return new GenericResponseDTO("00", HttpStatus.OK, "success", save);
                        }

                        return new GenericResponseDTO("99", HttpStatus.OK, "Invalid deviceId", null);


                    } catch (Exception e) {
                        e.printStackTrace();
                        return new GenericResponseDTO("99", HttpStatus.OK, e.getLocalizedMessage(), null);
                    }
                }
            }
        }

        return genericResponseDTO;


    }

    @Override
    public Optional<MyDevice> findByDeviceId(@NotNull String deviceId) {
        return myDeviceRepository.findByDeviceId(deviceId);
    }

    @Override
    public GenericResponseDTO getMyPersonalDevices() {
        User currentUser = utility.getCurrentUser();
        if (currentUser != null) {
            List<MyDevice> myDevices = findByProfilePhoneNumber(currentUser.getLogin());
            return new GenericResponseDTO("00", HttpStatus.OK, "success", myDevices);
        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Current login user is null", null);
    }

    @Override
    public List<MyDevice> findByProfilePhoneNumber(String phoneNumber) {
        return myDeviceRepository.findByProfilePhoneNumber(utility.formatPhoneNumber(phoneNumber));
    }

    @Override
    public Optional<MyDevice> findByProfilePhoneNumberAndDeviceId(String phoneNumber, String deviceId) {
        return myDeviceRepository.findByProfilePhoneNumberAndDeviceId(phoneNumber, deviceId);
    }
}
