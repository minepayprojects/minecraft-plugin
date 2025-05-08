package com.minecraft.minepay.http.request;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.minecraft.minepay.Core;
import com.minecraft.minepay.http.body.HttpBody;
import com.minecraft.minepay.http.body.object.HttpBodyObject;
import com.minecraft.minepay.http.result.HttpResult;
import com.minecraft.minepay.http.type.HttpType;
import com.minecraft.minepay.util.http.HTTPRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
public class HttpRequest {

    private final String url;

    private final HttpType type;

    private final String token;

    public HttpRequest(String url, HttpType type) {
        this(url, type, null);
    }

    public HttpRequest(String url, HttpType type, String token) {
        this.url = url;
        this.type = type;
        this.token = token;
    }

    @Setter
    private int code;

    @Setter
    private String errorMessage;

    @Setter
    private String message;

    @Setter
    private HttpResult result;

    @Setter
    private JsonObject jsonObject;

    @Setter
    private JsonArray jsonArray;

    @Setter
    private HttpBody body;

    @Setter
    private boolean minepayAPI = true;

    public boolean isSuccess() {
        return getResult().equals(HttpResult.SUCCESS);
    }

    public HttpRequest body(HttpBody body) {
        setBody(body);
        return this;
    }

    public final HttpRequest request() {
        HTTPRequest request = null;

        try {
            // Initializing http request
            request = new HTTPRequest(getUrl(), getType().name())
                    .connectTimeout(5000)
                    .readTimeout(6500)
                    .contentType("application/json")
                    .userAgent("Minepay/1.0.0");

            // Token
            if (getToken() != null) request = request.authorization(getToken());

            // AcceptJson
            request = request.acceptJson();

            // Body
            if (getBody() != null) request.sendBody(getBody().toString());

            // Code
            setCode(request.code());

            if (isMinepayAPI()) {
                JsonObject jsonObject = JsonParser.parseReader(request.reader()).getAsJsonObject();

                String message = jsonObject.get("message").getAsString();

                if (jsonObject.has("err")) {
                    message += " (" + jsonObject.get("err").getAsString() + ")";
                }

                boolean success = jsonObject.get("success").getAsBoolean();

                setResult(!success ? HttpResult.UNSUCCESSFUL : HttpResult.SUCCESS);
                setJsonObject(jsonObject);

                if (!success) {
                    throw new Error(message);
                }

                setMessage(message);
                return this;
            }

            setResult(HttpResult.SUCCESS);

            if (!request.isBodyEmpty()) {
                JsonElement jsonElement = JsonParser.parseReader(request.reader());

                if (!jsonElement.isJsonNull()) {

                    if (jsonElement.isJsonArray()) {
                        setJsonArray(jsonElement.getAsJsonArray());
                    }

                    if (jsonElement.isJsonObject()) {
                        setJsonObject(jsonElement.getAsJsonObject());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

            setResult(HttpResult.ERROR);
            setErrorMessage(e.getMessage());
        } finally {
            if (request != null) {
                request.disconnect();
            }
        }

        return this;
    }

}
