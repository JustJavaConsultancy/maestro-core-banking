package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.domain.BillerRecurring;
import ng.com.justjava.corebanking.domain.User;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PayBillerService {

    List<PayBiller> getBillers();

    List<BillerCategoryNew> getBillerCategories();

    List<PayBiller> getBillerCategoryId(String categoryId);

    List<BillerProduct> getBillerProducts(String  billerId);

    GenericResponseDTO getRRRDetails(Long rrr);

    GenericResponseDTO validateAndPayBiller(InitiateBillerTransactionDTO initiateBillerTransactionDTO);

    @NotNull GenericResponseDTO payRRR(BillerPayRrrDTO payRrrDTO);

    GenericResponseDTO checkBalanceIsNotSufficient(String sourceAccountNo, double amount, double bonusAmount, Object obj, String specificChannel);

    GenericResponseDTO payRecurring(BillerRecurring billerRecurring);

    GenericResponseDTO scheduleRecurring(InitiateBillerTransactionDTO initiateBillerTransactionDTO, User user);

    boolean isValidCustomer(BillerValidateCustomer billerValidateCustomer);
}
