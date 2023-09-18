package ng.com.systemspecs.apigateway.service.dto.stp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Customfielddata {

    @JsonProperty("DESCRIPTION")
    private String description;
    @JsonProperty("COLVAL")
    private String colval;
    public void setDescription(String description) {
         this.description = description;
     }
     public String getDescription() {
         return description;
     }

    public void setColval(String colval) {
         this.colval = colval;
     }
     public String getColval() {
         return colval;
     }

}
