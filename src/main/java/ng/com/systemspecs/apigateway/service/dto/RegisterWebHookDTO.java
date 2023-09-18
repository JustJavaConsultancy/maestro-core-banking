package ng.com.systemspecs.apigateway.service.dto;

public class RegisterWebHookDTO {
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "RegisterWebHookDTO{" +
            "url='" + url + '\'' +
            '}';
    }
}
