package com.example.Evenementiel.Entities.Concrete;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class Intervenant {
    private String nom;
    private String domaine;

    public Intervenant(String nom, String domaine) {
        this.nom = nom;
        this.domaine = domaine;
    }
}