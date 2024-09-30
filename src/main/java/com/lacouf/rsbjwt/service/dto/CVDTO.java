package com.lacouf.rsbjwt.service.dto;

import com.lacouf.rsbjwt.model.CV;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CVDTO {
    private Long id;
    private String name;
    private String type;
    private Date uploadDate;
    private String data;
    private String status;

    public CVDTO(String name, String type, Date uploadDate, String data, String status) {
        this.name = name;
        this.type = type;
        this.uploadDate = uploadDate;
        this.data = data;
        this.status = status;
    }

    public CVDTO(CV cv) {
        this.id = cv.getId();
        this.name = cv.getName();
        this.type = cv.getType();
        this.uploadDate = cv.getUploadDate();
        this.data = cv.getData();
        this.status = cv.getStatus();
    }

    @Override
    public String toString() {
        return "CVDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", uploadDate=" + uploadDate +
                ", status='" + status + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
