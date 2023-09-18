package ng.com.justjava.corebanking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "polaris_card_profile")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PolarisCardProfile  extends AbstractAuditingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "card_id")
    String cardId;

    @Column(name = "card_name")
    String cardName;

    @Column(name = "card_type")
    String cardType;

    @Column(name = "card_fee")
    double cardFee;

    @Column(name = "scheme")
    String scheme;

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

    public double getCardFee() {
        return this.cardFee;
    }

    public void setCardFee(double cardFee) {
        this.cardFee = cardFee;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public String toString() {
        return "PolarisCardProfile{" +
            "id=" + id +
            ", cardId='" + cardId + '\'' +
            ", cardName='" + cardName + '\'' +
            ", cardType='" + cardType + '\'' +
            ", cardFee=" + cardFee +
            ", scheme='" + scheme + '\'' +
            "} " + super.toString();
    }
}
