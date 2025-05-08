package com.minecraft.minepay.http.body.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class HttpBodyObject {

    private final String key;

    private final HttpBodyDataObject data;

    @Override
    public String toString() {
        return String.format("\"%s\":%s", getKey(), getData().toString());
    }
}
