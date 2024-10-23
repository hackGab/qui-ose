package com.lacouf.rsbjwt.model;

import jakarta.persistence.*;
import lombok.*;
import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private LocalDate uploadDate;

    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String data;

    @Column(nullable = false)
    private String status;

    @Column(length = 500)
    private String rejetMessage = "";

    public CV(String name, String type,  String data, String status) {
        this.name = name;
        this.type = type;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.uploadDate = LocalDate.parse(LocalDate.now().format(formatter), formatter);
        this.data = data;
        this.status = status;
    }

    @Override
    public String toString() {
        return "CV{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", uploadDate=" + uploadDate +
                ", status='" + status + '\'' +
                ", rejetMessage='" + rejetMessage + '\'' +
                '}';
    }
}