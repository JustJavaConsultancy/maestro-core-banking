package ng.com.justjava.corebanking.client;

import feign.Headers;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


//@FeignClient(name = "external-service-bvn", url = "https://login.remita.net/remita/exapp/api/v1")
@FeignClient(name = "external-service-bvn", url = "https://login.remita.net/remita/exapp/api/v1")
public interface ExternalRESTClient2 {


    @RequestMapping(value = "/ref/api/ext/referencedata/bvnverify", method = RequestMethod.POST)
    @Headers("Content-Type: application/json")
    Object validateBvn(@RequestHeader Map<String,String> headers, @RequestParam("bvn")  String  bvn);


    @RequestMapping(value = "/wallet/services/core-banking/v1/account/open", method = RequestMethod.POST)
    NewWalletAccountResponse getNewWalletAccount(@RequestBody NewWalletAccountDTO newWalletAccountDTO);


    @RequestMapping(value = "/wallet/services/core-banking/v1/account/addNew", method = RequestMethod.POST)
    NewWalletAccountResponse  getAdditionalWalletAccount(@RequestBody AdditionalWalletAccountDTO additionalWalletAccountDTO);


    @RequestMapping(value = "/wallet/services/core-banking/transaction/receive", method = RequestMethod.POST)
    Object  bankToWalletTransfer(@RequestBody BankToWalletDTO bankToWalletDTO);


    @RequestMapping(value = "/wallet/services/core-banking/transaction/singletransfer", method = RequestMethod.POST)
    Object  transferWithinBank(@RequestBody TransferWithinBankDTO transferWithinBankDTO);

    @RequestMapping(value = "/wallet/services/core-banking/transaction/singleInterbankTransfer", method = RequestMethod.POST)
    Object  transferToAnotherBank(@RequestBody  TransferToAnotherBankDTO  transferToAnotherBankDTO);


}
