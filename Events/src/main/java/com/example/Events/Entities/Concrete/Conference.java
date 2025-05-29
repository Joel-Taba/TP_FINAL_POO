package com.example.Events.Entities.Concrete;

import com.example.Events.Entities.Abstract.Evenement;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
@Setter
public class Conference extends Evenement {
    private String theme;
    private List<Intervenant> intervenants = new ArrayList<>();

    public Conference(String id, String nom, LocalDateTime date, String lieu, int capaciteMax, String theme) {
        super(id, nom, date, lieu, capaciteMax, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        this.theme = theme;
        this.intervenants = new ArrayList<>();
    }

    @Override
    public void afficherDetails() {
        System.out.println("=== CONFÉRENCE ===");
        System.out.println("Nom: " + nom);
        System.out.println("Thème: " + theme);
        System.out.println("Date: " + date);
        System.out.println("Lieu: " + lieu);
        System.out.println("Participants: " + participants.size() + "/" + capaciteMax);
        System.out.println("Intervenants: ");
        intervenants.forEach(i -> System.out.println("- " + i.getNom() + " (" + i.getSpecialite() + ")"));
        System.out.println("==================");
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public List<Intervenant> getIntervenants() {
        return intervenants;
    }

    public void setIntervenants(List<Intervenant> intervenants) {
        this.intervenants = intervenants;
    }
}