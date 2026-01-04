package com.lld.lld.bookMyShow.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Theatre {
    private String name;
    private List<Screen> screens;
    private Location location;
}
