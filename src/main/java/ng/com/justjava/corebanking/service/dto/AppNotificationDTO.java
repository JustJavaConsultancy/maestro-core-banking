package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.domain.enumeration.AppNotificationType;

import java.io.Serializable;
import java.time.Instant;

public class AppNotificationDTO implements Serializable {
    public Instant createdDate;
    public Long id;
    public String audience;
    public String scheme;
    public String content;
    public String title;
    public boolean display = true;
    public AppNotificationType notificationType;

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return "AppNotificationDTO{" +
            "createdDate=" + createdDate +
            ", id=" + id +
            ", audience='" + audience + '\'' +
            ", scheme='" + scheme + '\'' +
            ", content='" + content + '\'' +
            ", title='" + title + '\'' +
            ", notificationType=" + notificationType +
            '}';
    }
}
