package com.example.Events.Service;
import com.example.Events.Entities.Concrete.Participant;
import com.example.Events.Exception.ParticipantIntrouvableException;
import jakarta.xml.bind.ValidationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ParticipantService {
    private final List<Participant> participants;
    private final JsonDataService jsonDataService;
    private final NotificationService notificationService;

    public ParticipantService(JsonDataService jsonDataService, NotificationService notificationService) {
        this.jsonDataService = jsonDataService;
        this.notificationService = notificationService;
        this.participants = jsonDataService.chargerParticipants();
    }

    public Participant creerParticipant(String nom, String email) throws ValidationException {
        Participant participant = new Participant(UUID.randomUUID().toString(),nom,email);
        // Validations
        if (participant.getId() == null || participant.getId().trim().isEmpty()) {
            throw new ValidationException("L'ID du participant ne peut pas être vide");
        }
        if (participant.getNom() == null || participant.getNom().trim().isEmpty()) {
            throw new ValidationException("Le nom du participant ne peut pas être vide");
        }
        if (participant.getEmail() == null || !participant.getEmail().contains("@")) {
            throw new ValidationException("L'email du participant doit être valide");
        }

        // Vérifier unicité
        if (participants.stream().anyMatch(p -> p.getId().equals(participant.getId()))) {
            throw new ValidationException("Un participant avec cet ID existe déjà");
        }
        if (participants.stream().anyMatch(p -> p.getEmail().equals(participant.getEmail()))) {
            throw new ValidationException("Un participant avec cet email existe déjà");
        }

        participants.add(participant);
        jsonDataService.sauvegarderParticipants(participants);

        notificationService.envoyerNotificationAsync(
                "Bienvenue " + participant.getNom() + "! Votre compte a été créé avec succès."
        );

        return null;
    }

    public List<Participant> obtenirTousLesParticipants() {
        return new ArrayList<>(participants);
    }

    public Optional<Participant> obtenirParticipantParId(String id) {
        return participants.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Optional<Participant> obtenirParticipantParEmail(String email) {
        return participants.stream()
                .filter(p -> p.getEmail().equals(email))
                .findFirst();
    }

    public Participant mettreAJourParticipant(String id, Participant participantMisAJour)
            throws ParticipantIntrouvableException, ValidationException {

        Optional<Participant> participantExistant = obtenirParticipantParId(id);
        if (participantExistant.isEmpty()) {
            throw new ParticipantIntrouvableException("Participant avec l'ID " + id + " non trouvé");
        }

        // Validations
        if (participantMisAJour.getNom() == null || participantMisAJour.getNom().trim().isEmpty()) {
            throw new ValidationException("Le nom du participant ne peut pas être vide");
        }
        if (participantMisAJour.getEmail() == null || !participantMisAJour.getEmail().contains("@")) {
            throw new ValidationException("L'email du participant doit être valide");
        }

        // Vérifier unicité email (sauf pour le participant actuel)
        if (participants.stream()
                .anyMatch(p -> !p.getId().equals(id) && p.getEmail().equals(participantMisAJour.getEmail()))) {
            throw new ValidationException("Un autre participant avec cet email existe déjà");
        }

        Participant participant = participantExistant.get();
        participant.setNom(participantMisAJour.getNom());
        participant.setEmail(participantMisAJour.getEmail());

        jsonDataService.sauvegarderParticipants(participants);
        return participant;
    }

    public void supprimerParticipant(String id) throws ParticipantIntrouvableException {
        Optional<Participant> participant = obtenirParticipantParId(id);
        if (participant.isEmpty()) {
            throw new ParticipantIntrouvableException("Participant avec l'ID " + id + " non trouvé");
        }

        participants.removeIf(p -> p.getId().equals(id));
    }

    public List<Participant> rechercherParticipantsParNom(String nom) {
        return participants.stream()
                .filter(p -> p.getNom().toLowerCase().contains(nom.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Participant> rechercherParticipantsParEmail(String email) {
        return participants.stream()
                .filter(p -> p.getEmail().toLowerCase().contains(email.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Méthode interne pour mettre à jour les événements d'un participant
    void ajouterEvenementAuParticipant(String participantId, String evenementId) {
        obtenirParticipantParId(participantId).ifPresent(participant -> {
            if (!participant.getEvenementIds().contains(evenementId)) {
                participant.getEvenementIds().add(evenementId);
                jsonDataService.sauvegarderParticipants(participants);
            }
        });
    }

    void retirerEvenementDuParticipant(String participantId, String evenementId) {
        obtenirParticipantParId(participantId).ifPresent(participant -> {
            participant.getEvenementIds().remove(evenementId);
            jsonDataService.sauvegarderParticipants(participants);
        });
    }
}
