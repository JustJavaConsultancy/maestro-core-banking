package ng.com.justjava.corebanking.service.dto;

public class PostResponseDTO {
	private String message;
	private String token;
	private PostResponseDataDTO postResponseDataDTO;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public PostResponseDataDTO getPostResponseDataDTO() {
		return postResponseDataDTO;
	}
	public void setPostResponseDataDTO(PostResponseDataDTO postResponseDataDTO) {
		this.postResponseDataDTO = postResponseDataDTO;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}


    @Override
    public String toString() {
        return "PostResponseDTO{" +
            "message='" + message + '\'' +
            ", token='" + token + '\'' +
            ", postResponseDataDTO=" + postResponseDataDTO +
            '}';
    }
}
