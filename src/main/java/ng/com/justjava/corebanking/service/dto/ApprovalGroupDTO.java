package ng.com.justjava.corebanking.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ng.com.justjava.corebanking.domain.ApprovalGroup;
import ng.com.justjava.corebanking.domain.Profile;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the {@link ApprovalGroup} entity.
 */
public class ApprovalGroupDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private List<String> phoneNumbers ;

    @JsonIgnore
    private Set<Profile> profiles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhoneNumbers() {
        if (phoneNumbers == null && profiles != null && !profiles.isEmpty()){
            phoneNumbers = new ArrayList<>();
            for (Profile profile : profiles){
                phoneNumbers.add(profile.getPhoneNumber());
            }
            return phoneNumbers;
        }
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Set<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<Profile> profiles) {
        this.profiles = profiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApprovalGroupDTO)) {
            return false;
        }

        return id != null && id.equals(((ApprovalGroupDTO) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, phoneNumbers);
    }

    @Override
    public String toString() {
        return "ApprovalGroupDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", profiles=" + profiles +
            '}';
    }
}
