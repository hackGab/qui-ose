package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.SystemeService;
import com.lacouf.rsbjwt.service.dto.NotificationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    private final SystemeService systemeService;

    public NotificationController(SystemeService systemeService) {
        this.systemeService = systemeService;
    }


    //get all notifications par email
    @GetMapping("/all/{email}")
    public ResponseEntity<List<NotificationDTO>> getAllNotificationsByEmail(@PathVariable String email) {
        System.out.println("yiyiyiyiyiyiyiyiyiyiyiyiyiiyiyyiyiiyiyiyiyiyiyiyiyiyiyiyiy");
        List<NotificationDTO> notifications = systemeService.getAllNotificationsByEmail(email);
        System.out.println("Notifications : " + notifications + "dfaffafsdfa");
        return ResponseEntity.ok(notifications);
    }
}
