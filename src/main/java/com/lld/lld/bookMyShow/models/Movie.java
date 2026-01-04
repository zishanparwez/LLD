package com.lld.lld.bookMyShow.models;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Movie {
    private String name;
    private Integer rating;
    private List<String> languages;
}
