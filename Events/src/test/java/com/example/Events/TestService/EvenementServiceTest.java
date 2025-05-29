package com.example.Events.TestService;

import com.example.Events.Entities.Abstract.Evenement;
import com.example.Events.Entities.Concrete.Conference;
import com.example.Events.Entities.Concrete.Participant;
import com.example.Events.Exception.CapaciteMaxAtteinteException;
import com.example.Events.Service.*;
import jakarta.xml.bind.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {
        "app.data.evenements=test-evenements.json",
        "app.data.participants=test-participants.json"
})
class EvenementServiceTest {

    private EvenementService evenementService;
    private ParticipantService participantService;
    private NotificationService notificationService;
    private JsonDataService jsonDataService;
    private EvenementFactory evenementFactory;

    @BeforeEach
    void setUp() {
        jsonDataService = mock(JsonDataService.class);
        notificationService = mock(NotificationService.class);
        evenementFactory = new EvenementFactory();
        participantService = new ParticipantService(jsonDataService, notificationService);
        evenementService = new EvenementService(jsonDataService, notificationService, participantService, evenementFactory);

        when(jsonDataService.chargerEvenements()).thenReturn(new ArrayList<>());
        when(jsonDataService.chargerParticipants()).thenReturn(new ArrayList<>());
    }

    @Test
    @DisplayName("Doit créer un événement conférence avec succès")
    void testCreerConference() throws Exception {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("theme", "Java Programming");

        // When
        Evenement conference = evenementService.creerEvenement(
                "conference", "CONF001", "Java Conference 2025",
                LocalDateTime.now().plusDays(30), "Tech Center", 150, params
        );

        // Then
        assertNotNull(conference);
        assertTrue(conference instanceof Conference);
        assertEquals("Java Conference 2025", conference.getNom());
        assertEquals(150, conference.getCapaciteMax());
        verify(jsonDataService).equals(any());
    }

    @Test
    @DisplayName("Doit lever une exception pour un événement avec date passée")
    void testCreerEvenementDatePassee() {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("artiste", "Test Artist");
        params.put("genreMusical", "Rock");

        // When & Then
        assertThrows(ValidationException.class, () -> {
            evenementService.creerEvenement(
                    "concert", "CONC001", "Test Concert",
                    LocalDateTime.now().minusDays(1), "Venue", 100, params
            );
        });
    }

    @Test
    @DisplayName("Doit inscrire un participant avec succès")
    void testInscrireParticipant() throws Exception {
        // Given
        Participant participant = new Participant("P001", "Test User", "test@example.com");
        participantService.creerParticipant(participant.getNom(), participant.getEmail());

        Map<String, String> params = new HashMap<>();
        params.put("theme", "Testing");
        Evenement evenement = evenementService.creerEvenement(
                "conference", "E001", "Test Event",
                LocalDateTime.now().plusDays(10), "Test Venue", 50, params
        );

        // When
        evenementService.inscrireParticipant("E001", "P001");

        // Then
        assertEquals(1, evenement.getParticipants().size());
        assertTrue(evenement.getParticipantIds().contains("P001"));
        verify(notificationService).envoyerNotificationAsync(anyString());
    }

    @Test
    @DisplayName("Doit lever une exception si capacité maximale atteinte")
    void testCapaciteMaxAtteinte() throws Exception {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("theme", "Test");
        Evenement evenement = evenementService.creerEvenement(
                "conference",
                "E001",
                "Small Event",
                LocalDateTime.now().plusDays(10), "Small Venue", 1, params
        );

        Participant p1 = new Participant(
                "P001",
                "User1",
                "user1@example.com");
        Participant p2 = new Participant("P002",
                "User2",
                "user2@example.com");
        participantService.creerParticipant(p1.getNom(), p1.getEmail());
        participantService.creerParticipant(p2.getNom(),p2.getEmail());

        evenementService.inscrireParticipant("E001", "P001");

        // When & Then
        assertThrows(CapaciteMaxAtteinteException.class, () -> {
            evenementService.inscrireParticipant("E001", "P002");
        });
    }
}

