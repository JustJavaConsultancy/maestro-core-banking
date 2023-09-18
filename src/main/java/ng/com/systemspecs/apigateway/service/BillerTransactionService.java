package ng.com.systemspecs.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.systemspecs.remitabillinggateway.billers.GetBillerResponse;
import ng.com.systemspecs.remitabillinggateway.billers.GetBillerResponseData;
import ng.com.systemspecs.remitabillinggateway.customfields.GetCustomFieldResponse;
import ng.com.systemspecs.remitabillinggateway.generaterrr.GenerateResponse;
import ng.com.systemspecs.remitabillinggateway.paymentstatus.GetTransactionStatusResponse;
import ng.com.systemspecs.remitabillinggateway.rrrdetails.GetRRRDetailsResponse;
import ng.com.systemspecs.remitabillinggateway.service.RemitaBillingGatewayService;
import ng.com.systemspecs.remitabillinggateway.servicetypes.GetServiceResponse;
import ng.com.systemspecs.remitabillinggateway.servicetypes.GetServiceResponseData;
import ng.com.systemspecs.remitabillinggateway.validate.ValidateRequest;
import ng.com.systemspecs.remitabillinggateway.validate.ValidateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.BillerTransaction}.
 */
public interface BillerTransactionService {

    /**
     * Save a billerTransaction.
     *
     * @param billerTransactionDTO the entity to save.
     * @return the persisted entity.
     */
    BillerTransactionDTO save(BillerTransactionDTO billerTransactionDTO);

    /**
     * Get all the billerTransactions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BillerTransactionDTO> findAll(Pageable pageable);


    /**
     * Get the "id" billerTransaction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillerTransactionDTO> findOne(Long id);

    /**
     * Delete the "id" billerTransaction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    RemitaBillingGatewayService getRemitaBillingGatewayService(String transId);

    RemitaBillingGatewayService notifyBillingGatewayService(String transId);

    GetRRRDetailsResponse getRRRDetails(String rrr, String transId);

    ValidateResponse validate(ValidateRequest validateRequest, String transId);

    GenericResponseDTO validateAndPayBiller(ValidateRequest validateRequest, String sourceAccountNo, String transRef, String specificChannel, String narration, String agentRef, double bonusAmount, boolean redeemBonus);

    GenerateResponse generateRRR(ValidateRequest validateRequest, String transId);


    GenericResponseDTO payRRR(PayRRRDTO payRRRDTO);

    GetTransactionStatusResponse getTransactionStatus(String transactionId);

    GenericResponseDTO buyAirtime(BuyAirtimeDTO buyAirtimeDTO);

    GenericResponseDTO buyData(BuyDataDTO buyDataDTO);

    List<GetBillerResponseData> getBillers();

    List<GetServiceResponseData> getService(String billerId);

    GetCustomFieldResponse getCustomField(String serviceTypeId) throws Exception;

    GenericResponseDTO payForUtils(UtilDTO utilDTO);

    GenericResponseDTO validateMeter(UtilDTO utilDTO, HttpSession session) throws JsonProcessingException;

    ValidateResponse validateRequest(ValidateRequest validateRequest, HttpSession session, String transId) throws JsonProcessingException;

    GetBillerResponse getAllBillersFromIpg();

    GetServiceResponse getServicesFromIpg(String serviceTypeId);

    GenericResponseDTO payElectricity(UtilDTO utilDTO, ValidateRequest validateRequest);

    GenericResponseDTO validateCheck(ValidateRequest validateRequest);

    boolean billNotification(FundDTO fundDTO);

    GenericResponseDTO buyAirtimeForCustomer(AgentAirtimeDTO agentAirtimeDTO);

    GenericResponseDTO buyDataForCustomer(AgentAirtimeDTO agentAirtimeDTO);

}
