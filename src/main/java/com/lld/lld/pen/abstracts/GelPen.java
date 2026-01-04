package com.lld.lld.pen.abstracts;

import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.models.Refill;
import com.lld.lld.pen.strategy.SmoothWritingStrategy;

public class GelPen extends RefillablePen {

    public GelPen(String name, String brand, String colour, Double price, Refill refill) {
        super(name, brand, colour, price, PenType.GEL, refill, new SmoothWritingStrategy());
    }

    @Override
    public Boolean isRefillable() {
        return getRefill().getRefillable();
    }
}
