package ng.com.justjava.corebanking.service.dto;

public class EmailDto {
    private String email;
    private String subject;
    private String content;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "EmailDto{" +
            "email='" + email + '\'' +
            ", subject='" + subject + '\'' +
            ", content='" + content + '\'' +
            '}';
    }
}
