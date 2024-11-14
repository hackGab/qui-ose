package com.lacouf.rsbjwt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public Notification(String message, String titreOffre, String email, String url) {
        this.message = message;
        this.titreOffre = titreOffre;
        this.vu = false;
        this.email = email;
        this.url = url;
        this.dateCreation = LocalDateTime.now();
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
