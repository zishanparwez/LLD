package com.lld.lld.pen.singleClass;

import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.models.Ink;
import com.lld.lld.pen.models.Refill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Pen {
    private String name;
    private String colour;
    private Double price;
    private String brand;
    private Refill refill;
    private PenType penType;

    public void write() {
        switch(this.penType) {
            case GEL:
                System.out.println("Gel pen is writing");
                break;
            case BALL:
                System.out.println("Ball pen is writing");
                break;
            case FOUNTAIN:
                System.out.println("Fountaion pen is writing");
                break;
            case USEANDTHORW:
                System.out.println("Use and throw pen is writing");
                break;
            default:
                throw new IllegalArgumentException("Invalid Pen Type");
        }
    }

    public void changeRefill(Refill refill) {
        if(this.refill.getRefillable()) {
            this.refill = refill;
        }
    }

    public void changeInk(Ink ink) {
        this.refill.setInk(ink);
    }

}
