package com.minecraft.minepay.http.executor;

import com.minecraft.minepay.http.request.HttpRequest;

import java.util.function.Consumer;

public abstract class HttpExecutor {

    public abstract void run(HttpRequest request, Consumer<HttpRequest> consumer);
}
