package com.example.Events.TestService;

import com.example.Events.Entities.Concrete.Participant;
import com.example.Events.Service.JsonDataService;
import com.example.Events.Service.NotificationService;
import com.example.Events.Service.ParticipantService;
import jakarta.xml.bind.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ParticipantServiceTest {

    private ParticipantService participantService;
    private NotificationService notificationService;
    private JsonDataService jsonDataService;

    @BeforeEach
    void setUp() {
        jsonDataService = mock(JsonDataService.class);
        notificationService = mock(NotificationService.class);
        participantService = new ParticipantService(jsonDataService, notificationService);

        when(jsonDataService.chargerParticipants()).thenReturn(new ArrayList<>());
    }

    @Test
    @DisplayName("Doit lever une exception pour un email invalide")
    void testCreerParticipantEmailInvalide() {
        // Given
        Participant participant = new Participant("P001", "John Doe", "invalid-email");

        // When & Then
        assertThrows(ValidationException.class, () -> {
            participantService.creerParticipant(participant.getNom(),participant.getEmail());
        });
    }

    @Test
    @DisplayName("Doit rechercher des participants par nom")
    void testRechercherParNom() throws Exception {
        // Given
        Participant p1 = new Participant("P001", "Alice Martin", "alice@example.com");
        Participant p2 = new Participant("P002", "Bob Martin", "bob@example.com");
        Participant p3 = new Participant("P003", "Charlie Smith", "charlie@example.com");

        participantService.creerParticipant(p1.getNom(),p1.getEmail());
        participantService.creerParticipant(p2.getNom(), p2.getEmail());
        participantService.creerParticipant(p3.getNom(),p3.getEmail());

        // When
        List<Participant> resultats = participantService.rechercherParticipantsParNom("Martin");

        // Then
        assertEquals(2, resultats.size());
        assertTrue(resultats.stream().allMatch(p -> p.getNom().contains("Martin")));
    }
}
