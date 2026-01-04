package com.lld.lld.pen.interfaces;
import com.lld.lld.pen.models.Ink;
import com.lld.lld.pen.models.Nib;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class FountainPen extends Pen implements NonRefillablePen {

    private Nib nib;
    private Ink ink;

    // public FountainPen(String name, String brand, String colour, Double price, Nib nib, Ink ink) {
    //     super(name, brand, colour, price, PenType.FOUNTAIN, new RoughWritingStrategy());
    //     this.nib = nib;
    //     this.ink = ink;
    // }

    @Override
    public void changeInk(Ink ink) {
        this.ink = ink;
    }
    
}
