package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.CV;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CVDTO {
    private Long id;
    private String name;
    private String type;
    private LocalDate uploadDate;
    private String data;
    private String status;
    private String rejetMessage;

    public CVDTO(String name, String type, String data, String status) {
        this.name = name;
        this.type = type;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.uploadDate = LocalDate.parse(LocalDate.now().format(formatter), formatter);
        this.data = data;
        this.status = status;
        this.rejetMessage = "";
    }

    public CVDTO(CV cv) {
        this.id = cv.getId();
        this.name = cv.getName();
        this.type = cv.getType();
        this.uploadDate = cv.getUploadDate();
        this.data = cv.getData();
        this.status = cv.getStatus();
        this.rejetMessage = cv.getRejetMessage();
    }

    @Override
    public String toString() {
        return "CVDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", uploadDate=" + uploadDate +
                ", status='" + status + '\'' +
                ", messageDeRejet='" + rejetMessage + '\'' +
                '}';
    }
}
