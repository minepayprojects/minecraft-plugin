package com.minecraft.minepay.http.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HttpType {

    POST(true),
    GET(false),
    PUT(true),
    DELETE(false);

    private final boolean isBody;

}
