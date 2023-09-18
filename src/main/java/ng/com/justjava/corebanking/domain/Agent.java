package ng.com.justjava.corebanking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ng.com.justjava.corebanking.domain.enumeration.AgentType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Profile of the user of the Solution this can be of any of the types :- Customer, Agent or Admin
 */
@Entity
@Table(name = "agent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Agent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "business_name")
    private String businessName;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "company_reg_type")
    private String companyRegType;

    @Column(name = "company_reg_no")
    private String companyRegNumber;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "cac_doc")
    private String cACDoc;

    @Column(name = "c07_doc")
    private String c07Doc;

    @Column(name = "c01_doc")
    private String c01Doc;

    @Column(name = "c02_doc")
    private String c02Doc;

    @OneToOne
    private Profile profile;

    @ManyToOne
    @JsonIgnoreProperties(value = "subAgents", allowSetters = true)
    private Agent superAgent;

    @OneToMany(mappedBy = "superAgent", fetch = FetchType.EAGER)
    private Set<Agent> subAgents = new HashSet<>();

    @OneToMany(mappedBy = "agent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Teller> tellers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Agent getSuperAgent() {
        return superAgent;
    }

    public void setSuperAgent(Agent superAgent) {
        this.superAgent = superAgent;
    }

    public Set<Agent> getSubAgents() {
        return subAgents;
    }

    public void setSubAgents(Set<Agent> subAgents) {
        this.subAgents = subAgents;
    }

    public Set<Teller> getTellers() {
        return tellers;
    }

    public void setTellers(Set<Teller> tellers) {
        this.tellers = tellers;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCompanyRegType() {
        return companyRegType;
    }

    public void setCompanyRegType(String companyRegType) {
        this.companyRegType = companyRegType;
    }

    public String getCompanyRegNumber() {
        return companyRegNumber;
    }

    public void setCompanyRegNumber(String companyRegNumber) {
        this.companyRegNumber = companyRegNumber;
    }

    public String getAgentType() {

        if (StringUtils.isNotEmpty(businessName) && StringUtils.isNotEmpty(companyRegNumber)) {
            return AgentType.BUSINESS.toString();
        }

        return AgentType.INDIVIDUAL.toString();
    }

    public String getcACDoc() {
        return cACDoc;
    }

    public String getC07Doc() {
        return c07Doc;
    }

    public String getC01Doc() {
        return c01Doc;
    }

    public String getC02Doc() {
        return c02Doc;
    }

    @Override
    public String toString() {
        return "Agent{" +
            "id=" + id +
            ", location='" + location + '\'' +
            ", latitude=" + latitude +
            ", occupation='" + occupation + '\'' +
            ", businessName='" + businessName + '\'' +
            ", businessType='" + businessType + '\'' +
            ", companyRegType='" + companyRegType + '\'' +
            ", companyRegNumber='" + companyRegNumber + '\'' +
            ", longitude=" + longitude +
            ", cACDoc='" + cACDoc + '\'' +
            ", c07Doc='" + c07Doc + '\'' +
            ", c01Doc='" + c01Doc + '\'' +
            ", c02Doc='" + c02Doc + '\'' +
            '}';
    }
}
