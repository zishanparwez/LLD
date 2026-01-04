package com.lld.lld.pen.multipleClass;

import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.models.Ink;
import com.lld.lld.pen.models.Nib;
import com.lld.lld.pen.models.Refill;

import lombok.Getter;

@Getter
public class FountainPen extends Pen {
    private Ink ink;
    private Nib nib;

    public FountainPen(String name, String brand, String colour, Double price, Ink ink, Nib nib) {
        super(name, brand, colour, price, PenType.FOUNTAIN);
        this.ink = ink;
        this.nib = nib;
    }

    @Override
    public void write() {
        System.out.println("Fountain Pen is Writing.");
    }

    @Override
    public void changeRefill(Refill refill) {
        throw new UnsupportedOperationException("Fountain Pen cannot be refilled");
    }

    public void changeInk(Ink ink) {
        this.ink = ink;
    }
}
