package com.example.Events.Service;

import com.example.Events.Entities.Abstract.Evenement;
import com.example.Events.Entities.Concrete.Participant;
import com.example.Events.Exception.*;
import jakarta.xml.bind.ValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class EvenementService {
    private List<Evenement> evenements;
    private final JsonDataService jsonDataService;
    private final NotificationService notificationService;
    private final ParticipantService participantService;
    private final EvenementFactory evenementFactory;

    public EvenementService(JsonDataService jsonDataService,
                            NotificationService notificationService,
                            ParticipantService participantService,
                            EvenementFactory evenementFactory) {
        this.jsonDataService = jsonDataService;
        this.notificationService = notificationService;
        this.participantService = participantService;
        this.evenementFactory = evenementFactory;
        this.evenements = jsonDataService.chargerEvenements();

        // Reconstruction des liens Observer après chargement
        reconstruireLiensObserver();
    }

    private void reconstruireLiensObserver() {
        for (Evenement evenement : evenements) {
            for (String participantId : evenement.getParticipantIds()) {
                participantService.obtenirParticipantParId(participantId)
                        .ifPresent(participant -> {
                            evenement.getParticipants().add(participant);
                            evenement.ajouterObserver(participant);
                        });
            }
        }
    }

    public Evenement creerEvenement(String type, String id, String nom, LocalDateTime date,
                                    String lieu, int capaciteMax, Map<String, String> parametresSpecifiques)
            throws EvenementDejaExistantException, ValidationException {

        // Vérifier unicité
        if (evenements.stream().anyMatch(e -> e.getId().equals(id))) {
            throw new EvenementDejaExistantException("Un événement avec l'ID " + id + " existe déjà");
        }

        Evenement evenement = evenementFactory.creerEvenement(type, id, nom, date, lieu, capaciteMax, parametresSpecifiques);
        evenements.add(evenement);
        jsonDataService.sauvegarderEvenements(evenements);

        notificationService.envoyerNotificationAsync(
                "Nouvel événement créé: " + evenement.getNom() + " le " + evenement.getDate()
        );

        return evenement;
    }

    public List<Evenement> obtenirTousLesEvenements() {
        return new ArrayList<>(evenements);
    }

    public Optional<Evenement> obtenirEvenementParId(String id) {
        return evenements.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    public Evenement mettreAJourEvenement(String id, Evenement evenementMisAJour)
            throws EvenementIntrouvableException, ValidationException {

        Optional<Evenement> evenementExistant = obtenirEvenementParId(id);
        if (evenementExistant.isEmpty()) {
            throw new EvenementIntrouvableException("Événement avec l'ID " + id + " non trouvé");
        }

        // Validations
        if (evenementMisAJour.getNom() == null || evenementMisAJour.getNom().trim().isEmpty()) {
            throw new ValidationException("Le nom de l'événement ne peut pas être vide");
        }
        if (evenementMisAJour.getDate() == null || evenementMisAJour.getDate().isBefore(LocalDateTime.now())) {
            throw new ValidationException("La date de l'événement doit être future");
        }
        if (evenementMisAJour.getCapaciteMax() <= 0) {
            throw new ValidationException("La capacité maximale doit être positive");
        }

        Evenement evenement = evenementExistant.get();
        String ancienNom = evenement.getNom();

        evenement.setNom(evenementMisAJour.getNom());
        evenement.setDate(evenementMisAJour.getDate());
        evenement.setLieu(evenementMisAJour.getLieu());
        evenement.setCapaciteMax(evenementMisAJour.getCapaciteMax());

        jsonDataService.sauvegarderEvenements(evenements);

        // Notification asynchrone de mise à jour
        evenement.notifierObservers("L'événement " + ancienNom + " a été mis à jour");
        notificationService.envoyerNotificationAsync(
                "L'événement " + evenement.getNom() + " a été mis à jour"
        );

        return evenement;
    }

    public void supprimerEvenement(String id) throws EvenementIntrouvableException {
        Optional<Evenement> evenement = obtenirEvenementParId(id);
        if (evenement.isEmpty()) {
            throw new EvenementIntrouvableException("Événement avec l'ID " + id + " non trouvé");
        }

        Evenement evt = evenement.get();

        // Notifier les participants avant suppression
        evt.notifierObservers("L'événement " + evt.getNom() + " a été supprimé");

        // Retirer l'événement des listes des participants
        for (String participantId : evt.getParticipantIds()) {
            participantService.retirerEvenementDuParticipant(participantId, id);
        }

        evenements.removeIf(e -> e.getId().equals(id));
        jsonDataService.sauvegarderEvenements(evenements);
    }

    public void inscrireParticipant(String evenementId, String participantId)
            throws EvenementIntrouvableException, ParticipantIntrouvableException,
            CapaciteMaxAtteinteException, ParticipantDejaInscritException {

        Evenement evenement = obtenirEvenementParId(evenementId)
                .orElseThrow(() -> new EvenementIntrouvableException("Événement avec l'ID " + evenementId + " non trouvé"));

        Participant participant = participantService.obtenirParticipantParId(participantId)
                .orElseThrow(() -> new ParticipantIntrouvableException("Participant avec l'ID " + participantId + " non trouvé"));

        if (evenement.getParticipantIds().contains(participantId)) {
            throw new ParticipantDejaInscritException("Le participant est déjà inscrit à cet événement");
        }

        evenement.ajouterParticipant(participant);
        participantService.ajouterEvenementAuParticipant(participantId, evenementId);

        // Sauvegarde immédiate des deux fichiers
        jsonDataService.sauvegarderEvenements(evenements);

        // Notification asynchrone
        notificationService.envoyerNotificationAsync(
                "Inscription confirmée pour " + participant.getNom() + " à l'événement: " + evenement.getNom()
        );

        System.out.println("✅ Participant " + participant.getNom() + " inscrit à " + evenement.getNom());
    }

    public void desinscrireParticipant(String evenementId, String participantId)
            throws EvenementIntrouvableException, ParticipantIntrouvableException {

        Evenement evenement = obtenirEvenementParId(evenementId)
                .orElseThrow(() -> new EvenementIntrouvableException("Événement avec l'ID " + evenementId + " non trouvé"));

        Participant participant = participantService.obtenirParticipantParId(participantId)
                .orElseThrow(() -> new ParticipantIntrouvableException("Participant avec l'ID " + participantId + " non trouvé"));

        evenement.retirerParticipant(participant);
        participantService.retirerEvenementDuParticipant(participantId, evenementId);

        // Sauvegarde immédiate des deux fichiers
        jsonDataService.sauvegarderEvenements(evenements);

        System.out.println("✅ Participant " + participant.getNom() + " désinscrit de " + evenement.getNom());
    }

    public List<Evenement> rechercherEvenementsParNom(String nom) {
        return evenements.stream()
                .filter(e -> e.getNom().toLowerCase().contains(nom.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Evenement> rechercherEvenementsParLieu(String lieu) {
        return evenements.stream()
                .filter(e -> e.getLieu().toLowerCase().contains(lieu.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Evenement> rechercherEvenementsParDateApres(LocalDateTime date) {
        return evenements.stream()
                .filter(e -> e.getDate().isAfter(date))
                .collect(Collectors.toList());
    }
    public List<Evenement> obtenirEvenementsAvecPlacesDisponibles() {
        return evenements.stream()
                .filter(e -> e.getParticipants().size() < e.getCapaciteMax())
                .collect(Collectors.toList());
    }

    public List<Evenement> obtenirEvenementsParType(Class<? extends Evenement> type) {
        return evenements.stream()
                .filter(type::isInstance)
                .collect(Collectors.toList());
    }

    public Map<String, Long> statistiquesParticipationParMois() {
        return evenements.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getDate().getMonth().toString(),
                        Collectors.summingLong(e -> e.getParticipants().size())
                ));
    }

    public List<Evenement> obtenirEvenementsPopulaires(int seuilParticipants) {
        return evenements.stream()
                .filter(e -> e.getParticipants().size() >= seuilParticipants)
                .sorted((e1, e2) -> Integer.compare(e2.getParticipants().size(), e1.getParticipants().size()))
                .collect(Collectors.toList());
    }

    public void annulerEvenement(String id) throws EvenementIntrouvableException {
        Evenement evenement = obtenirEvenementParId(id)
                .orElseThrow(() -> new EvenementIntrouvableException("Événement avec l'ID " + id + " non trouvé"));

        evenement.annuler(); // Notifie automatiquement les observers
        supprimerEvenement(id);
    }

    public CompletableFuture<Void> diffuserAnnonce(String message) {
        List<CompletableFuture<Void>> notifications = evenements.stream()
                .flatMap(e -> e.getParticipants().stream())
                .distinct()
                .map(p -> notificationService.envoyerNotificationAsync(
                        "Annonce générale pour " + p.getNom() + ": " + message))
                .collect(Collectors.toList());

        return CompletableFuture.allOf(notifications.toArray(new CompletableFuture[0]));
    }
}