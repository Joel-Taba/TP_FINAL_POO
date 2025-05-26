package com.example.Evenementiel.Repository;

import com.example.Evenementiel.Entities.Abstract.Evenement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, String> {
    List<Evenement> findByDateBetween(LocalDateTime debut, LocalDateTime fin);
    List<Evenement> findByLieuContainingIgnoreCase(String lieu);

    @Query("SELECT e FROM Evenement e WHERE e.capaciteMax - SIZE(e.inscriptions) >= :placesLibres")
    List<Evenement> findEvenementsAvecPlacesLibres(@Param("placesLibres") int placesLibres);
}