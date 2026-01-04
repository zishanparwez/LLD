package com.lld.lld.pen.multipleClass;

import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.models.Refill;

import lombok.Getter;

@Getter
public class UseAndThrowPen extends Pen {

    private Refill refill;

    public UseAndThrowPen(String name, String brand, String colour, Double price, Refill refill) {
        super(name, brand, colour, price, PenType.USEANDTHORW);
        this.refill = refill;
    }

    @Override
    public void write() {
        System.out.println("Use and Throw pen is writing.");
    }

    @Override
    public void changeRefill(Refill refill) {
        throw new UnsupportedOperationException("Use and throw pen can't be refilled.");
    }
    
}
