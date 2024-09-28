package com.kakaogroom6.server.domain.place.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Cloud {
    BLUE("blue"),
    RED("red"),
    GREY("grey");

    private final String cloud;
}
