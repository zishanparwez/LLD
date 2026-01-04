package com.lld.lld.pen.factory;

import com.lld.lld.pen.enums.InkType;
import com.lld.lld.pen.enums.NibType;
import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.enums.RefillType;
import com.lld.lld.pen.interfaces.GelPen;
import com.lld.lld.pen.interfaces.Pen;
import com.lld.lld.pen.models.Ink;
import com.lld.lld.pen.models.Nib;
import com.lld.lld.pen.models.Refill;
import com.lld.lld.pen.strategy.SmoothWritingStrategy;

public class GelPenFactory extends AbstractPenFactory {

    @Override
    public Pen createPen() {

        Ink ink = Ink.builder().colour("blue").inkType(InkType.BALL).build();
        Nib nib = Nib.builder().nibType(NibType.SILVER).radius(0.5).build();
        Refill refill = Refill.builder().refillType(RefillType.BALL).refillable(true).ink(ink).nib(nib).build();
        return GelPen.builder()
                .name("Butter Flow")
                .brand("Cello")
                .colour("Blue")
                .price(10.0)
                .type(PenType.GEL)    
                .writingStrategy(new SmoothWritingStrategy())
                .refill(refill)
                .build();
    }
    
}
