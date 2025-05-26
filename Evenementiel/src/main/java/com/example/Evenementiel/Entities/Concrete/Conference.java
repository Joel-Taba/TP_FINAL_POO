package com.example.Evenementiel.Entities.Concrete;

import com.example.Evenementiel.Entities.Abstract.Evenement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;
import java.util.List;

public class Conference extends Evenement {
    private String theme;
    private List<String> intervenants;

    public Conference(){
        super("", "",LocalDateTime.now(),"", 0 );
    }

    public Conference(String id, String nom, LocalDateTime date, String lieu, int capaciteMax,
                      String theme, List<String> intervenants) {
        super(id, nom, date, lieu, capaciteMax);
        this.theme = theme;
        this.intervenants = intervenants;
    }
}
