package com.lacouf.rsbjwt.presentation;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

class PDFControllerTest {

    @Test
    @WithMockUser(username = "user", roles = {"GESTIONNAIRE"})
    void generateContratPDF() {

    }
}