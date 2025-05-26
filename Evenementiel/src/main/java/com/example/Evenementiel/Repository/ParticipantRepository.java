package com.example.Evenementiel.Repository;
import com.example.Evenementiel.Entities.Concrete.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Integer> {
    // Requête personnalisée éventuelle : findByEmail(String email)
}
