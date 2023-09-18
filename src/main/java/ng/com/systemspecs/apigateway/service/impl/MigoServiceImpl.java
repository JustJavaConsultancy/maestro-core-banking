package ng.com.systemspecs.apigateway.service.impl;

import com.google.gson.Gson;
import ng.com.systemspecs.apigateway.service.MigoService;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.systemspecs.apigateway.util.MigoUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@Transactional
public class MigoServiceImpl implements MigoService {

    private final MigoUtils migoUtils;

    public MigoServiceImpl(MigoUtils migoUtils) {
        this.migoUtils = migoUtils;
    }

    @Override
    public GenericResponseDTO getLoan(HashMap<String, String> queryParams){

        if (queryParams == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "QueryParams not found", null);
        }
        try{

        GenericResponseDTO response = migoUtils.requestWithQuaryParams("/v1/offers?clientNo={clientNo}", queryParams, HttpMethod.GET);

        if (response == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response", null);
        }
            String genericResponseDTOData = (String) response.getData();

            MigoGetLoanResponseDTO[] migoGetLoanResponseDTOS = new Gson().fromJson(genericResponseDTOData, MigoGetLoanResponseDTO[].class);

            return new GenericResponseDTO("00", HttpStatus.OK, "Successful", migoGetLoanResponseDTOS);

        }
        catch (Exception e) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
        }
    }

    @Override
    public GenericResponseDTO getLoanRealTimeOffers(MigoLoanRealTimeOffersRequestDTO migoLoanRealTimeOffersRequestDTO){

        if (migoLoanRealTimeOffersRequestDTO == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "QueryParams not found", null);
        }

        GenericResponseDTO response = migoUtils.requestWithPayload("/v1/real-time-offers", migoLoanRealTimeOffersRequestDTO, HttpMethod.POST);

        if (response == null){
            return new GenericResponseDTO("00", HttpStatus.BAD_REQUEST, "No response", null);
        }

        return response;
    }

    @Override
    public GenericResponseDTO selectOffer(MigoSelectOfferRequestDTO migoSelectOfferRequestDTO){


        try {
            GenericResponseDTO response = migoUtils.requestWithPayload("/v1/offers", migoSelectOfferRequestDTO, HttpMethod.PUT);

            if (migoSelectOfferRequestDTO == null) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "QueryParams not found", null);
            }

            if (response == null) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response", null);
            }

            String genericResponseDTOData = (String) response.getData();

//            MigoSelectOfferResponseDTO migoSelectOfferResponseDTOS = new Gson().fromJson(genericResponseDTOData, MigoSelectOfferResponseDTO.class);

            return new GenericResponseDTO("00", HttpStatus.OK, "Successful", genericResponseDTOData);
        }

        catch (Exception e) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());

        }
    }

    @Override
    public GenericResponseDTO getAccount(HashMap<String, String> queryParams){

        if (queryParams == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "QueryParams not found", null);
        }

        try {
            GenericResponseDTO response = migoUtils.requestWithQuaryParams("/v1/accounts?clientNo={clientNo}&lastUsed={lastUsed}", queryParams, HttpMethod.GET);

            if (response == null) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response", null);
            }

            String genericResponseDTOData = (String) response.getData();

            MigoGetAccountResponseDTO[] migoGetAccountResponseDTO = new Gson().fromJson(genericResponseDTOData, MigoGetAccountResponseDTO[].class);

            return new GenericResponseDTO("00", HttpStatus.OK, "Successful", migoGetAccountResponseDTO);

        }
        catch (Exception e) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
        }
    }

    @Override
    public GenericResponseDTO getBankList(){

        try {

            GenericResponseDTO response = migoUtils.requestWithoutPayload("/v1/banks", HttpMethod.GET);

            if (response == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response", null);
            }

            String genericResponseDTOData = (String) response.getData();

            MigoGetBanksResponseDTO[] migoGetBanksResponseDTO = new Gson().fromJson(genericResponseDTOData, MigoGetBanksResponseDTO[].class);

            return new GenericResponseDTO("00", HttpStatus.OK, "Successful", migoGetBanksResponseDTO);

        }catch (Exception e){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());
        }


    }

    @Override
    public GenericResponseDTO submitApplication(MigoSubmitApplicationRequestDTO migoSubmitApplicationRequestDTO){

        if (migoSubmitApplicationRequestDTO == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Payload not found", null);
        }

        try{
            GenericResponseDTO response = migoUtils.requestWithPayload("/v1/applications", migoSubmitApplicationRequestDTO, HttpMethod.POST);

            if (response == null){
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response", null);
            }

            String genericResponseDTOData = (String) response.getData();

            MigoSubmitApplicationResponseDTO migoSubmitApplicationResponseDTOS = new Gson().fromJson(genericResponseDTOData, MigoSubmitApplicationResponseDTO.class);

            return new GenericResponseDTO("00", HttpStatus.OK, "Successful", migoSubmitApplicationResponseDTOS);
        }
        catch (Exception e){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());

        }
    }

    @Override
    public GenericResponseDTO termsAndConditions(){

        GenericResponseDTO response = migoUtils.requestWithoutPayload("/v1/terms", HttpMethod.GET);

        if (response == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response", null);
        }

        return response;
    }

    @Override
    public GenericResponseDTO applicationCheckout(MigoSubmitApplicationRequestDTO migoSubmitApplicationRequestDTO){

        GenericResponseDTO response = migoUtils.requestWithPayload("/v1/applications", migoSubmitApplicationRequestDTO, HttpMethod.POST);

        if (response == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response", null);
        }

        return response;
    }

    @Override
    public GenericResponseDTO getActiveLoan(HashMap<String, String> queryParams){

        if (queryParams == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "QueryParams not found", null);
        }

        GenericResponseDTO response = migoUtils.requestWithQuaryParams("/v1/loans?clientNo={clientNo}", queryParams, HttpMethod.GET);

        if (response == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response", null);
        }

        return response;

    }

    @Override
    public GenericResponseDTO postPayment(MigoPostPaymentsRequestDTO migoPostPaymentsRequestDTO){


        try {


            GenericResponseDTO response = migoUtils.requestWithPayload("/v2/payments", migoPostPaymentsRequestDTO, HttpMethod.POST);

            if (migoPostPaymentsRequestDTO == null) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "QueryParams not found", null);
            }

            if (response == null) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "No response", null);
            }

            String genericResponseDTOData = (String) response.getData();

            MigoPostPaymentResponseDTO migoPostPaymentResponseDTOS = new Gson().fromJson(genericResponseDTOData, MigoPostPaymentResponseDTO.class);

            return new GenericResponseDTO("00", HttpStatus.OK, "Successful", migoPostPaymentResponseDTOS);

        }

        catch (Exception e) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), e.getStackTrace());

        }
    }

}
