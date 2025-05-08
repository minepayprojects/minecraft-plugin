package com.minecraft.minepay.http.body;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.http.body.object.HttpBodyDataObject;
import com.minecraft.minepay.http.body.object.HttpBodyObject;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HttpBody {

    private final List<HttpBodyObject> bodyObjects = new ArrayList<>();

    public static HttpBody create() {
        return new HttpBody();
    }

    public HttpBody add(String key, Integer[] integers) {
        getBodyObjects().add(new HttpBodyObject(key, HttpBodyDataObject.builder().integers(integers).build()));
        return this;
    }

    public HttpBody add(String key, Object value) {
        getBodyObjects().add(new HttpBodyObject(key, HttpBodyDataObject.builder().value(value).build()));
        return this;
    }

    public String toJson() {
        return Core.GSON.toJson(getBodyObjects());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");

        for (int i = 0; i < getBodyObjects().size(); i++) {
            HttpBodyObject body = getBodyObjects().get(i);

            builder.append(body.toString());

            if (i == (getBodyObjects().size() - 1)) {
                break;
            }

            builder.append(",");
        }

        builder.append("}");

        return builder.toString();
    }

}
