package com.lld.lld.pen.multipleClass;

import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.models.Refill;

import lombok.Getter;

@Getter
public class GelPen extends Pen {

    private Refill refill;

    public GelPen(String name, String brand, String colour, Double price, Refill refill) {
        super(name, brand, colour, price, PenType.GEL);
        this.refill = refill;
    }

    @Override
    public void write() {
        System.out.println("Gel Pen is Writing.");
    }

    @Override
    public void changeRefill(Refill refill) {
        if(this.refill.getRefillable()) {
            this.refill = refill;
        }
    }
    
}
