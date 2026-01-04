package com.lld.lld.tictactoe.observer;

import java.util.ArrayList;
import java.util.List;

import com.lld.lld.tictactoe.models.Game;

public abstract class GameSubject {
    private final List<GameObserver> observers = new ArrayList<>();

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (GameObserver observer : observers) {
            // Pass 'this' which is the Game instance
            observer.update((Game) this);
        }
    }
}