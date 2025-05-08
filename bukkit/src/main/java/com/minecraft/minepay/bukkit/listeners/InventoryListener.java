package com.minecraft.minepay.bukkit.listeners;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.account.Account;
import com.minecraft.minepay.bukkit.BukkitCore;
import com.minecraft.minepay.bukkit.menu.Menu;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

@RequiredArgsConstructor
@Getter
public class InventoryListener implements Listener {

    private final BukkitCore instance;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        Menu menu = getInstance().getMenuController().getMenus().get(player.getUniqueId());

        if (menu == null) {
            return;
        }

        if (!menu.getTitle().startsWith(event.getView().getTitle())) {
            return;
        }

        event.setCancelled(true);

        menu.onInventoryClick(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getPlayer();

        Menu menu = getInstance().getMenuController().getMenus().get(player.getUniqueId());

        if (menu == null) {
            return;
        }

        if (!event.getPlayer().getOpenInventory().getTitle().startsWith(menu.getTitle())) {
            return;
        }

        getInstance().getMenuController().getMenus().remove(player.getUniqueId());
    }

}
