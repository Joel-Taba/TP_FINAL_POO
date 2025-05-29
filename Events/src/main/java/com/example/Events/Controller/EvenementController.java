package com.example.Events.Controller;
import com.example.Events.Entities.Abstract.Evenement;
import com.example.Events.Entities.Concrete.Concert;
import com.example.Events.Entities.Concrete.Conference;
import com.example.Events.Exception.*;
import com.example.Events.Service.EvenementService;
import jakarta.xml.bind.ValidationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/evenements")
@CrossOrigin(origins = "*")
public class EvenementController {

    @Autowired
    private EvenementService evenementService;

    @GetMapping
    public ResponseEntity<List<Evenement>> obtenirTousLesEvenements() {
        return ResponseEntity.ok(evenementService.obtenirTousLesEvenements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evenement> obtenirEvenementParId(@PathVariable String id) {
        return evenementService.obtenirEvenementParId(id)
                .map(evenement -> ResponseEntity.ok(evenement))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/creation")
    public ResponseEntity<?> creerEvenement(
            @RequestParam String nom,
            @RequestParam String lieu,
            @RequestParam int capaciteMax,
            @RequestParam(required = false) String theme,
            @RequestParam(required = false) String artiste,
            @RequestParam(required = false) String genreMusical
    ) {
        try {
            if (theme != null) {
                Map<String, String> info = new HashMap<>();
                info.put("theme",theme);
                Evenement evenement = evenementService.creerEvenement(
                        "Conference", UUID.randomUUID().toString(), nom, LocalDateTime.now(), lieu, capaciteMax, info
                );
                return ResponseEntity.status(HttpStatus.OK).body(evenement);
            } else if (artiste != null && genreMusical != null) {
                Map<String, String> info = new HashMap<>();
                info.put("artiste", artiste);
                info.put("genre musical",genreMusical);
                Evenement evenement = evenementService.creerEvenement(
                        "Concert", UUID.randomUUID().toString(), nom, LocalDateTime.now(), lieu, capaciteMax, info
                );
                return ResponseEntity.status(HttpStatus.OK).body(evenement);
            }
        } catch (EvenementDejaExistantException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse() {
                @Override
                public HttpStatusCode getStatusCode() {
                    return null;
                }

                @Override
                public ProblemDetail getBody() {
                    return null;
                }
            });
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> mettreAJourEvenement(@PathVariable String id, @RequestBody Evenement evenement) {
        try {
            Evenement evenementMisAJour = evenementService.mettreAJourEvenement(id, evenement);
            return ResponseEntity.ok(evenementMisAJour);
        } catch (EvenementIntrouvableException | ValidationException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerEvenement(@PathVariable String id) {
        try {
            evenementService.supprimerEvenement(id);
            return ResponseEntity.noContent().build();
        } catch (EvenementIntrouvableException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{evenementId}/participants/{participantId}")
    public ResponseEntity<?> inscrireParticipant(@PathVariable String evenementId, @PathVariable String participantId) {
        try {
            evenementService.inscrireParticipant(evenementId, participantId);
            return ResponseEntity.ok().build();
        } catch (EvenementIntrouvableException | ParticipantIntrouvableException e) {
            return ResponseEntity.notFound().build();
        } catch (CapaciteMaxAtteinteException | ParticipantDejaInscritException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse() {
                @Override
                public HttpStatusCode getStatusCode() {
                    return null;
                }

                @Override
                public ProblemDetail getBody() {
                    return null;
                }
            });
        }
    }

    @DeleteMapping("/{evenementId}/participants/{participantId}")
    public ResponseEntity<?> desinscrireParticipant(@PathVariable String evenementId, @PathVariable String participantId) {
        try {
            evenementService.desinscrireParticipant(evenementId, participantId);
            return ResponseEntity.ok().build();
        } catch (EvenementIntrouvableException | ParticipantIntrouvableException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<Evenement>> rechercherEvenements(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String lieu,
            @RequestParam(required = false) String type) {

        List<Evenement> resultats = evenementService.obtenirTousLesEvenements();

        if (nom != null && !nom.isEmpty()) {
            resultats = evenementService.rechercherEvenementsParNom(nom);
        }
        if (lieu != null && !lieu.isEmpty()) {
            resultats = resultats.stream()
                    .filter(e -> e.getLieu().toLowerCase().contains(lieu.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (type != null && !type.isEmpty()) {
            if ("conference".equalsIgnoreCase(type)) {
                resultats = resultats.stream()
                        .filter(e -> e instanceof Conference)
                        .collect(Collectors.toList());
            } else if ("concert".equalsIgnoreCase(type)) {
                resultats = resultats.stream()
                        .filter(e -> e instanceof Concert)
                        .collect(Collectors.toList());
            }
        }

        return ResponseEntity.ok(resultats);
    }

    @GetMapping("/statistiques")
    public ResponseEntity<Map<String, Object>> obtenirStatistiques() {
        Map<String, Object> stats = new HashMap<>();
        List<Evenement> evenements = evenementService.obtenirTousLesEvenements();

        stats.put("totalEvenements", evenements.size());
        stats.put("totalParticipants", evenements.stream()
                .mapToInt(e -> e.getParticipants().size())
                .sum());
        stats.put("evenementsAvecPlaces", evenementService.obtenirEvenementsAvecPlacesDisponibles().size());
        stats.put("participationParMois", evenementService.statistiquesParticipationParMois());

        return ResponseEntity.ok(stats);
    }

    @PostMapping("/annonce")
    public ResponseEntity<String> diffuserAnnonce(@RequestParam String message) {
        CompletableFuture<Void> diffusion = evenementService.diffuserAnnonce(message);
        diffusion.thenRun(() -> System.out.println("Diffusion reussie"));
        return ResponseEntity.ok("Annonce en cours de diffusion");
    }
}

