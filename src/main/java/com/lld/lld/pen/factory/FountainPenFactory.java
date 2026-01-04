package com.lld.lld.pen.factory;

import com.lld.lld.pen.enums.InkType;
import com.lld.lld.pen.enums.NibType;
import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.interfaces.FountainPen;
import com.lld.lld.pen.interfaces.Pen;
import com.lld.lld.pen.models.Ink;
import com.lld.lld.pen.models.Nib;
import com.lld.lld.pen.strategy.RoughWritingStrategy;

public class FountainPenFactory extends AbstractPenFactory {

    @Override
    public Pen createPen() {
        Ink ink = Ink.builder().colour("black").inkType(InkType.FOUNTAIN).build();
        Nib nib = Nib.builder().nibType(NibType.GOLD).radius(0.8).build();
        return FountainPen.builder()
                .name("Parker")
                .brand("Parker")
                .colour("Black")
                .price(20.0)
                .type(PenType.FOUNTAIN)    
                .writingStrategy(new RoughWritingStrategy())
                .nib(nib)
                .ink(ink)
                .build();
    }
    
}
