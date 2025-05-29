package com.example.Events.Service;

import com.example.Events.Entities.Abstract.Evenement;
import com.example.Events.Entities.Concrete.Concert;
import com.example.Events.Entities.Concrete.Conference;
import jakarta.xml.bind.ValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class EvenementFactory {

    public Evenement creerEvenement(String type, String id, String nom,
                                    LocalDateTime date, String lieu, int capaciteMax,
                                    Map<String, String> parametresSpecifiques) throws ValidationException {

        // Validations communes
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("L'ID de l'événement ne peut pas être vide");
        }
        if (nom == null || nom.trim().isEmpty()) {
            throw new ValidationException("Le nom de l'événement ne peut pas être vide");
        }
       /* if (date == null || date.isBefore(LocalDateTime.now())) {
            throw new ValidationException("La date de l'événement doit être future");
        }*/
        if (capaciteMax <= 0) {
            throw new ValidationException("La capacité maximale doit être positive");
        }

        switch (type.toLowerCase()) {
            case "conference":
                String theme = parametresSpecifiques.get("theme");
                if (theme == null || theme.trim().isEmpty()) {
                    throw new ValidationException("Le thème de la conférence est obligatoire");
                }
                return new Conference(id, nom, date, lieu, capaciteMax, theme);

            case "concert":
                String artiste = parametresSpecifiques.get("artiste");
                String genre = parametresSpecifiques.get("genre musical");
                if (artiste == null || artiste.trim().isEmpty()) {
                    throw new ValidationException("L'artiste du concert est obligatoire");
                }
                if (genre == null || genre.trim().isEmpty()) {
                    throw new ValidationException("Le genre musical est obligatoire");
                }
                return new Concert(id, nom, date, lieu, capaciteMax, artiste, genre);

            default:
                throw new IllegalArgumentException("Type d'événement non supporté: " + type);
        }
    }
}
