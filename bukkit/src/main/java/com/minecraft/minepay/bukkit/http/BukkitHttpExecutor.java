package com.minecraft.minepay.bukkit.http;

import com.minecraft.minepay.bukkit.BukkitCore;
import com.minecraft.minepay.http.executor.HttpExecutor;
import com.minecraft.minepay.http.request.HttpRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
public class BukkitHttpExecutor extends HttpExecutor {

    private final BukkitCore instance;

    @Override
    public void run(HttpRequest request, Consumer<HttpRequest> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(getInstance(), () -> consumer.accept(request.request()));
    }
}
