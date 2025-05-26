package com.example.Evenementiel.Entities.Concrete;

import com.example.Evenementiel.Entities.Abstract.Evenement;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

public class Concert extends Evenement {
    private String artiste;
    private String genreMusical;

    public Concert(){
        super("", "",LocalDateTime.now(),"", 0 );
    }

    public String getArtiste() {
        return artiste;
    }

    public String getGenreMusical() {
        return genreMusical;
    }

    public Concert(String id, String nom, LocalDateTime date, String lieu, int capaciteMax,
                   String artiste, String genreMusical) {
        super(id, nom, date, lieu, capaciteMax);
        this.artiste = artiste;
        this.genreMusical = genreMusical;
    }
}
