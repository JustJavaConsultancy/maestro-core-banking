package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.AccessRight;
import ng.com.systemspecs.apigateway.service.dto.*;

import javax.servlet.http.HttpSession;

/**
 * Service Interface for managing {@link AccessRight}.
 */
public interface PolarisVulteService {
    GenericResponseDTO getBalance(String accountNumber, String schemeId) throws Exception;

    GenericResponseDTO getStatement(String accountNumber, String startDate, String endDate, String schemeId) throws Exception;

    GenericResponseDTO fundTransfer(PolarisVulteFundTransferDTO transferDTO) throws Exception;

    GenericResponseDTO disburse(PolarisVulteFundTransferDTO transferDTO) throws Exception;

    GenericResponseDTO collect(PolarisVulteFundTransferDTO transferDTO) throws Exception;

    GenericResponseDTO bvnLookupMin(ValidateBVNDTO validateBVNDTO, String schemeId) throws Exception;

    GenericResponseDTO bvnLookupMid(ValidateBVNDTO validateBVNDTO, String schemeId) throws Exception;

    GenericResponseDTO bvnLookupMax(ValidateBVNDTO validateBVNDTO, String schemeId) throws Exception;

    GenericResponseDTO lookupAccountMax(PolarisVulteFundTransferDTO lookupAccountDTO) throws Exception;

    GenericResponseDTO lookupAccountMid(PolarisVulteFundTransferDTO lookupAccountDTO) throws Exception;

    GenericResponseDTO lookupAccountMin(PolarisVulteFundTransferDTO lookupAccountDTO) throws Exception;

    GenericResponseDTO getAccountsMin(PolarisVulteFundTransferDTO lookupAccountDTO) throws Exception;

    GenericResponseDTO getAccountsMid(PolarisVulteFundTransferDTO lookupAccountDTO) throws Exception;

    GenericResponseDTO getAccountsMax(PolarisVulteFundTransferDTO lookupAccountDTO) throws Exception;

    GenericResponseDTO lookUpNuban(PolarisVulteFundTransferDTO lookupAccountDTO) throws Exception;

    GenericResponseDTO getBanks(String schemeId, HttpSession session) throws Exception;

    GenericResponseDTO getBranch(String schemeId, HttpSession session) throws Exception;

    GenericResponseDTO openAccount(PolarisVulteOpenAccountDTO accountDTO) throws Exception;

    GenericResponseDTO openCollectionAccount(PolarisCollectionAccountRequestDTO polarisCollectionAccountRequestDTO) throws Exception;

    GenericResponseDTO getAuth() throws Exception;

    GenericResponseDTO openVirtualAccount(PolarisVulteOpenAccountDTO accountDTO) throws Exception;

    GenericResponseDTO openWallet(PolarisVulteOpenAccountDTO accountDTO) throws Exception;

    GenericResponseDTO webHook(PolarisVulteWebHookRequest request) throws Exception;

    GenericResponseDTO validateTransaction(String otp, String requestType, String transactionRef, String schemeId) throws Exception;

    GenericResponseDTO queryTransaction(String requestType, String transactionRef, String schemeId) throws Exception;
}
