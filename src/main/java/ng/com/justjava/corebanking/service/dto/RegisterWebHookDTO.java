package ng.com.justjava.corebanking.service.dto;

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
