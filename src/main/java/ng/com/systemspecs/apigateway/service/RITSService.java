package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.remitarits.accountenquiry.AccountEnqiryRequest;
import ng.com.systemspecs.remitarits.accountenquiry.AccountEnquiryResponse;
import ng.com.systemspecs.remitarits.bankenquiry.GetActiveBankResponse;
import ng.com.systemspecs.remitarits.bulkpayment.BulkPaymentRequest;
import ng.com.systemspecs.remitarits.bulkpayment.BulkPaymentResponse;
import ng.com.systemspecs.remitarits.bulkpaymentstatus.BulkPaymentStatusRequest;
import ng.com.systemspecs.remitarits.bulkpaymentstatus.BulkPaymentStatusResponse;
import ng.com.systemspecs.remitarits.service.RemitaRITSService;
import ng.com.systemspecs.remitarits.singlepayment.SinglePaymentRequest;
import ng.com.systemspecs.remitarits.singlepayment.SinglePaymentResponse;
import ng.com.systemspecs.remitarits.singlepaymentstatus.PaymentStatusRequest;
import ng.com.systemspecs.remitarits.singlepaymentstatus.PaymentStatusResponse;

public interface RITSService {

    RemitaRITSService getRemitaRITSService(String requestId);


    SinglePaymentResponse singlePayment(SinglePaymentRequest singlePaymentRequest);

    BulkPaymentResponse postBulkPayment(BulkPaymentRequest bulkPaymentRequest);

    PaymentStatusResponse singlePaymentStatus(PaymentStatusRequest paymentStatusRequest);


    BulkPaymentStatusResponse bulkPaymentStatus(BulkPaymentStatusRequest bulkPaymentStatusRequest);


    AccountEnquiryResponse getAccountEnquiry(AccountEnqiryRequest accountEnqiryRequest);


    GetActiveBankResponse getActiveBanks();


}
