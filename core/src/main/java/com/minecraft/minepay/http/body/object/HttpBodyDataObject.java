package com.minecraft.minepay.http.body.object;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;

@Builder
@Getter
public class HttpBodyDataObject {

    private Object value;

    private Integer[] integers;

    public String getValue() {
        if (getIntegers() != null) {
            return Arrays.toString(getIntegers());
        }

        return value.toString();
    }

    @Override
    public String toString() {
        if (getIntegers() != null) {
            return Arrays.toString(getIntegers());
        }

        return String.format("\"%s\"", getValue());
    }
}
