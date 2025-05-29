package com.example.Events.Service;
import org.springframework.stereotype.Service;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

// Service de Notification
@Service
public class NotificationImplementation implements NotificationService {

    @Override
    public void envoyerNotification(String message) {
        System.out.println("Notification envoyée: " + message);
        // Simulation d'envoi d'email/SMS
    }

    @Override
    public CompletableFuture<Void> envoyerNotificationAsync(String message) {
        return CompletableFuture.runAsync(() -> {
            try {
                // Simulation d'un délai d'envoi
                TimeUnit.SECONDS.sleep(1);
                envoyerNotification(message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Erreur lors de l'envoi de notification", e);
            }
        });
    }
}
