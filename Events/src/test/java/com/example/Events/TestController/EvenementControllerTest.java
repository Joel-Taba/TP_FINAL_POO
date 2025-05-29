package com.example.Events.TestController;

import com.example.Events.Service.EvenementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EvenementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EvenementService evenementService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // No reset or delete; preserve existing events in evenements.json
        // Ensure file exists to avoid IOException in EvenementService
        try {
            Path path = Paths.get("src/main/resources/data/evenements.json");
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path, "[]", StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize evenements.json", e);
        }
    }

    @Test
    void testCreateConferenceMissingTheme() throws Exception {
        mockMvc.perform(post("/evenements/creation")
                        .param("nom", "Test Conference")
                        .param("lieu", "Yaoundé")
                        .param("capaciteMax", "100")
                        // Pas de paramètre theme
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk()) // Votre contrôleur retourne null dans ce cas
                .andExpect(content().string("")); // Le body sera vide
    }

    @Test
    void testGetAllEvenements() throws Exception {
        // Create an event first
        mockMvc.perform(post("/evenements/creation")
                        .param("nom", "Another Conference")
                        .param("lieu", "Douala")
                        .param("capaciteMax", "50")
                        .param("theme", "Tech")
                        .param("date", "2025-05-24T16:03:00")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk());

        mockMvc.perform(get("/evenements")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[?(@.nom=='Another Conference')]").exists());
    }
    @Test
    void testGetEvenementByIdNotFound() throws Exception {
        mockMvc.perform(get("/evenements/non-existent-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}