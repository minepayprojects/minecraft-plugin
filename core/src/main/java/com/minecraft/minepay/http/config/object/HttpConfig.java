package com.minecraft.minepay.http.config.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class HttpConfig {

    private final String name;

    private final Object value;

}
