package com.example.Evenementiel.Repository;

import com.example.Evenementiel.Entities.Concrete.Intervenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntervenantRepository extends JpaRepository<Intervenant, Long> {
    // Tu peux ajouter ici des méthodes personnalisées, ex: findByEmail(String email)
}
