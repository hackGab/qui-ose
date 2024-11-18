package com.lacouf.rsbjwt.presentation;

import com.lacouf.rsbjwt.service.SystemeService;
import com.lacouf.rsbjwt.service.dto.NotificationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationControllerTest {

    private MockMvc mockMvc;
    private SystemeService systemeService;
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        systemeService = Mockito.mock(SystemeService.class);
        notificationController = new NotificationController(systemeService);
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }

    @Test
    void testGetAllUnreadNotificationsByEmail() throws Exception {
        NotificationDTO notification1 = new NotificationDTO(1L, "Message 1", "Offre1", false, "test1@example.com", "/url1", "il y a 5 minutes");
        NotificationDTO notification2 = new NotificationDTO(2L, "Message 2", "Offre2", false, "test2@example.com", "/url2", "il y a 10 minutes");
        List<NotificationDTO> notifications = Arrays.asList(notification1, notification2);

        Mockito.when(systemeService.getAllUnreadNotificationsByEmail(anyString())).thenReturn(notifications);

        mockMvc.perform(get("/notification/allUnread/test@example.com"))
                .andExpect(status().isOk());

        ResponseEntity<List<NotificationDTO>> response = notificationController.getAllUnreadNotificationsByEmail("test@example.com");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Message 1", response.getBody().get(0).getMessage());
    }


    @Test
    void testMarkNotificationAsRead() throws Exception {
        // Arrange
        Mockito.doNothing().when(systemeService).markNotificationAsRead(anyLong());

        // Act & Assert
        mockMvc.perform(put("/notification/markAsRead/1"))
                .andExpect(status().isOk());

        Mockito.verify(systemeService, Mockito.times(1)).markNotificationAsRead(anyLong());
    }
}