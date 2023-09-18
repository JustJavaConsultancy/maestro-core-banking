package ng.com.justjava.corebanking.service.dto;

import java.util.List;

public class ItexBillersDto {
    private String category;
    private List<ItexBiller> billers;

    public ItexBillersDto() {
    }

    public ItexBillersDto(String category, List<ItexBiller> billers) {
        this.category = category;
        this.billers = billers;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ItexBiller> getBillers() {
        return billers;
    }

    public void setBillers(List<ItexBiller> billers) {
        this.billers = billers;
    }

    @Override
    public String toString() {
        return "ItexBillersDto{" +
            "category='" + category + '\'' +
            ", billers=" + billers +
            '}';
    }
}
