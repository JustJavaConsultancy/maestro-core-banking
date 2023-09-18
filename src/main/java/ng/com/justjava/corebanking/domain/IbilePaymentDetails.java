package ng.com.justjava.corebanking.domain;

import ng.com.justjava.corebanking.domain.enumeration.IbilePaymentStatus;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ibile_payment_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IbilePaymentDetails extends AbstractDateAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    private IbilePaymentStatus status;

    @Column(name = "total_amount_paid")
    private double totalAmountPaid;

    @Column(name = "internal_ref")
    private String internalRef;

    @Column(name = "bill_ref_number")
    private String billReferenceNumber;

    @Column(name = "total_due")
    private Double totalDue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public IbilePaymentStatus getStatus() {
        return status;
    }

    public void setStatus(IbilePaymentStatus status) {
        this.status = status;
    }

    public double getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(double totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public String getInternalRef() {
        return internalRef;
    }

    public void setInternalRef(String internalRef) {
        this.internalRef = internalRef;
    }

    public String getBillReferenceNumber() {
        return billReferenceNumber;
    }

    public void setBillReferenceNumber(String billReferenceNumber) {
        this.billReferenceNumber = billReferenceNumber;
    }

    public Double getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(Double totalDue) {
        this.totalDue = totalDue;
    }

    @Override
    public String toString() {
        return "IbilePaymentDetails{" +
            "id=" + id +
            ", status=" + status +
            ", totalAmountPaid=" + totalAmountPaid +
            ", internalRef='" + internalRef + '\'' +
            ", billReferenceNumber='" + billReferenceNumber + '\'' +
            ", totalDue=" + totalDue +
            '}';
    }
}
