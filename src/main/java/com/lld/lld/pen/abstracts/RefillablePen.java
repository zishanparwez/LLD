package com.lld.lld.pen.abstracts;

import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.models.Refill;
import com.lld.lld.pen.strategy.WritingStrategy;

import lombok.Getter;

@Getter
public abstract class RefillablePen extends Pen {

    private Refill refill;
    
    public RefillablePen(String name, String brand, String colour, Double price, PenType penType, Refill refill, WritingStrategy writingStrategy) {
        super(name, brand, colour, price, penType, writingStrategy);
        this.refill = refill;
        this.refill.setRefillable(true);
    }

    public void changeRefill(Refill refill) {
        if(this.refill.getRefillable()) {
            this.refill = refill;
        }
    }

    public abstract Boolean isRefillable();
    
}
