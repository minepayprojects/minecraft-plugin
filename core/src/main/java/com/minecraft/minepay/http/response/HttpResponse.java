package com.minecraft.minepay.http.response;

import com.minecraft.minepay.http.request.HttpRequest;
import com.minecraft.minepay.util.json.JsonUtil;

public abstract class HttpResponse extends JsonUtil {

    public abstract HttpResponse parse(HttpRequest request);



}
