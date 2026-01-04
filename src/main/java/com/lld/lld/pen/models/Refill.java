package com.lld.lld.pen.models;

import com.lld.lld.pen.enums.RefillType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Refill {
    private RefillType refillType;
    private Ink ink;
    private Nib nib;
    private Double radius;
    private Boolean refillable;
}
