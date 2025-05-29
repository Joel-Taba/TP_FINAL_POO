package com.example.Events.Service;

import com.example.Events.Entities.Abstract.Evenement;
import com.example.Events.Entities.Concrete.Participant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JsonDataService {
    private final ObjectMapper objectMapper;
    private final String evenementsFilePath ="src/main/resources/data/evenements.json";
    private final String participantsFilePath= "src/main/resources/data/participants.json";

    public JsonDataService(@Value("${app.data.evenements:evenements.json}") String evenementsFilePath,
                           @Value("${app.data.participants:participants.json}") String participantsFilePath) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public synchronized void sauvegarderEvenements(List<Evenement> evenements) {
        try {
            objectMapper.writeValue(new File(evenementsFilePath), evenements);
            System.out.println("✅ Événements sauvegardés dans " + evenementsFilePath);
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de la sauvegarde des événements: " + e.getMessage());
            throw new RuntimeException("Erreur de sauvegarde des événements", e);
        }
    }

    public synchronized void sauvegarderParticipants(List<Participant> participants) {
        try {
            objectMapper.writeValue(new File(participantsFilePath), participants);
            System.out.println("✅ Participants sauvegardés dans " + participantsFilePath);
        } catch (IOException e) {
            System.err.println("❌ Erreur lors de la sauvegarde des participants: " + e.getMessage());
            throw new RuntimeException("Erreur de sauvegarde des participants", e);
        }
    }

    public List<Evenement> chargerEvenements() {
        try {
            File file = new File(evenementsFilePath);
            if (!file.exists()) {
                System.out.println("Fichier événements non trouvé, création d'une liste vide");
                return new ArrayList<>();
            }
            List<Evenement> evenements = objectMapper.readValue(file,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Evenement.class));
            System.out.println("✅ " + evenements.size() + " événements chargés depuis " + evenementsFilePath);
            return evenements;
        } catch (IOException e) {
            System.err.println("❌ Erreur lors du chargement des événements: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Participant> chargerParticipants() {
        try {
            File file = new File(participantsFilePath);
            if (!file.exists()) {
                System.out.println("Fichier participants non trouvé, création d'une liste vide");
                return new ArrayList<>();
            }
            List<Participant> participants = objectMapper.readValue(file,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Participant.class));
            System.out.println("✅ " + participants.size() + " participants chargés depuis " + participantsFilePath);
            return participants;
        } catch (IOException e) {
            System.err.println("❌ Erreur lors du chargement des participants: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}