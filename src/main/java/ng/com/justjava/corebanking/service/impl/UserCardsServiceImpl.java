package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.UserCardsRepository;
import ng.com.justjava.corebanking.service.mapper.UserCardsMapper;
import ng.com.justjava.corebanking.domain.UserCards;
import ng.com.justjava.corebanking.service.UserCardsService;
import ng.com.justjava.corebanking.service.dto.UserCardsDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserCardsServiceImpl implements UserCardsService {

    private final UserCardsRepository userCardsRepository;
    private final UserCardsMapper userCardsMapper;

    public UserCardsServiceImpl(UserCardsRepository userCardsRepository, UserCardsMapper userCardsMapper) {
        this.userCardsRepository = userCardsRepository;
        this.userCardsMapper = userCardsMapper;
    }


    @Override
    public UserCardsDTO save(UserCardsDTO userCardsDTO) {
        return userCardsMapper.toDto(userCardsRepository.save(userCardsMapper.toEntity(userCardsDTO)));
    }

    @Override
    public void delete(UserCardsDTO userCardsDTO) {
        userCardsRepository.delete(userCardsMapper.toEntity(userCardsDTO));
    }

    @Override
    public List<UserCardsDTO> findAllByOwner_Id(Long id) {
        return userCardsRepository.findAllByOwner_Id(id).stream().map(userCardsMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<UserCardsDTO> findAllByOwner_PhoneNumber(String phoneNumber) {
        return userCardsRepository.findAllByOwner_PhoneNumber(phoneNumber).stream().map(userCardsMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<UserCardsDTO> findAllByScheme(String scheme) {
        return userCardsRepository.findAllByScheme(scheme).stream().map(userCardsMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<UserCardsDTO> findAllByStatus(String status) {
        return userCardsRepository.findAllByStatus(status).stream().map(userCardsMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<UserCardsDTO> findAllBySchemeAndOwner_PhoneNumber(String scheme, String phoneNumber) {
        return userCardsRepository.findAllBySchemeAndOwner_PhoneNumber(scheme,phoneNumber).stream().map(userCardsMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<UserCards> findOneBySchemeAndOwner_PhoneNumber(String scheme, String phoneNumber) {
        return userCardsRepository.findOneBySchemeAndOwner_PhoneNumber(scheme, phoneNumber);
    }

    @Override
    public Optional<UserCards> findOneByAccountNumber(String accountNumber) {
        return userCardsRepository.findOneByAccountNumber(accountNumber);
    }

    @Override
    public int countByScheme(String scheme) {
        return userCardsRepository.countByScheme(scheme);
    }

    @Override
    public int countBySchemeAndStatus(String scheme, String status) {
        return userCardsRepository.countBySchemeAndStatus(scheme, status);
    }

    @Override
    public int countByStatus(String status) {
        return userCardsRepository.countByStatus(status);
    }

    @Override
    public long count() {
        return userCardsRepository.count();
    }

    @Override
    public List<UserCards> findAllByAccountNumber(String accountNumber) {
        return userCardsRepository.findAllByAccountNumber(accountNumber);
    }
}
