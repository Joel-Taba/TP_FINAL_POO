package com.example.Events.Service;
import java.util.concurrent.CompletableFuture;

public interface NotificationService {
    void envoyerNotification(String message);
    CompletableFuture<Void> envoyerNotificationAsync(String message);
}