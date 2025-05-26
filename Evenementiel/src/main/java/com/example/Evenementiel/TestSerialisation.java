package com.example.Evenementiel;

import com.example.Evenementiel.Entities.Concrete.Concert;
import com.example.Evenementiel.Entities.Concrete.Conference;
import com.example.Evenementiel.Utils.EvenementSerializer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TestSerialisation {
    public static void main(String[] args) {
        try {
            Concert concert = new Concert(
                    "C1", "Jazz Night", LocalDateTime.now(), "Paris", 100, "Youssou Ndour", "Jazz"
            );
            Conference conference = new Conference(
                    "F1", "AI Conf", LocalDateTime.now(), "Yaoundé", 50, "IA & Éthique", List.of()
            );

            // Sauvegarde
            EvenementSerializer.sauvegarderEvenements(List.of(concert, conference), "evenements.json");
            // Chargement
            // var evenements = EvenementSerializer.chargerEvenements("evenements.json");
            // evenements.forEach(e -> {
            //     System.out.println("Événement rechargé :");
            //     e.afficherDetails();
            // });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}