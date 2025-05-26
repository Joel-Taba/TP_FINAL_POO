package com.example.Evenementiel.Observer;

import com.example.Evenementiel.Entities.Abstract.Evenement;

public interface ParticipantObserver {
    void notifier(String message);
}
