package com.example.Events.Observer;

public interface EvenementObservable {
    void ajouterObserver(ParticipantObserver observer);
    void retirerObserver(ParticipantObserver observer);
    void notifierObservers(String message);
}