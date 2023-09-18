package ng.com.systemspecs.apigateway.service.dto;


public class PolarisCardProfileDTO {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    String cardId;

    String cardName;

    String cardType;

    Double cardFee;


    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Double getCardFee() {
        return cardFee;
    }

    public void setCardFee(Double cardFee) {
        this.cardFee = cardFee;
    }

    @Override
    public String toString() {
        return "PolarisCardProfileDTO{" +
            "id=" + id +
            ", cardId='" + cardId + '\'' +
            ", cardName='" + cardName + '\'' +
            ", cardType='" + cardType + '\'' +
            ", cardFee=" + cardFee +
            '}';
    }
}
