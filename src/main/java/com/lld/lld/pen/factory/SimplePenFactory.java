package com.lld.lld.pen.factory;

import com.lld.lld.pen.enums.InkType;
import com.lld.lld.pen.enums.NibType;
import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.enums.RefillType;
import com.lld.lld.pen.interfaces.FountainPen;
import com.lld.lld.pen.interfaces.GelPen;
import com.lld.lld.pen.interfaces.Pen;
import com.lld.lld.pen.models.Ink;
import com.lld.lld.pen.models.Nib;
import com.lld.lld.pen.models.Refill;
import com.lld.lld.pen.strategy.RoughWritingStrategy;
import com.lld.lld.pen.strategy.SmoothWritingStrategy;

public class SimplePenFactory {
    public static Pen createPen(PenType penType) {
        switch(penType) {
            case GEL:
                Ink gelink = Ink.builder().colour("blue").inkType(InkType.BALL).build();
                Nib gelnib = Nib.builder().nibType(NibType.SILVER).radius(0.5).build();
                Refill refill = Refill.builder().refillType(RefillType.BALL).refillable(true).ink(gelink).nib(gelnib).build();
                return GelPen.builder()
                        .name("Butter Flow")
                        .brand("Cello")
                        .colour("Blue")
                        .price(10.0)
                        .type(PenType.GEL)    
                        .writingStrategy(new SmoothWritingStrategy())
                        .refill(refill)
                        .build();
            case BALL:
                // return new BallPen();
            case FOUNTAIN:
                Ink fountainink = Ink.builder().colour("black").inkType(InkType.FOUNTAIN).build();
                Nib fountainnib = Nib.builder().nibType(NibType.GOLD).radius(0.8).build();
                return FountainPen.builder()
                        .name("Parker")
                        .brand("Parker")
                        .colour("Black")
                        .price(20.0)
                        .type(PenType.FOUNTAIN)    
                        .writingStrategy(new RoughWritingStrategy())
                        .nib(fountainnib)
                        .ink(fountainink)
                        .build();
            case USEANDTHORW:
                // return new UseAndThrowPen();
            default:
                throw new IllegalArgumentException("Invalid Pen Type");
        }
    }
}