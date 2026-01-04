package com.lld.lld.tictactoe.models;

import com.lld.lld.tictactoe.enums.Symbol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Player {
    private String name;
    private Symbol symbol;
}
