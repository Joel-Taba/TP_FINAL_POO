package com.example.Events.Entities.Abstract;
import com.example.Events.Entities.Concrete.Concert;
import com.example.Events.Entities.Concrete.Conference;
import com.example.Events.Entities.Concrete.Participant;
import com.example.Events.Exception.CapaciteMaxAtteinteException;
import com.example.Events.Observer.EvenementObservable;
import com.example.Events.Observer.ParticipantObserver;
import lombok.*;
import com.fasterxml.jackson.annotation.*;
import java.time.LocalDateTime;
import java.util.*;


@Data

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Conference.class, name = "conference"),
        @JsonSubTypes.Type(value = Concert.class, name = "concert")
})
@Getter
@Setter
public abstract class Evenement implements EvenementObservable {
    protected String id;
    protected String nom;
    protected LocalDateTime date;
    protected String lieu;
    protected int capaciteMax;
    @JsonIgnore
    protected List<Participant> participants = new ArrayList<>();
    @JsonIgnore
    protected List<ParticipantObserver> observers = new ArrayList<>();
    protected List<String> participantIds = new ArrayList<>();

    public void ajouterParticipant(Participant participant) throws CapaciteMaxAtteinteException {
        if (participants.size() >= capaciteMax) {
            throw new CapaciteMaxAtteinteException("Capacité maximale atteinte pour l'événement " + nom);
        }
        if (!participants.contains(participant)) {
            participants.add(participant);
            participantIds.add(participant.getId());
            ajouterObserver(participant);
        }
    }

    public void retirerParticipant(Participant participant) {
        participants.remove(participant);
        participantIds.remove(participant.getId());
        retirerObserver(participant);
    }

    public void annuler() {
        notifierObservers("L'événement " + nom + " a été annulé");
    }

    public abstract void afficherDetails();

    // Observer Pattern Implementation
    @Override
    public void ajouterObserver(ParticipantObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void retirerObserver(ParticipantObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifierObservers(String message) {
        for (ParticipantObserver observer : observers) {
            observer.mettreAJour(message);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evenement evenement = (Evenement) o;
        return Objects.equals(id, evenement.id);
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<ParticipantObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<ParticipantObserver> observers) {
        this.observers = observers;
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<String> participantIds) {
        this.participantIds = participantIds;
    }

    public Evenement() {
    }

    public Evenement(String id, String nom, LocalDateTime date, String lieu, int capaciteMax, List<Participant> participants, List<ParticipantObserver> observers, List<String> participantIds) {
        this.id = id;
        this.nom = nom;
        this.date = date;
        this.lieu = lieu;
        this.capaciteMax = capaciteMax;
        this.participants = participants;
        this.observers = observers;
        this.participantIds = participantIds;
    }
}