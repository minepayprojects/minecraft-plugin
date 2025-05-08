package com.minecraft.minepay.http.response.list;

import com.minecraft.minepay.http.request.HttpRequest;
import com.minecraft.minepay.http.response.HttpResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultResponse extends HttpResponse {

    private boolean success;

    private String message;

    @Override
    public DefaultResponse parse(HttpRequest request) {
        setSuccess(request.isSuccess());
        setMessage(!request.isSuccess() ? request.getErrorMessage() : request.getMessage());
        return this;
    }
}
