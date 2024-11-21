package com.lacouf.rsbjwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private String titreOffre;

    @Column(nullable = false)
    private boolean vu;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    @Column(nullable = false)
    private String tempsDepuisReception;

    public Notification(String message, String titreOffre, String email, String url) {
        this.message = message;
        this.titreOffre = titreOffre;
        this.vu = false;
        this.email = email;
        this.url = url;
        this.dateCreation = LocalDateTime.now();
        this.tempsDepuisReception = calculateTempsDepuisReception(this.dateCreation);
    }


    public String calculateTempsDepuisReception(LocalDateTime dateCreation) {
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

    public void markAsRead() {
        this.vu = true;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", titreOffre='" + titreOffre + '\'' +
                ", vu=" + vu +
                ", email='" + email + '\'' +
                ", url='" + url + '\'' +
                ", dateCreation=" + dateCreation +
                '}';
    }
}
