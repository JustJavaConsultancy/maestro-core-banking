package ng.com.justjava.corebanking.service.dto;

public class PostResponseDataDTO {
	private String code;
	private String description;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

    @Override
    public String toString() {
        return "PostResponseDataDTO{" +
            "code='" + code + '\'' +
            ", description='" + description + '\'' +
            '}';
    }
}
