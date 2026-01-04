package com.lld.lld.pen.interfaces;

import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.strategy.WritingStrategy;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public abstract class Pen {
    private String name;
    private String brand;
    private String colour;
    private Double price;
    private PenType type;
    private WritingStrategy writingStrategy;
    public void write() {
        writingStrategy.write();
    }
}
