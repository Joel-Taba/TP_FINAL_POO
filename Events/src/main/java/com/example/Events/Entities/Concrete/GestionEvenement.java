package com.example.Events.Entities.Concrete;
import com.example.Events.Entities.Abstract.Evenement;
import com.example.Events.Exception.CapaciteMaxAtteinteException;
import com.example.Events.Exception.EvenementDejaExistantException;
import lombok.*;

import java.util.Map;

@Getter
@Setter
public class GestionEvenement {
    private Map<String, Evenement> evenements;

    public void ajouterEvenement(Evenement e) throws EvenementDejaExistantException {
        if (evenements.containsKey(e.getId())) {
            throw new EvenementDejaExistantException("L'événement existe déjà !");
        }
        this.evenements.put(e.getId(),e);
    }
    public void supprimerEvenement(){}
    public void rechercherEvenement(){}

    public void ajouterParticipant(Evenement event, Participant p) throws CapaciteMaxAtteinteException {
        if (event.getParticipants().size() >= event.getCapaciteMax()) {
            throw new CapaciteMaxAtteinteException("Capacité maximale atteinte !");
        }
        event.ajouterParticipant(p);
    }

    private GestionEvenement(Map<String, Evenement> evenements){
        this.evenements = evenements;
    }


    public Map<String, Evenement> getEvenements() {
        return evenements;
    }

    public void setEvenements(Map<String, Evenement> evenements) {
        this.evenements = evenements;
    }


}
