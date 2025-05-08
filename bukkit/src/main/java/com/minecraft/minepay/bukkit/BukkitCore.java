package com.minecraft.minepay.bukkit;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.bukkit.command.MinepayCommand;
import com.minecraft.minepay.bukkit.config.BukkitConfiguration;
import com.minecraft.minepay.bukkit.event.player.PlayerBuyProductEvent;
import com.minecraft.minepay.bukkit.http.BukkitHttpExecutor;
import com.minecraft.minepay.bukkit.listeners.AccountListener;
import com.minecraft.minepay.bukkit.listeners.InventoryListener;
import com.minecraft.minepay.bukkit.menu.MenuController;
import com.minecraft.minepay.bukkit.store.BukkitStoreExecutor;
import com.minecraft.minepay.bukkit.task.AsyncDeliveryProductTask;
import com.minecraft.minepay.bukkit.task.AsyncRefundProductTask;
import com.minecraft.minepay.http.HttpHandler;
import com.minecraft.minepay.http.config.HttpConfiguration;
import com.minecraft.minepay.store.StoreController;
import com.minecraft.minepay.util.logger.FormattedLogger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class BukkitCore extends JavaPlugin {

    @Getter
    private static BukkitCore instance;

    @Setter
    private BukkitConfiguration bukkitConfiguration;

    @Setter
    private MenuController menuController;

    private final HashMap<UUID, PlayerBuyProductEvent> eventBuy = new HashMap<>();

    private final Set<UUID> couponSelect = new HashSet<>();

    @Override
    public void onLoad() {
        super.onLoad();

        instance = this;

        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        setMenuController(new MenuController());
        setConfiguration();

        Core.setLogger(new FormattedLogger(getLogger(), "Minepay"));
        Core.setStoreController(new StoreController(new BukkitStoreExecutor()));
    }

    @Override
    public void onEnable() {
        super.onEnable();

        // HandleCommands
        getCommand("minepay").setExecutor(new MinepayCommand());

        // Events
        Bukkit.getPluginManager().registerEvents(new AccountListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(this), this);

        // Task

        // Delivery a cada 8 segundos
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new AsyncDeliveryProductTask(), 20 * 8, 20 * 8);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new AsyncRefundProductTask(), 20 * 8, 20 * 8);

        Core.getStoreController().loadStores();
    }

    @Override
    public void onDisable() {
        super.onDisable();

        getCouponSelect().clear();
        getEventBuy().clear();
    }

    public void setConfiguration() {
        File dataFolder = getDataFolder();

        setBukkitConfiguration(BukkitConfiguration.load(new File(dataFolder, "config.json")));
        Core.setHttpHandler(new HttpHandler(HttpConfiguration.load(dataFolder + File.separator + "http.json"), new BukkitHttpExecutor(this)));
    }

}
