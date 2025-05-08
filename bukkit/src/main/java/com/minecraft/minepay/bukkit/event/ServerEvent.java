package com.minecraft.minepay.bukkit.event;

import com.minecraft.minepay.bukkit.BukkitCore;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
public class ServerEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public void call() {
        if (!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(BukkitCore.getInstance(), this::callServerEvent);
            return;
        }

        callServerEvent();
    }

    private void callServerEvent() {
        Bukkit.getPluginManager().callEvent(this);
    }

}
