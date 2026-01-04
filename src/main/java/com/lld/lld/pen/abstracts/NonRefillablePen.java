package com.lld.lld.pen.abstracts;

import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.models.Ink;
import com.lld.lld.pen.models.Nib;
import com.lld.lld.pen.strategy.WritingStrategy;

public abstract class NonRefillablePen extends Pen {

    private Ink ink;
    private Nib nib;
    public NonRefillablePen(String name, String brand, String colour, Double price, PenType penType, Ink ink, Nib nib, WritingStrategy writingStrategy) {
        super(name, brand, colour, price, penType, writingStrategy);
        this.ink = ink;
        this.nib = nib;
    }

    public void changeInk(Ink ink) {
        this.ink = ink;
    }
    
}
