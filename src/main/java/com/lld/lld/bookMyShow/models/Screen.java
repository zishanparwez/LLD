package com.lld.lld.bookMyShow.models;

import java.util.List;

import com.lld.lld.bookMyShow.enums.AudioType;
import com.lld.lld.bookMyShow.enums.ScreenType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Screen {
    private ScreenType screenType;
    private AudioType audioType;
    private String name;
    private List<Show> shows;
    private List<Seat> seats;
}
