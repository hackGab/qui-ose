package com.lacouf.rsbjwt.model;

import jakarta.persistence.*;
import lombok.*;
import org.w3c.dom.Text;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
    private Date uploadDate;

    @Column(nullable = false, length = Integer.MAX_VALUE)
    private String data;

    @Column(nullable = false)
    private String status;

    public CV(String name, String type, Date uploadDate, String data, String status) {
        this.name = name;
        this.type = type;
        this.uploadDate = uploadDate;
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
                '}';
    }
}