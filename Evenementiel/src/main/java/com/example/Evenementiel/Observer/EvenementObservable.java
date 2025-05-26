package com.example.Evenementiel.Observer;
import java.util.ArrayList;
import java.util.List;

public class EvenementObservable {
    private final List<ParticipantObserver> observers = new ArrayList<>();

    public void ajouterObserver(ParticipantObserver observer) {
        observers.add(observer);
    }

    public void supprimerObserver(ParticipantObserver observer) {
        observers.remove(observer);
    }

    public void notifierTous(String message) {
        for (ParticipantObserver observer : observers) {
            observer.notifier(message);
        }
    }
}