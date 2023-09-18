package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ng.com.justjava.corebanking.domain.BillerPlatform;
import ng.com.justjava.corebanking.domain.CustomFieldDropdown;

import java.io.Serializable;
import java.util.Set;

public class BillerServiceOptionDTO implements Serializable {

    @JsonIgnore
    private String id;
    private String serviceOptionId;
    private String name;
    private String type;
    private String length;
    private boolean required;
    private boolean hasFixedPrice;
    private double fixedAmount;
    private String billerPlatformId;

    private boolean hasDropdown;

    private Set<CustomFieldDropdown> customFieldDropdowns;

    private BillerPlatform billerPlatform;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getBillerPlatformId() {
        return billerPlatformId;
    }

    public void setBillerPlatformId(String billerPlatformId) {
        this.billerPlatformId = billerPlatformId;
    }

    public Set<CustomFieldDropdown> getCustomFieldDropdowns() {
        return customFieldDropdowns;
    }

    public void setCustomFieldDropdowns(Set<CustomFieldDropdown> customFieldDropdowns) {
        this.customFieldDropdowns = customFieldDropdowns;
    }

    public BillerPlatform getBillerPlatform() {
        return billerPlatform;
    }

    public void setBillerPlatform(BillerPlatform billerPlatform) {
        this.billerPlatform = billerPlatform;
    }

    public boolean isHasDropdown() {
        return hasDropdown;
    }

    public void setHasDropdown(boolean hasDropdown) {
        this.hasDropdown = hasDropdown;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isHasFixedPrice() {
        return hasFixedPrice;
    }

    public void setHasFixedPrice(boolean hasFixedPrice) {
        this.hasFixedPrice = hasFixedPrice;
    }

    public double getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(double fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public String getServiceOptionId() {
        return serviceOptionId;
    }

    public void setServiceOptionId(String serviceOptionId) {
        this.serviceOptionId = serviceOptionId;
    }

    @Override
    public String toString() {
        return "BillerServiceOptionDTO{" +
            "id='" + id + '\'' +
            ", serviceOptionId='" + serviceOptionId + '\'' +
            ", name='" + name + '\'' +
            ", type='" + type + '\'' +
            ", length='" + length + '\'' +
            ", required=" + required +
            ", hasFixedPrice=" + hasFixedPrice +
            ", fixedAmount=" + fixedAmount +
            ", billerPlatformId='" + billerPlatformId + '\'' +
            ", hasDropdown=" + hasDropdown +
            ", customFieldDropdowns=" + customFieldDropdowns +
            ", billerPlatform=" + billerPlatform +
            '}';
    }
}
