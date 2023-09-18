package ng.com.systemspecs.apigateway.domain;

import ng.com.systemspecs.apigateway.domain.enumeration.AppNotificationType;

import javax.persistence.*;

@Entity
@Table(name = "app_notification")
public class AppNotification extends AbstractAuditingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "audience", nullable = false)
    private String audience;

    @Column(name = "scheme", nullable = false)
    private String scheme;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private AppNotificationType notificationType;

    @Column(name = "display")
    private boolean display = true;

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AppNotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(AppNotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
