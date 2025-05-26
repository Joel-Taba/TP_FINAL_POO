package com.example.Evenementiel;

import com.example.Evenementiel.Entities.Abstract.Evenement;
import com.example.Evenementiel.Entities.Concrete.Concert;
import com.example.Evenementiel.Entities.Concrete.Conference;
import com.example.Evenementiel.Utils.EvenementSerializer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EvenementSerialisationTest {

    @Test
    public void testSauvegardeEtChargementEvenements() throws Exception {
        // Fichier temporaire pour le test
        String fichierTest = "evenements_test.json";

        // Création de deux événements
        Concert concert = new Concert("C4", "Concert Test", LocalDateTime.now(), "Douala", 100, "Fally", "Rumba");
        Conference conf = new Conference("F4", "Conf Test", LocalDateTime.now(), "Yaoundé", 50, "Tech", List.of());

        // Sauvegarde dans le fichier JSON
        EvenementSerializer.sauvegarderEvenements(List.of(concert, conf), fichierTest);

        // Chargement depuis le fichier
        List<Evenement> relus = EvenementSerializer.chargerEvenements(fichierTest);

        // Vérifications
        assertEquals(2, relus.size());
        assertEquals("Concert Test", relus.get(0).getNom());
        assertEquals("Conf Test", relus.get(1).getNom());

        // Nettoyage
        new File(fichierTest).delete();
    }
}