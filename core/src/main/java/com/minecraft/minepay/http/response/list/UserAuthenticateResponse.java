package com.minecraft.minepay.http.response.list;

import com.google.gson.JsonObject;
import com.minecraft.minepay.Core;
import com.minecraft.minepay.http.request.HttpRequest;
import com.minecraft.minepay.http.response.HttpResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthenticateResponse extends HttpResponse {

    private boolean success;

    private String message;

    private String token;

    private String err;

    @Override
    public UserAuthenticateResponse parse(HttpRequest request) {
        setSuccess(request.isSuccess());
        setMessage(!request.isSuccess() ? request.getErrorMessage() : request.getMessage());

        if (!request.isSuccess()) {
            try {
                setErr(request.getJsonObject().get("err").getAsString());
            } catch (Exception e) {
                setErr(request.getMessage());
            }
        }

        if (request.isSuccess()) {
            JsonObject jsonObject = request.getJsonObject();

            JsonObject data = jsonObject.get("data").getAsJsonObject();

            setToken(data.get("token").getAsString());
        }

        return this;
    }
}
