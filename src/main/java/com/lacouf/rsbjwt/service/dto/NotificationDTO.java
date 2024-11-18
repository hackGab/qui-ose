package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.Notification;
import java.time.Duration;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String message;
    private String titreOffre;
    private boolean vu;
    private String email;
    private String url;
    private String tempsDepuisReception;

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.titreOffre = notification.getTitreOffre();
        this.vu = notification.isVu();
        this.email = notification.getEmail();
        this.url = notification.getUrl();
        this.tempsDepuisReception = calculateTempsDepuisReception(notification.getDateCreation());
    }

    private String calculateTempsDepuisReception(LocalDateTime dateCreation) {
        Duration duration = Duration.between(dateCreation, LocalDateTime.now());

        if (duration.getSeconds() < 60) {
            return "il y a " + duration.getSeconds() + " secondes";
        } else if (duration.toMinutes() < 60) {
            return "il y a " + duration.toMinutes() + " minutes";
        } else if (duration.toHours() < 24) {
            return "il y a " + duration.toHours() + " heures";
        } else {
            return "il y a " + duration.toDays() + " jours";
        }
    }

    @Override
    public String toString() {
        return "NotificationDTO{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", titreOffre='" + titreOffre + '\'' +
                ", vu=" + vu +
                ", email='" + email + '\'' +
                ", url='" + url + '\'' +
                ", tempsDepuisReception='" + tempsDepuisReception + '\'' +
                '}';
    }
}
