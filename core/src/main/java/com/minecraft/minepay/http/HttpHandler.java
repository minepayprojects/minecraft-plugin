package com.minecraft.minepay.http;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.http.body.HttpBody;
import com.minecraft.minepay.http.config.HttpConfiguration;
import com.minecraft.minepay.http.config.object.HttpConfig;
import com.minecraft.minepay.http.executor.HttpExecutor;
import com.minecraft.minepay.http.request.HttpRequest;
import com.minecraft.minepay.http.response.list.UserAuthenticateResponse;
import com.minecraft.minepay.http.type.HttpType;
import lombok.Getter;

import java.util.function.Consumer;

@Getter
public class HttpHandler {

    private final HttpConfiguration configuration;

    private final HttpExecutor executor;

    public HttpHandler(HttpConfiguration configuration, HttpExecutor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    public void execute(HttpHandlerBuilder builder) {
        execute(builder, true, null);
    }

    public void execute(HttpHandlerBuilder builder, Consumer<HttpRequest> consumer) {
        execute(builder, true, consumer);
    }

    public void execute(HttpHandlerBuilder builder, boolean async, Consumer<HttpRequest> consumer) {
        String url = getConfiguration().getBaseUrl() + builder.getPath();

        if (builder.getParams() != null) {
            url = url + builder.getParams().build();
        }

        var token = builder.getToken();
        if (builder.getTokenFinder() != null) {
            token = fetchToken(builder.getTokenFinder());
        }

        HttpRequest request = new HttpRequest(url, builder.getType(), token);

        request.setMinepayAPI(builder.isMinepay());

        if (builder.getBody() != null) {

            if (!builder.getType().isBody()) {
                throw new Error("O tipo da requisição informado não suporta body");
            }

            request = request.body(builder.getBody());
        }

        if (consumer != null) {

            if (async) {
                getExecutor().run(request, consumer);
                return;
            }

            consumer.accept(request.request());
            return;
        }

        request.request();
    }

    public HttpRequest authenticateStore(int storeId) {
        String url = getConfiguration().getBaseUrl() + "/user/authenticate/store";

        HttpBody body = HttpBody.create()
                .add("storeId", storeId)
                .add("tokenPersist", true);

        HttpRequest request = new HttpRequest(url, HttpType.POST, getConfiguration().getUserToken());

        request.setMinepayAPI(true);
        request.body(body);

        return request.request();
    }

    private String fetchToken(String tokenConfig) {
        String[] split = tokenConfig.split(":");

        int storeId = Integer.parseInt(split[1]);

        HttpConfig config = getConfiguration().getConfigs().stream().filter(httpConfig -> httpConfig.getName().equalsIgnoreCase(tokenConfig)).findFirst().orElse(null);

        // Token não encontrado
        if (config == null) {
            HttpRequest request = authenticateStore(storeId);

            UserAuthenticateResponse response = new UserAuthenticateResponse().parse(request);

            if (!response.isSuccess()) {
                Core.getLogger().error("Ocorreu um erro ao gerar o token de acesso para a loja informada: #%s. Mais informações: '%s'", storeId, response.getMessage());
                return null;
            }

            getConfiguration().getConfigs().add(new HttpConfig(tokenConfig, response.getToken()));
            getConfiguration().save();

            return response.getToken();
        }

        return config.getValue().toString();
    }

}
