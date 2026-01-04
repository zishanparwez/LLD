package com.lld.lld.pen.models;

import com.lld.lld.pen.enums.NibType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Nib {
    private NibType nibType;
    private Double radius;
}
