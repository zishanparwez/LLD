package com.lld.lld.pen.abstracts;

import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.strategy.WritingStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public abstract class Pen {
    private String name;
    private String brand;
    private String colour;
    private Double price;
    private PenType penType;
    private WritingStrategy writingStrategy;
    public void write() {
        this.writingStrategy.write();
    }
}
