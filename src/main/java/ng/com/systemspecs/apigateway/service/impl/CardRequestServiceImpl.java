package ng.com.systemspecs.apigateway.service.impl;

import ng.com.systemspecs.apigateway.domain.CardRequest;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.repository.CardRequestRepository;
import ng.com.systemspecs.apigateway.service.CardRequestService;
import ng.com.systemspecs.apigateway.service.dto.CardRequestDTO;
import ng.com.systemspecs.apigateway.service.dto.ProfileDTO;
import ng.com.systemspecs.apigateway.service.mapper.CardRequestMapper;
import ng.com.systemspecs.apigateway.service.mapper.ProfileMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CardRequestServiceImpl implements CardRequestService {

    private final CardRequestRepository cardRequestRepository;
    private final CardRequestMapper cardRequestMapper;
    private final ProfileMapper profileMapper;

    public CardRequestServiceImpl(CardRequestRepository cardRequestRepository, CardRequestMapper cardRequestMapper, ProfileMapper profileMapper) {
        this.cardRequestRepository = cardRequestRepository;
        this.cardRequestMapper = cardRequestMapper;
        this.profileMapper = profileMapper;
    }

    @Override
    public CardRequestDTO save(CardRequestDTO cardRequestDTO) {
        CardRequest cardRequest = cardRequestRepository.save(cardRequestMapper.toEntity(cardRequestDTO));
        return cardRequestMapper.toDto(cardRequest);
    }

    @Override
    public List<CardRequestDTO> save(List<CardRequestDTO> cardRequestDTOList) {
        List<CardRequest> cardRequests= cardRequestRepository.saveAll(cardRequestMapper.toEntity(cardRequestDTOList));
        return cardRequests.stream().map(cardRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CardRequestDTO> findAllByCardtype(String Cardtype) {
        return cardRequestRepository.findByCardtypeEqualsIgnoreCase(Cardtype).stream().map(cardRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CardRequestDTO> findAllByScheme(String SCHEME) {
        return cardRequestRepository.findAllByScheme(SCHEME).stream().map(cardRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CardRequestDTO> findAllBySchemeAndCardtype(String SCHEME, String Cardtype) {
        return cardRequestRepository.findAllBySchemeAndCardtype(SCHEME, Cardtype).stream().map(cardRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CardRequestDTO> findAllBySchemeAndCardtypeAndCreatedDateBetween(String SCHEME, String Cardtype, Instant startDate, Instant endDate) {
        return cardRequestRepository.findAllBySchemeAndCardtypeAndCreatedDateBetween(SCHEME, Cardtype, startDate, endDate).stream().map(cardRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CardRequestDTO> findAllByStatus(String status) {
        return cardRequestRepository.findAllByStatusEqualsIgnoreCase(status).stream().map(cardRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CardRequestDTO> findActiveCardRequests(String status) {
        return cardRequestRepository.findAllByStatusEqualsIgnoreCase("NEW").stream().map(cardRequestMapper::toDto).collect(Collectors.toList());
    }


    @Override
    public Optional<CardRequest> findById(Long id) {
        return cardRequestRepository.findById(id);
    }

    @Override
    public Page<CardRequestDTO> findAll(Pageable pageable) {
        return cardRequestRepository.findAll(pageable).map(cardRequestMapper::toDto);
    }

    @Override
    public List<CardRequestDTO> findAll() {
        return cardRequestRepository.findAll().stream().map(cardRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public long countAllRequests() {
        return cardRequestRepository.count();
    }

//    @Override
//    public Page<CardRequestDTO> findAllWithKeyword(Pageable pageable, String key) {
//        return null;
//    }

    @Override
    public List<CardRequestDTO> findAllByCardtypeAndCreatedDateBetween(String cardtype, Instant startDate, Instant endDate) {
        return cardRequestRepository.findAllByCardtypeAndCreatedDateBetween(cardtype, startDate, endDate).stream().map(cardRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<CardRequestDTO> findAllByCreatedDateBetween(Instant startDate, Instant endDate) {
        return cardRequestRepository.findAllByCreatedDateBetween(startDate, endDate).stream().map(cardRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public CardRequest findActiveRequest(ProfileDTO p, String scheme) {
        Profile owner = profileMapper.toEntity(p);
        CardRequest c = cardRequestRepository.findOneByOwnerAndSchemeAndStatus(owner, scheme, "NEW");
        return c;
    }

    @Override
    public List<CardRequestDTO> findAllBySchemeAndCreatedDateBetween(String scheme, Instant startDate, Instant endDate) {
        return cardRequestRepository.findAllBySchemeAndCreatedDateBetween(scheme, startDate, endDate).stream().map(cardRequestMapper::toDto).collect(Collectors.toList());
    }
}
