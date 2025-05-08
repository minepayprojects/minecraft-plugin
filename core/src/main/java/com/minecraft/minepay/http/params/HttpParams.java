package com.minecraft.minepay.http.params;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class HttpParams {

    private final HashMap<String, Object> params = new HashMap<>();

    public static HttpParams create() {
        return new HttpParams();
    }

    public HttpParams add(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public String build() {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, Object> entry : params.entrySet()) {


            builder.append(builder.isEmpty() ? "?" : "&").append(entry.getKey()).append("=").append(entry.getValue());
        }

        return builder.toString();
    }

}
