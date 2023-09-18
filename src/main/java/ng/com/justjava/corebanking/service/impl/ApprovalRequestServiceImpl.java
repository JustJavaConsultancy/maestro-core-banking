package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.domain.*;
import ng.com.justjava.corebanking.domain.enumeration.*;
import ng.com.justjava.corebanking.repository.ApprovalRequestRepository;
import ng.com.justjava.corebanking.repository.UserRepository;
import ng.com.justjava.corebanking.security.SecurityUtils;
import ng.com.justjava.corebanking.service.*;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.mapper.ApprovalRequestMapper;
import ng.com.justjava.corebanking.service.mapper.WalletAccountMapper;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.systemspecs.apigateway.domain.enumeration.*;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.justjava.corebanking.util.Utility;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApprovalRequestServiceImpl implements ApprovalRequestService {

    private final ApprovalRequestRepository approvalRequestRepository;
    private final ApprovalRequestMapper approvalRequestMapper;
    private final ProfileService profileService;
    private final WalletAccountService walletAccountService;
    private final WalletAccountMapper walletAccountMapper;
    private final JournalService journalService;
    private final AccessItemService accessItemService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final Utility utility;
    private final ApprovalWorkflowService approvalWorkflowService;

    private final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    @Value("${app.document-url}")
    private String documentUrl;
    private User theUser;


    public ApprovalRequestServiceImpl(ApprovalRequestRepository approvalRequestRepository,
                                      ApprovalRequestMapper approvalRequestMapper, @Lazy ProfileService profileService,
                                      @Lazy WalletAccountService walletAccountService, WalletAccountMapper walletAccountMapper, @Lazy JournalService journalService,
                                      AccessItemService accessItemService, UserRepository userRepository, UserService userService, @Lazy Utility utility, ApprovalWorkflowService approvalWorkflowService) {

        this.approvalRequestRepository = approvalRequestRepository;
        this.approvalRequestMapper = approvalRequestMapper;
        this.profileService = profileService;
        this.walletAccountService = walletAccountService;
        this.walletAccountMapper = walletAccountMapper;
        this.journalService = journalService;
        this.accessItemService = accessItemService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.utility = utility;
        this.approvalWorkflowService = approvalWorkflowService;
    }

    @Override
    public ApprovalRequestDTO save(ApprovalRequestDTO approvalRequestDTO, ApprovalGroup approver, ApprovalGroup initiator) {
        log.debug("Request to save ApprovalRequest : {}", approvalRequestDTO);
        ApprovalRequest approvalRequest = approvalRequestMapper.toEntity(approvalRequestDTO);
        approvalRequest.setApprover(approver);
        approvalRequest.setInitiator(initiator);
        approvalRequest = approvalRequestRepository.save(approvalRequest);
        return approvalRequestMapper.toDto(approvalRequest);
    }

    @Override
    public List<ApprovalRequestDTO> findAll() {
        return approvalRequestRepository.findAll().stream().map(approvalRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<ApprovalRequestDTO> findAll(Pageable pageable) {
        return approvalRequestRepository.findAll(pageable).map(approvalRequestMapper::toDto);
    }

    @Override
    public Optional<ApprovalRequestDTO> findOne(Long id) {
        return approvalRequestRepository.findById(id)
            .map(approvalRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        approvalRequestRepository.deleteById(id);
    }

    @Override
    public GenericResponseDTO createApprovalRequest(ApprovalRequestDTO approvalRequestDTO) {

        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            this.theUser = user;
        });

        if (theUser == null) {
            return new GenericResponseDTO("401", HttpStatus.BAD_REQUEST, "Invalid Login User, please login again", null);

        }
        String login = theUser.getLogin();
        Profile initiatorProfile = profileService.findByPhoneNumber(login);

        String requestTypeCode = approvalRequestDTO.getRequestTypeCode().trim();
        ApprovalWorkflow approvalWorkflow = approvalWorkflowService.findByTransactionType_code(requestTypeCode);

        if (approvalWorkflow == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Approval workflow not found for this request", null);
        }

        ApprovalGroup initiator = approvalWorkflow.getInitiator();
        if (initiator == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Initiator not found for this request", null);
        }
        Set<Profile> initiatorProfiles = initiator.getProfiles();

        if (!initiatorProfiles.contains(initiatorProfile)) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "You do not have access to make this request", null);
        }

        if (approvalRequestDTO.getStatus() == null) {
            approvalRequestDTO.setStatus(RequestStatus.AWAITING_APPROVAL.getName());
        }

        ApprovalGroup approver = approvalWorkflow.getApprover();
        if (approver == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Approver not found for this request", null);
        }

        Set<Profile> approverProfiles = approver.getProfiles();

//        List<AccessItem> accessItemList = accessItemService.findAllByRightCodeAndMakerFalse(requestTypeCode);
//        if (accessItemList.isEmpty()) {
//            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Checker is not available for this request", null);
//        }

//        AccessItem accessItem = accessItemList.get(0);
//        profile = accessItem.getAccessRight().getProfile();

        if (approverProfiles.isEmpty()) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Approver Profiles not found for this request", null);
        }

        String requestId = utility.getUniqueRequestId();
        approvalRequestDTO.setRequestId(requestId);

        if (!StringUtils.isEmpty(approvalRequestDTO.getSupportingDoc()) || !StringUtils.isEmpty(approvalRequestDTO.getDocType())) {

            String encodedString = approvalRequestDTO.getSupportingDoc();

            String photoName;

            if (DocumentType.JPG.name().equalsIgnoreCase(approvalRequestDTO.getDocType())) {
                photoName = "document-" + requestId + ".jpg";
            } else if (DocumentType.PDF.name().equalsIgnoreCase(approvalRequestDTO.getDocType())) {
                photoName = "document-" + requestId + ".pdf";
            } else {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Document type", null);
            }
        String outputFileName = documentUrl + photoName;

            approvalRequestDTO = saveImage(approvalRequestDTO, encodedString, photoName, outputFileName);
        }

        ApprovalRequestDTO save = save(approvalRequestDTO, approver, initiator);

        if (save != null) {
            log.info("Saved request +++===> " + save);
            String request_send_to_ = "Request sent for approval";
            return new GenericResponseDTO("00", HttpStatus.OK, request_send_to_, save);
        }

        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Failed to create request", null);

    }

    @Override
    public List<ApprovalRequestDTO> getAllApprovalRequestByPhone(String phoneNumber) {

        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        Profile profile = profileService.findByPhoneNumber(phoneNumber);
        if (profile != null) {
            ApprovalGroup approvalGroup = profile.getApprovalGroup();

            if (approvalGroup != null) {

                return approvalRequestRepository
                    .findAllByApprover(approvalGroup)
                    .stream()
                    .map(approvalRequestMapper::toDto)
                    .collect(Collectors.toList());
            }
        }

        return new ArrayList<>();
    }

    @Override
    public ApprovalRequestDetailsDTO getApprovalRequestDetails(String requestRef, String requestType) {
        requestRef = requestRef.trim();
        requestType = requestType.trim();
        ApprovalRequestDetailsDTO approvalRequestDetailsDTO = new ApprovalRequestDetailsDTO();
        Optional<ApprovalRequestDTO> optionalApprovalRequest = approvalRequestRepository
            .findAllByRequestRefAndRequestTypeCode(requestRef, requestType)
            .map(approvalRequestMapper::toDto);

        optionalApprovalRequest.ifPresent(approvalRequestDTO -> {
            approvalRequestDetailsDTO.setApprovalRequest(approvalRequestDTO);
            String requestTypeCode = approvalRequestDTO.getRequestTypeCode();
            String requestReference = approvalRequestDTO.getRequestRef();

            if ("DC".equalsIgnoreCase(requestTypeCode) || "FC".equalsIgnoreCase(requestTypeCode) || "UC".equalsIgnoreCase(requestTypeCode)){
                Profile profile = profileService.findByPhoneNumber(utility.formatPhoneNumber(requestReference));
                approvalRequestDetailsDTO.setData(profile);
            }
            if ("BA".equalsIgnoreCase(requestTypeCode) || "PD".equalsIgnoreCase(requestTypeCode)){
                WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(requestReference);
                WalletAccountDTO walletAccountDTO = walletAccountMapper.toDto(walletAccount);
                approvalRequestDetailsDTO.setData(walletAccountDTO);
            }
            if ("HF".equalsIgnoreCase(requestTypeCode) || "RT".equalsIgnoreCase(requestTypeCode)){
                Optional<Journal> optionalJournal = journalService.findByReference(requestReference);
                optionalJournal.ifPresent(approvalRequestDetailsDTO::setData);
            }

        });
        return approvalRequestDetailsDTO;

    }

    @Override
    public GenericResponseDTO approveRequest(ApproveRequestDTO approveRequestDTO) {

        SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByLogin).ifPresent(user -> {
            this.theUser = user;
        });

        if (theUser == null) {
            return new GenericResponseDTO("401", HttpStatus.BAD_REQUEST, "Invalid Login User, please login again", null);
        }
        String login = theUser.getLogin();
        Profile loginProfile = profileService.findByPhoneNumber(login);

        String requestId = approveRequestDTO.getRequestId().trim();
        ApprovalRequest approvalRequest = findByRequestId(requestId);
        if (approvalRequest != null) {

            Set<Profile> profiles = approvalRequest.getApprover().getProfiles();

            if (!profiles.contains(loginProfile)) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "You do not have access to approve this request", null);
            }

            approvalRequest.setComment(approvalRequest.getComment() + "#" + approveRequestDTO.getAdditionalComment());
            String action = approveRequestDTO.getAction();
            try {
                RequestStatus requestStatus = RequestStatus.valueOf(action.toUpperCase());
                approvalRequest.setStatus(requestStatus.getName());

                String requestRef = approvalRequest.getRequestRef().trim();

                if (RequestStatus.APPROVED.equals(requestStatus)) {
                    GenericResponseDTO genericResponseDTO;

                    switch (approvalRequest.getRequestTypeCode().trim()) {
                        case "DC":
                            //Deactivate Customer'
                            genericResponseDTO = userService.changeUserStatus(requestRef, UserStatus.DEACTIVATE_CUSTOMER);
                            break;

                        case "FC":
                            //Flag Customer
                            genericResponseDTO = userService.changeUserStatus(requestRef, UserStatus.FLAG_CUSTOMER);
                            break;

                        case "UC":
                            //Update Customer
                            genericResponseDTO = userService.changeUserStatus(requestRef, UserStatus.UPDATE_CUSTOMER);
                            break;

                        case "BA" :
                            //Block Account
                            GenericResponseDTO response = walletAccountService.checkAccountStatus(requestRef);
                            try {
                                AccountStatus accountStatus = AccountStatus.valueOf((String)response.getData());
                                if (AccountStatus.ACTIVE.equals(accountStatus)) {
                                    genericResponseDTO = walletAccountService.changeAccountStatus(requestRef, AccountStatus.SUSPENDED.name());
                                }else {
                                    genericResponseDTO = walletAccountService.changeAccountStatus(requestRef, AccountStatus.ACTIVE.name());
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                                genericResponseDTO = new GenericResponseDTO("99", HttpStatus.BAD_REQUEST,"Invalid account Status", null);
                            }
                            break;

                        case "HF" :
                            //Hold Funds
                            genericResponseDTO = journalService.changeTransactionStatus(requestRef, TransactionStatus.SUSPENDED);
                            break;

                        case "PD" :
                            //Post No Debit
                            genericResponseDTO = walletAccountService.changeAccountStatus(requestRef, AccountStatus.DEBIT_ON_HOLD.name());
                            break;

                        case "RT" :
//                            Reverse Transaction
                            ReverseDTO reverseDTO = new ReverseDTO();
                            reverseDTO.setStatus("Incomplete");
                            if (approvalRequest.isCompleted()){
                                reverseDTO.setStatus("Completed");
                            }
                            reverseDTO.setPointOfFailure(approvalRequest.getPointOfFailure());
                            journalService.reverseTransaction(reverseDTO);
                            genericResponseDTO = new GenericResponseDTO("00", HttpStatus.OK, "Approval Successful", null);
                            break;

                        default:
                            genericResponseDTO = new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid requestType code", null);
                    }
                    if (genericResponseDTO == null) {
                        genericResponseDTO = new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null);
                    }

                    if (HttpStatus.OK.equals(genericResponseDTO.getStatus())){
                        approvalRequestRepository.save(approvalRequest);
                    }

                    return genericResponseDTO;
                } else {
                    approvalRequestRepository.save(approvalRequest);
                    return new GenericResponseDTO("00", HttpStatus.OK, "Request Rejected Successful", null);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Request, Please try again", null);
    }

    @Override
    public ApprovalRequest findByRequestId(String requestId) {
        return approvalRequestRepository.findAllByRequestId(requestId);
    }

    private ApprovalRequestDTO saveImage(ApprovalRequestDTO approvalRequestDTO, String encodedString, String photoName, String outputFileName) {
        try {
            log.debug("ABOUT TO DECODE");
            byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
            log.debug("AFTER DECODE");

//            decodedBytes = utility.resizeImage(decodedBytes);

            File file = new File(outputFileName);
            log.debug("Path name = " + outputFileName);
            log.debug("File absolute path = " + file.getAbsolutePath());
            FileUtils.writeByteArrayToFile(file, decodedBytes);

            approvalRequestDTO.setSupportingDoc(photoName);
            return approvalRequestDTO;

        } catch (IOException e) {
            e.printStackTrace();
            log.debug(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
