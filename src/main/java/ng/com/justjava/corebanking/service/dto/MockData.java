package ng.com.justjava.corebanking.service.dto;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class MockData {

//  public final  static	String[]  airtimeProviders  = { "Glo", "MTN", "Airtel", "9Mobile" };
  public final  static	long[]  airtimeProviders  = { 168644, 4729747, 816846, 818914 };
  public final  static	String[]  dataPlanIds  = { "48297525", "2514514", "963972", "2427544" };
  public final  static	String[]  billerNames  = { "Glo Nigeria Limited", "MTN Nigeria", "Airtel Nigeria", "9Mobile Nigeria" };


    public static  List<DataPlanDTO> getDataPlans(String providerId) {
        List<DataPlanDTO> dataPlanDTOs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            DataPlanDTO dataPlanDTO = new DataPlanDTO();
            dataPlanDTO.setId(dataPlanIds[i]);
            dataPlanDTO.setName("N" + ((i + 1) * 2000) + " " + providerId + " Data Plus");
            dataPlanDTO.setAmount((i + 1) * 2000);

            dataPlanDTOs.add(dataPlanDTO);
        }
        return dataPlanDTOs;
    }


    public static GenericResponseDTO buyAirtime(BuyAirtimeDTO buyAirtimeDTO) {
        return new GenericResponseDTO("00", HttpStatus.OK, buyAirtimeDTO.getServiceId() + " Airtime purchase of " + buyAirtimeDTO.getAmount(), null);
    }


    public static GenericResponseDTO buyData(BuyDataDTO buyDataDTO) {
        String pin = buyDataDTO.getPin();
        return new GenericResponseDTO("00", HttpStatus.OK, "success", buyDataDTO);
    }





}
