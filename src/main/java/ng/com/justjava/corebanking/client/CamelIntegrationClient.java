package ng.com.justjava.corebanking.client;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.exception.GenericException;
import ng.com.systemspecs.apigateway.service.dto.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

@Component
@SuppressWarnings("unused")
public class CamelIntegrationClient {

    @Value("${app.pay-biller.baseUrl}")
    private String BASE_URL;

    public static final String GET_BILLERS = "/api/remita/billers/";
    public static final String GET_BILLERS_CATEGORIES = "/api/remita/biller/categories";
    public static final String GET_BILLERS_CATEGORIES_BY_ID = "/api/remita/biller/category/{categoryId}";
    public static final String GET_BILLERS_PRODUCTS = "/api/remita/billers/products/{billerId}";
    public static final String VALIDATE_CUSTOMER = "/api/remita/biller/transaction/validate/customer";
    public static final String INITIATE_TRANSACTION = "/api/remita/biller/initiate/transaction";
    public static final String BILL_PAYMENT_NOTIFICATION = "/api/remita/bill/payment/notification";
    public static final String GET_BILL_PAYMENT_STATUS = "/api/remita/bill/payment/status/{transactionRef}";
    public static final String GENERATE_RRR = "/api/remita/biller/transaction/initiate";
    public static final String LOOKUP_RRR = "/api/remita/lookup/rrr/";

    public List<PayBiller> getBiller() {

        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> httpResponse = Unirest.get(BASE_URL + GET_BILLERS)
                .asString();

            out.printf("Get billers response: %s\n", httpResponse);
            out.printf("%nGet billers response data: %s\n\n", httpResponse.getBody());
            out.printf("Get billers response status: %s\n", httpResponse.getStatus());
            CamelResponse camelResponse = new Gson().fromJson(httpResponse.getBody(), CamelResponse.class);

            if (httpResponse.getStatus() == 200 && camelResponse.getStatusCode() == 200) {
                JSONObject jsonObject = new JSONObject(httpResponse.getBody());
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                List<PayBiller> payBillers = new ArrayList<>();

                for (Object object : jsonArray) {
                    payBillers.add(new Gson().fromJson(object.toString(), PayBiller.class));
                }
                return payBillers;
            } else {
                throw new GenericException("Failed to retrieve billers.");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            out.println("Local message " + e.getLocalizedMessage());
            throw new GenericException("Connection error!");
        }
    }

    public List<BillerCategoryNew> getBillerCategories() {

        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> httpResponse = Unirest.get(BASE_URL + GET_BILLERS_CATEGORIES)
                .asString();

            out.printf("Get billers categories response: %s\n", httpResponse);
            out.printf("%nGet billers categories response data: %s\n\n", httpResponse.getBody());
            out.printf("Get billers categories response status: %s\n", httpResponse.getStatus());
            CamelResponse camelResponse = new Gson().fromJson(httpResponse.getBody(), CamelResponse.class);

            if (httpResponse.getStatus() == 200 && camelResponse.getStatusCode() == 200) {
                JSONObject jsonObject = new JSONObject(httpResponse.getBody());
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                List<BillerCategoryNew> billerCategory = new ArrayList<>();

                for (Object object : jsonArray) {
                    billerCategory.add(new Gson().fromJson(object.toString(), BillerCategoryNew.class));
                }
                return billerCategory;
            } else {
                throw new GenericException("Failed to retrieve billers category.");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            out.println("Local message " + e.getLocalizedMessage());
            throw new GenericException("Connection error!");
        }
    }

    public List<PayBiller> getBillerCategoryId(String categoryId) {

        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> httpResponse = Unirest.get(BASE_URL + GET_BILLERS_CATEGORIES_BY_ID)
                .routeParam("categoryId", categoryId).asString();

            out.printf("Get billers by category Id response: %s\n", httpResponse);
            out.printf("%nGet billers by category Id response data: %s\n\n", httpResponse.getBody());
            out.printf("Get billers by category Id response status: %s\n", httpResponse.getStatus());
            CamelResponse camelResponse = new Gson().fromJson(httpResponse.getBody(), CamelResponse.class);

            if (httpResponse.getStatus() == 200 && camelResponse.getStatusCode() == 200) {
                JSONObject jsonObject = new JSONObject(httpResponse.getBody());
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                List<PayBiller> payBillers = new ArrayList<>();

                for (Object object : jsonArray) {
                    payBillers.add(new Gson().fromJson(object.toString(), PayBiller.class));
                }
                return payBillers;
            } else {
                throw new GenericException("Failed to retrieve billers by category Id.");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            out.println("Local message " + e.getLocalizedMessage());
            throw new GenericException("Connection error!");
        }
    }

