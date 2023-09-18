package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.VehicleInsuranceRequestRepository;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.mapper.VehicleInsuranceRequestMapper;
import ng.com.justjava.corebanking.client.MutualVehicleInsuranceRestClient;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.domain.VehicleInsuranceRequest;
import ng.com.justjava.corebanking.domain.WalletAccount;
import ng.com.justjava.corebanking.service.InsuranceService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.VehicleInsuranceRequestService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link VehicleInsuranceRequest}.
 */
@Service
@Transactional
public class VehicleInsuranceRequestServiceImpl implements VehicleInsuranceRequestService {

    private final Logger log = LoggerFactory.getLogger(VehicleInsuranceRequestServiceImpl.class);

    @Value("${app.mutual-insurance.secret-key}")
    public static String MUTUAL_INSURANCE_SECRET_KEY;

    @Value("${app.percentage.vat}")
    private double vatFeePercentage;

    private final VehicleInsuranceRequestRepository vehicleInsuranceRequestRepository;
    private final Utility utility;

    private final VehicleInsuranceRequestMapper vehicleInsuranceRequestMapper;

    private final ProfileService profileService;

    private final WalletAccountService walletAccountService;

    private final MutualVehicleInsuranceRestClient mutualVehicleInsuranceRestClient;

    private final InsuranceService insuranceService;

    public VehicleInsuranceRequestServiceImpl(VehicleInsuranceRequestRepository vehicleInsuranceRequestRepository,
                                              Utility utility, VehicleInsuranceRequestMapper vehicleInsuranceRequestMapper, ProfileService profileService,
                                              WalletAccountService walletAccountService,
                                              MutualVehicleInsuranceRestClient mutualVehicleInsuranceRestClient, InsuranceService insuranceService) {
        this.vehicleInsuranceRequestRepository = vehicleInsuranceRequestRepository;
        this.utility = utility;
        this.vehicleInsuranceRequestMapper = vehicleInsuranceRequestMapper;
        this.profileService = profileService;
        this.mutualVehicleInsuranceRestClient = mutualVehicleInsuranceRestClient;
        this.walletAccountService = walletAccountService;
        this.insuranceService = insuranceService;
    }

    @Override
    public VehicleInsuranceRequestDTO save(VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO) {
        log.debug("Request to save VehicleInsuranceRequest : {}", vehicleInsuranceRequestDTO);
        VehicleInsuranceRequest vehicleInsuranceRequest = vehicleInsuranceRequestMapper.toEntity(vehicleInsuranceRequestDTO);
        vehicleInsuranceRequest = vehicleInsuranceRequestRepository.save(vehicleInsuranceRequest);
        return vehicleInsuranceRequestMapper.toDto(vehicleInsuranceRequest);
    }

    public Double getChargeByVehicleType(String field) {
    	 switch (field) {
	         case "BUS":
	             return 7500.00;
             case "JEEP":
	             return 5000.00;
	         case "GEN. CARTAGE/TRUCK TRUCKS":
	             return 25000.00;
	         case "MID TRUCKS":
	             return 25000.00;
	         case "PICK UP/MINI TRUCK":
	             return 7500.00;
	         case "MOTOR/TRI CYCLE":
	             return 7500.00;
	         case "SALOON CAR":
	             return 5000.00;
	         case "VAN":
	             return 7500.00;
	         case "WAGON-MINI BUS":
	             return 7500.00;
	         default:
	             return 7500.00;
     }
    }

