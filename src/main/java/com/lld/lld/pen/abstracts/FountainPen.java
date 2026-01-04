package com.lld.lld.pen.abstracts;

import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.models.Ink;
import com.lld.lld.pen.models.Nib;
import com.lld.lld.pen.strategy.RoughWritingStrategy;

public class FountainPen extends NonRefillablePen {

    public FountainPen(String name, String brand, String colour, Double price, Ink ink, Nib nib) {
        super(name, brand, colour, price, PenType.FOUNTAIN, ink, nib, new RoughWritingStrategy());
    }
}
