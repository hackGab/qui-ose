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

    @GetMapping("/allUnread/{email}")
    public ResponseEntity<List<NotificationDTO>> getAllUnreadNotificationsByEmail(@PathVariable String email) {
        List<NotificationDTO> notifications = systemeService.getAllUnreadNotificationsByEmail(email);
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/markAsRead/{id}")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Long id) {
        systemeService.markNotificationAsRead(id);
        return ResponseEntity.ok().build();
    }

}
