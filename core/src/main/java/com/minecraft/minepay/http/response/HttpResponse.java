package com.minecraft.minepay.http.response;

import com.minecraft.minepay.http.request.HttpRequest;

public abstract class HttpResponse {

    public abstract HttpResponse parse(HttpRequest request);

}
