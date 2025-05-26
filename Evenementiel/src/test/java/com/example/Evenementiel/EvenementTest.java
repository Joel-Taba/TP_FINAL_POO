package com.example.Evenementiel;

import com.example.Evenementiel.Entities.Concrete.Concert;
import com.example.Evenementiel.Entities.Concrete.Participant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EvenementTest {

    @Test
    public void testAjoutParticipantReussi() throws CapaciteMaximalAtteinteException {
        // Création d’un événement avec capacité 2
        Concert concert = new Concert(
                "C1", "Test Concert", LocalDateTime.now(), "Douala", 2,
                "Artiste X", "Afro"
        );

        // Création d’un participant
        Participant participant = new Participant("P1", "Alice", "alice@example.com");

        // Action
        concert.ajouterParticipant(participant);

        // Vérification
        assertEquals(1, concert.getParticipants().size());
        assertEquals("Alice", concert.getParticipants().get(0).getNom());
    }

    @Test
    public void testDepassementCapaciteDeclencheException() {
        // Création d’un concert avec capacité 1
        Concert concert = new Concert(
                "C2", "Petit Concert", LocalDateTime.now(), "Douala", 1,
                "Artiste Z", "Rap"
        );

        Participant p1 = new Participant("P1", "Bob", "bob@example.com");
        Participant p2 = new Participant("P2", "Charlie", "charlie@example.com");

        // Premier ajout sans souci
        try {
            concert.ajouterParticipant(p1);
        } catch (CapaciteMaximalAtteinteException e) {
            fail("Le premier ajout ne doit pas échouer");
        }

        // Deuxième ajout → on attend une exception
        assertThrows(
                CapaciteMaximalAtteinteException.class,
                () -> concert.ajouterParticipant(p2),
                "L'ajout du deuxième participant aurait dû lancer une exception"
        );
    }
}