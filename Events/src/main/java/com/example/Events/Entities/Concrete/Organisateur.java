package com.example.Events.Entities.Concrete;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class Organisateur extends Participant {
    private List<String> evenementsOrganises = new ArrayList<>();

    public Organisateur(String id, String nom, String email) {
        super(id, nom, email);
        this.evenementsOrganises = new ArrayList<>();
    }

    public void ajouterEvenementOrganise(String evenementId) {
        if (!evenementsOrganises.contains(evenementId)) {
            evenementsOrganises.add(evenementId);
        }
    }

    public void retirerEvenementOrganise(String evenementId) {
        evenementsOrganises.remove(evenementId);
    }

    public List<String> getEvenementsOrganises() {
        return evenementsOrganises;
    }

    public void setEvenementsOrganises(List<String> evenementsOrganises) {
        this.evenementsOrganises = evenementsOrganises;
    }

    public Organisateur(String id, String nom, String email, List<String> evenementIds, List<String> evenementsOrganises) {
        super(id, nom, email, evenementIds);
        this.evenementsOrganises = evenementsOrganises;
    }

    public Organisateur(List<String> evenementsOrganises) {
        this.evenementsOrganises = evenementsOrganises;
    }

    public Organisateur(String id, String nom, String email, List<String> evenementsOrganises) {
        super(id, nom, email);
        this.evenementsOrganises = evenementsOrganises;
    }
}