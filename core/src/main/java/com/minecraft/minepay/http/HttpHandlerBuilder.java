package com.minecraft.minepay.http;

import com.minecraft.minepay.http.body.HttpBody;
import com.minecraft.minepay.http.params.HttpParams;
import com.minecraft.minepay.http.type.HttpType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HttpHandlerBuilder {

    private final HttpType type;

    private final String path;

    private final HttpBody body;

    private final HttpParams params;

    private final boolean minepay;

    private final String token;

    private final String tokenFinder;

}