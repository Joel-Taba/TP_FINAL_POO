package com.example.Events.Entities.Concrete;

import com.example.Events.Observer.ParticipantObserver;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@Getter
@Setter
public class Participant implements ParticipantObserver {
    protected String id;
    protected String nom;
    protected String email;
    protected List<String> evenementIds = new ArrayList<>();

    public Participant(String id, String nom, String email) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.evenementIds = new ArrayList<>();
    }

    @Override
    public void mettreAJour(String message) {
        System.out.println("Notification pour " + nom + " (" + email + "): " + message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getEvenementIds() {
        return evenementIds;
    }

    public void setEvenementIds(List<String> evenementIds) {
        this.evenementIds = evenementIds;
    }

    public Participant(String id, String nom, String email, List<String> evenementIds) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.evenementIds = evenementIds;
    }

    public Participant() {
    }
}