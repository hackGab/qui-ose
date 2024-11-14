package com.lacouf.rsbjwt.service.dto;
import com.lacouf.rsbjwt.model.Notification;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private String message;
    private boolean vu;
    private String email;
    private String url;
    private String tempsDepuisReception;
    private LocalDateTime dateCreation;

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.vu = notification.isVu();
        this.email = notification.getEmail();
        this.url = notification.getUrl();
        this.tempsDepuisReception = notification.getTempsDepuisReception();
        this.dateCreation = notification.getDateCreation();
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", vu=" + vu +
                ", email='" + email + '\'' +
                ", url='" + url + '\'' +
                ", tempsDepuisReception='" + tempsDepuisReception + '\'' +
                ", dateCreation=" + dateCreation +
                '}';
    }

}