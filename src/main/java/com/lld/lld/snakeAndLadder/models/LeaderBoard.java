package com.lld.lld.snakeAndLadder.models;

import java.util.HashMap;
import java.util.Map;

public class LeaderBoard {
    private final Map<String, Integer> positions = new HashMap<>();

    public void update(Player player, int position) {
        positions.put(player.getName(), position);
    }

    public void print() {
        System.out.println("=== LeaderBoard ===");
        positions.forEach((name, pos) -> System.out.println(name + " -> " + pos));
        System.out.println("===================");
    }
}