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
    private boolean vu;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String tempsDepuisReception;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    public Notification(String message, String email, String url) {
        this.message = message;
        this.vu = false;
        this.email = email;
        this.url = url;
        this.dateCreation = LocalDateTime.now();
        this.tempsDepuisReception = getFormattedTimeSinceReception();
    }

    public void markAsRead() {
        this.vu = true;
    }


    public String getFormattedTimeSinceReception() {
        Duration duration = Duration.between(dateCreation, LocalDateTime.now());

        if (duration.getSeconds() < 60) {
            return duration.getSeconds() + " secondes";
        } else if (duration.toMinutes() < 60) {
            return duration.toMinutes() + " minutes";
        } else if (duration.toHours() < 24) {
            return duration.toHours() + " heures";
        } else {
            return duration.toDays() + " jours";
        }
    }

    @Override
    public String toString() {
        return "Notification{" +
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