    public List<BillerProduct> getBillerProducts(String billerId) {

        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> httpResponse = Unirest.get(BASE_URL + GET_BILLERS_PRODUCTS).routeParam("billerId", billerId).asString();

            out.printf("Get billers products response: %s\n", httpResponse);
            out.printf("%nGet billers products response data: %s\n\n", httpResponse.getBody());
            out.printf("Get billers products response status: %s\n", httpResponse.getStatus());
            CamelResponse camelResponse = new Gson().fromJson(httpResponse.getBody(), CamelResponse.class);

            if (httpResponse.getStatus() == 200 && camelResponse.getStatusCode() == 200) {
                JSONObject jsonObject = new JSONObject(httpResponse.getBody());
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                List<BillerProduct> billerProducts = new ArrayList<>();

                for (Object object : jsonArray) {
                    billerProducts.add(new Gson().fromJson(object.toString(), BillerProduct.class));
                }
                return billerProducts;
            } else {
                throw new GenericException("Failed to retrieve billers products.");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            out.println("Local message " + e.getLocalizedMessage());
            throw new GenericException("Connection error!");
        }
    }

    public BillerValidateCustomer validateCustomer(String billPaymentProductId, String customerId) {
        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> httpResponse = Unirest.post(BASE_URL + VALIDATE_CUSTOMER)
                .header("Content-Type", "application/json")
                .body("{\r\n\"billPaymentProductId\": \"" + billPaymentProductId + "\"," +
                    "\r\n\"customerId\": \"" + customerId + "\"\r\n}")
                .asString();


            out.printf("Validate customer response: %s\n", httpResponse);
            out.printf("%nValidate customer response data: %s\n\n", httpResponse.getBody());
            out.printf("Validate customer response status: %s\n", httpResponse.getStatus());
            CamelResponse camelResponse = new Gson().fromJson(httpResponse.getBody(), CamelResponse.class);

            if (httpResponse.getStatus() == 200 && camelResponse.getStatusCode() == 200) {
                JSONObject jsonObject = new JSONObject(httpResponse.getBody()).getJSONObject("data");

                return new Gson().fromJson(jsonObject.toString(), BillerValidateCustomer.class);
            } else {
                throw new GenericException("Failed to validate customer.");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            out.println("Local message " + e.getLocalizedMessage());
            throw new GenericException("Connection error!");
        }
    }

    public PayBillerRRR getRRRDetails(Long rrr) {
        try {
            out.println("Attempting to get RRR details");
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> httpResponse = Unirest.get(BASE_URL + LOOKUP_RRR + rrr)
                .asString();

            out.printf("%nRRR details response data: %s\n\n", httpResponse.getBody());
            out.printf("Get RRR details response status: %s\n", httpResponse.getStatus());
            CamelResponse camelResponse = new Gson().fromJson(httpResponse.getBody(), CamelResponse.class);
            if (httpResponse.getStatus() == 200 && camelResponse.getStatusCode() == 200) {
                JSONObject jsonObject = new JSONObject(httpResponse.getBody());
                return new Gson().fromJson(jsonObject.get("data").toString(), PayBillerRRR.class);
            } else {
                throw new GenericException("Failed to retrieve rrr details.");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            out.println("Local message " + e.getLocalizedMessage());
            throw new GenericException("Connection error!");
        }
    }


    public BillerInitiateTransactionResponse initiateTransaction(InitiateBillerTransaction initiateBillerTransaction) {

        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> httpResponse = Unirest.post(BASE_URL + INITIATE_TRANSACTION)
                .header("Content-Type", "application/json")
                .body(new JSONObject(initiateBillerTransaction).toString())
                .asString();


            out.printf("Initiate transaction response: %s\n", httpResponse);
            out.printf("%nInitiate transaction response data: %s\n\n", httpResponse.getBody());
            out.printf("Initiate transaction response status: %s\n", httpResponse.getStatus());
            CamelResponse camelResponse = new Gson().fromJson(httpResponse.getBody(), CamelResponse.class);

            if (httpResponse.getStatus() == 200 && camelResponse.getStatusCode() == 200) {
                JSONObject jsonObject = new JSONObject(httpResponse.getBody()).getJSONObject("data");

                return new Gson().fromJson(jsonObject.toString(), BillerInitiateTransactionResponse.class);
            } else {
                throw new GenericException("Failed to initiate transaction.");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            out.println("Local message " + e.getLocalizedMessage());
            throw new GenericException("Connection error!");
        }
    }

    public JSONObject billPaymentNotification(JSONObject jsonObject) {

        try {
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> httpResponse = Unirest.post(BASE_URL + BILL_PAYMENT_NOTIFICATION)
                .header("Content-Type", "application/json")
                .body(jsonObject.toString())
                .asString();


            out.printf("Bill payment notification response: %s\n", httpResponse);
            out.printf("%nBill payment notification response data: %s\n\n", httpResponse.getBody());
            out.printf("Bill payment notification response status: %s\n", httpResponse.getStatus());
            CamelResponse camelResponse = new Gson().fromJson(httpResponse.getBody(), CamelResponse.class);

            if (httpResponse.getStatus() == 200 && camelResponse.getStatusCode() == 200) {

                return new JSONObject(httpResponse.getBody()).getJSONObject("data");
            } else {
                throw new GenericException("Failed to call bill payment notification.");
            }
        } catch (UnirestException e) {
            e.printStackTrace();
            out.println("Local message " + e.getLocalizedMessage());
            throw new GenericException("Connection error!");
        }
    }
}
