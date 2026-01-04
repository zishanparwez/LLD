package com.lld.lld.pen.multipleClass;

import com.lld.lld.pen.enums.PenType;
import com.lld.lld.pen.models.Refill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public abstract class Pen {
    private String name;
    private String brand;
    private String colour;
    private Double price;
    private PenType penType;
    public abstract void write();
    public abstract void changeRefill(Refill refill);
}
