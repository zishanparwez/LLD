package com.lld.lld.pen.models;

import com.lld.lld.pen.enums.InkType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Ink {
    private String colour;
    private InkType inkType;
}
