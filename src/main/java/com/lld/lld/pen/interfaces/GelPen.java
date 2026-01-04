package com.lld.lld.pen.interfaces;

import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.models.Refill;
import com.lld.lld.pen.strategy.SmoothWritingStrategy;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class GelPen extends Pen implements RefillablePen {


    private Refill refill;

    // public GelPen(String name, String brand, String colour, Double price, Refill refill) {
    //     super(name, brand, colour, price, PenType.GEL, new SmoothWritingStrategy());
    //     this.refill = refill;
    // }

    @Override
    public void changeRefill(Refill refill) {
        if(this.isRefillable()) {
            this.refill = refill;
        }
    }

    @Override
    public Refill getRefill() {
       return refill;
    }

    @Override
    public Boolean isRefillable() {
        return getRefill().getRefillable();
    }
    
}