    public GenericResponseDTO createNewVehicleInsurance(VehicleInsuranceRequestDTO vehicleInsuranceRequestDTO, HttpSession session) {
    	Optional<ProfileDTO> theUser = profileService.findByUserIsCurrentUser();
    	 if (theUser.isPresent()) {
    		 Double userBalance = 0.00;
             //List<WalletAccountDTO> userWallets = walletAccountService.findByUserIsCurrentUserAndScheme(session);
             WalletAccount userWallet = walletAccountService.findOneByAccountNumber(vehicleInsuranceRequestDTO.getAccountNumber());
             if (!userWallet.getActualBalance().isEmpty()) {
                 userBalance = Double.parseDouble(userWallet.getActualBalance());
                 System.out.println();
                 System.out.println(userWallet + "============The user wallet==============");
                 System.out.println();
             }
             Double amountToChargeWallet = getChargeByVehicleType(vehicleInsuranceRequestDTO.getVehicleType());

             double InsuranceCharge = 25.00;
             double vatFee = InsuranceCharge * vatFeePercentage;

             if (amountToChargeWallet + InsuranceCharge + vatFee > userBalance) {//372500
                 return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Insufficient funds");
             }

             LocalDate dateNow = LocalDate.now();
             LocalDate dateOneYear = dateNow.plusYears(1);
             DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
             String formattedNow = dtf.format(dateNow);
             String formattednextYear = dateOneYear.format(dtf);
             System.out.println();
             System.out.println(formattedNow + " ========now=========next year " + formattednextYear);
             System.out.println();
             //fda7925908a9f751cfd7c23bf4895ade628a07465abdcc9ec0b8a53ec944b969
             InsureVihicleRequestDTO insureVehicleRequest = new InsureVihicleRequestDTO();
             insureVehicleRequest.setSecretKey("3a26b1da66b8e8ec0f5d328ddcf1f012ce68b083cddb4e52497975cbf5b779e1");
             insureVehicleRequest.setOperation(vehicleInsuranceRequestDTO.getOperation());
             ProfileDTO profileDTO = theUser.get();
             insureVehicleRequest.setInsuredName(vehicleInsuranceRequestDTO.getFirstName() + " " + vehicleInsuranceRequestDTO.getLastName());
             insureVehicleRequest.setGender(vehicleInsuranceRequestDTO.getGender().toUpperCase());
             insureVehicleRequest.setPhone(vehicleInsuranceRequestDTO.getPhoneNumber());
             User user = profileDTO.getUser();
             insureVehicleRequest.setEmail(vehicleInsuranceRequestDTO.getEmail());
             insureVehicleRequest.setAddress(vehicleInsuranceRequestDTO.getAddress());
             insureVehicleRequest.setBirthDate(vehicleInsuranceRequestDTO.getDateOfBirth());
             insureVehicleRequest.setOccupation(vehicleInsuranceRequestDTO.getOccupation());
             insureVehicleRequest.setSector(vehicleInsuranceRequestDTO.getSector());
             insureVehicleRequest.setState(vehicleInsuranceRequestDTO.getState());
             insureVehicleRequest.setLga(vehicleInsuranceRequestDTO.getLga());
             System.out.println();
             //System.out.println(vehicleInsuranceRequestDTO+"======================>I am the result of the API<============================");
             System.out.println();
             insureVehicleRequest.setIdNumber(vehicleInsuranceRequestDTO.getIdNumber());
             insureVehicleRequest.setIdType(vehicleInsuranceRequestDTO.getIdType());
             insureVehicleRequest.setIdNumber(vehicleInsuranceRequestDTO.getIdNumber());
			insureVehicleRequest.setVehicleType(vehicleInsuranceRequestDTO.getVehicleType());
			insureVehicleRequest.setRegistrationNo(vehicleInsuranceRequestDTO.getRegistrationNo());
			insureVehicleRequest.setVehMake(vehicleInsuranceRequestDTO.getVehMake());
			insureVehicleRequest.setVehModel(vehicleInsuranceRequestDTO.getVehModel());
			insureVehicleRequest.setVehYear(vehicleInsuranceRequestDTO.getVehYear().toString());
			insureVehicleRequest.setVehModel(vehicleInsuranceRequestDTO.getVehModel());
			insureVehicleRequest.setRegistrationStart(formattedNow);
			insureVehicleRequest.setMileage(vehicleInsuranceRequestDTO.getMileage());
             insureVehicleRequest.setVehColor(vehicleInsuranceRequestDTO.getVehColor());
             insureVehicleRequest.setChassisNo(vehicleInsuranceRequestDTO.getChassisNo());
             insureVehicleRequest.setEngineNo(vehicleInsuranceRequestDTO.getEngineNo());
             insureVehicleRequest.setEngineCapacity(vehicleInsuranceRequestDTO.getEngineCapacity());
             insureVehicleRequest.setSeatCapacity(vehicleInsuranceRequestDTO.getSeatCapacity());

             InsuranceDTO debitUserDTO = new InsuranceDTO();
             // debitUserDTO.setAccountNumber(userWallets.get(0).getAccountNumber());
             debitUserDTO.setAccountNumber(vehicleInsuranceRequestDTO.getAccountNumber());
             debitUserDTO.setAmount(amountToChargeWallet);//372500.00
             debitUserDTO.setCharges(InsuranceCharge);
             debitUserDTO.setLenderId(vehicleInsuranceRequestDTO.getLenderId());
             System.out.println();
             System.out.println("This is working====================================================>" + debitUserDTO);
             System.out.println();

             ResponseEntity<InsureVihicleResponseDTO> result = mutualVehicleInsuranceRestClient.insureVehicle(insureVehicleRequest);
             if (HttpStatus.OK.equals(result.getStatusCode())) {
                 System.out.println("This is working=====>");
                 if (result.getBody().isStatus()) {

                     insuranceService.processInsurance(debitUserDTO, session);// debit the customers wallet

                     InsureVihicleResponseDTO contentResult = result.getBody();
                     vehicleInsuranceRequestDTO.setCertificateNo(contentResult.getData().getCertificateNo());
                     vehicleInsuranceRequestDTO.setCertificateURL(contentResult.getData().getCertificateUrl());
                     vehicleInsuranceRequestDTO.setPolicyNo(contentResult.getData().getPolicyNo());
                     vehicleInsuranceRequestDTO.setBalance(contentResult.getData().getBalance());
                     vehicleInsuranceRequestDTO.setExpiryDate(formattednextYear);
                     vehicleInsuranceRequestDTO.setRegistrationStart(formattedNow);
                     vehicleInsuranceRequestDTO.setProfileId(profileDTO.getId());
                     VehicleInsuranceRequest vehicleInsuranceRequest = vehicleInsuranceRequestMapper.toEntity(vehicleInsuranceRequestDTO);
                     vehicleInsuranceRequest = vehicleInsuranceRequestRepository.save(vehicleInsuranceRequest);
                     String email = user.getEmail();
                     if (utility.checkStringIsValid(email)) {
                         String msg = "Dear " + user.getFirstName() +
                             "<br/>" +
                             "<br/>" +
                             "<br/>" +
                             "Your request to for vehicle insurance covered by <b>Mutual Benefits</b> was successful." +
                             "<br/>" +
                             "<br/>" +
                             "Further details will be sent shortly by the provider." +
                             "<br/>" +
                             "<br/>" +
                             "<br/>" +
                             "Thank you for choosing us!";

                         utility.sendEmail(email, "Successful Insurance ", msg);
                     }

                     return new GenericResponseDTO("00", HttpStatus.OK, "success", vehicleInsuranceRequest);
                 }
             }

		  /*   //Failure email message
            //Todo replace email
             String email = user.getEmail();
             if (utility.checkStringIsValid(email)) {
                 String msg = "Dear " + user.getFirstName() +
                     "<br/>" +
                     "<br/>" +
                     "<br/>" +
                     "Your request to for vehicle insurance covered by <b>Mutual Benefits</b> was successful." + hfjjfjrj +
                     "<br/>" +
                     "<br/>" +
                     "Further details will be sent shortly by the provider." +
                     "<br/>" +
                     "<br/>" +
                     "<br/>" +
                     "Thank you for choosing us!";

                 utility.sendEmail(email, "Successful Insurance ", msg);
             }
*/

             return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, result.getBody().getMessage(), result.getBody());
         }
         return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Profile not found");
    }


    @Override
    @Transactional(readOnly = true)
    public GenericResponseDTO findByLoggedInUser() {
		Optional<ProfileDTO> theUser = profileService.findByUserIsCurrentUser();
		if (theUser.isPresent()) {
			 log.debug("Request to get all VehicleInsuranceRequests");
			 Collection<VehicleInsuranceRequestDTO> vehicleInsurance =  vehicleInsuranceRequestRepository.findAllByProfileIdOrderByIdDesc(theUser.get().getId())
					 .stream()
			            .map(vehicleInsuranceRequestMapper::toDto)
			            .collect(Collectors.toCollection(LinkedList::new));
			 return new GenericResponseDTO("00", HttpStatus.OK, "success", vehicleInsurance);

		}
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Current login user is null", null);
    }


    @Override
    @Transactional(readOnly = true)
    public List<VehicleInsuranceRequestDTO> findAll() {
        log.debug("Request to get all VehicleInsuranceRequests");
        return vehicleInsuranceRequestRepository.findAll().stream()
            .map(vehicleInsuranceRequestMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<VehicleInsuranceRequestDTO> findOne(Long id) {
        log.debug("Request to get VehicleInsuranceRequest : {}", id);
        return vehicleInsuranceRequestRepository.findById(id)
            .map(vehicleInsuranceRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete VehicleInsuranceRequest : {}", id);
        vehicleInsuranceRequestRepository.deleteById(id);
    }


}
