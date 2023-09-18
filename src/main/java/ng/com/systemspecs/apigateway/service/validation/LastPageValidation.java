package ng.com.systemspecs.apigateway.service.validation;

import ng.com.systemspecs.apigateway.service.dto.RegistrationLastPageDTO;
import org.apache.logging.log4j.util.Strings;

public class LastPageValidation {
    private String errors = "";
    private boolean isValid = true;
    private final String MALE = "MALE";
    private final String FEMALE = "FEMALE";
    private final RegistrationLastPageDTO input;

    public LastPageValidation(RegistrationLastPageDTO data) {
        this.isValid = true;
        this.errors = "";
        this.input = data;
    }

    public boolean checkErrors() {
        if (Strings.isEmpty(this.input.getAddress())) {
            this.isValid = false;
            this.errors += " The address field is required!";
		}
		if(this.input.getLatitude() == null) {
			this.isValid = false;
			this.errors += " The Latitude field is required!";
		}
		if(this.input.getLongitude() == null) {
			this.isValid = false;
			this.errors += " The Longitude field is required!";
		}
		if(this.input.getDateOfBirth() == null) {
			this.isValid = false;
			this.errors += " The date of birth field is required!";
		}
		if(this.input.getGender() == null) {
			this.isValid = false;
			this.errors += " The gender field is required!";
		}
		if(!this.input.getGender().equalsIgnoreCase(FEMALE) && !this.input.getGender().equalsIgnoreCase(MALE)) {
			this.isValid = false;
			this.errors += " The gender must either be a MALE or a FEMALE";
		}
		return this.isValid;
	}

	public String getErrors() {
		return this.errors;
	}
}
