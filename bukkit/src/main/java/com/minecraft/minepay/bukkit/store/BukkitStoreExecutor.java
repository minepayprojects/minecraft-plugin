package com.minecraft.minepay.bukkit.store;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.bukkit.BukkitCore;
import com.minecraft.minepay.store.executor.StoreExecutor;
import org.bukkit.Bukkit;

public class BukkitStoreExecutor extends StoreExecutor {

    @Override
    public void loadStores(boolean async) {
        if (async) {
            Core.getStoreController().loadStores();
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(BukkitCore.getInstance(), () -> Core.getStoreController().loadStores());
    }
}
