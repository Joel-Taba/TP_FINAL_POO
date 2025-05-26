package com.example.Evenementiel;

import com.example.Evenementiel.Entities.Concrete.Concert;
import com.example.Evenementiel.Observer.ParticipantObserverTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class EvenementObserverTest {

    @Test
    public void testNotificationEnvoyeeLorsAnnulation() {
        // Créer un événement
        Concert concert = new Concert(
                "C3", "Annulation Test", LocalDateTime.now(), "Yaoundé", 5,
                "Artiste Test", "Rock");

        // Créer un faux participant qui observe
        ParticipantObserverTest observer = new ParticipantObserverTest();
        concert.getObservable().ajouterObserver(observer); // Accès direct ou via méthode publique

        // Annuler l’événement → déclenche la notification
        concert.annuler();

        // Vérifier que le message a bien été reçu
        assertEquals(1, observer.getMessages().size());
        assertTrue(observer.getMessages().get(0).contains("L'événement 'Annulation Test' est annulé."));
    }

    {

    }
}

