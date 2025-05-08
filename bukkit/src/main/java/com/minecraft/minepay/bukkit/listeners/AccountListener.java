package com.minecraft.minepay.bukkit.listeners;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.account.Account;
import com.minecraft.minepay.bukkit.BukkitCore;
import com.minecraft.minepay.bukkit.account.BukkitAccountExecutor;
import com.minecraft.minepay.bukkit.event.player.PlayerBuyProductEvent;
import com.minecraft.minepay.bukkit.menu.Menu;
import com.minecraft.minepay.bukkit.menu.list.StoresMenu;
import com.minecraft.minepay.bukkit.util.Util;
import com.minecraft.minepay.bukkit.util.map.MapCreator;
import com.minecraft.minepay.http.data.store.StoreData;
import com.minecraft.minepay.util.color.ColorUtil;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
public class AccountListener implements Listener {

    private final BukkitCore instance = BukkitCore.getInstance();

    @EventHandler (priority = EventPriority.LOW, ignoreCancelled = true)
    public synchronized void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!event.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)) {
            return;
        }

        UUID uniqueId = event.getUniqueId();

        String username = event.getName();

        Account account = new Account(uniqueId, username, new BukkitAccountExecutor(uniqueId));

        Core.getAccountController().register(uniqueId, account);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            return;
        }

        Player player = event.getPlayer();

        ItemStack itemStack = event.getItem();
        if (itemStack == null) {
            return;
        }

        if (itemStack.getType().equals(Material.MAP) && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(MapCreator.getMapName())) {
            event.setCancelled(true);
            return;
        }

        if (!getInstance().getBukkitConfiguration().isDebug()) {
            return;
        }

        if (itemStack.getType().equals(Material.DIAMOND)) {
            Menu.open(player, new StoresMenu());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Core.getAccountController().unregister(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerBuyProduct(PlayerBuyProductEvent event) {
        Player player = event.getPlayer();

        int hotbar = event.getHotbar();

        if (hotbar == -1) {
            for (int id = 0; id < 9; id++) {
                ItemStack hotbatItemStack = player.getInventory().getItem(id);

                if (hotbatItemStack == null || hotbatItemStack.getType().equals(Material.AIR)) {
                    hotbar = id;
                    break;
                }
            }
        }

        if (hotbar == -1) {
            player.sendMessage(ColorUtil.RED + "Não há espaço em sua barra de ação (hotbar) para gerar o mapa de pagamento com o QR Code.");
            return;
        }

        event.setHotbar(hotbar);

        StoreData storeData = Core.getStoreController().getStoreById(event.getStoreId());

        if (storeData == null) {
            Core.getLogger().error("Ocorreu um erro ao efetuar a aquisição do produto #%d pois a loja informada #%d não foi encontrada!", event.getProductId(), event.getStoreId());

            player.sendMessage(ColorUtil.RED + "Ocorreu um erro ao efetuar a aquisição de seu produto");
            return;
        }

        if (storeData.getGateways().stream().anyMatch(gatewayData -> gatewayData.getName().startsWith("CentralCart"))) {

            if (event.getCoupon() == null && !event.isNoCoupon()) {
                player.sendMessage("========================================");
                player.sendMessage(ColorUtil.WHITE + "Possui " + ColorUtil.GREEN + "cupom" + ColorUtil.WHITE + " de desconto para efetuar a compra?");
                player.sendMessage(ColorUtil.WHITE + "Se sim, é só digitar o cupom para ser aplicado a sua compra.");
                player.sendMessage(ColorUtil.WHITE + "Caso não possua, é só digitar " + ColorUtil.RED + "não" + ColorUtil.WHITE + " para prosseguir com a compra.");
                player.sendMessage("========================================");

                getInstance().getCouponSelect().add(player.getUniqueId());
                getInstance().getEventBuy().put(player.getUniqueId(), event);

                return;
            }
        }

        player.sendMessage(ColorUtil.GREEN + "Criando sua transação, aguarde alguns segundos...");

        Account account = Account.getAccount(player.getUniqueId());

        if (event.getCoupon() != null) {
            account.createTransaction(event.getStoreId(), event.getCategoryId(), event.getShopId(), event.getProductId(), event.getGatewayId(), event.getAddress(), event.getCoupon());
            return;
        }

        account.createTransaction(event.getStoreId(), event.getCategoryId(), event.getShopId(), event.getProductId(), event.getGatewayId(), event.getAddress());
        return;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();

        if (!itemStack.getType().equals(Material.MAP)) {
            return;
        }

        if (!itemStack.hasItemMeta()) {
            return;
        }

        if (!itemStack.getItemMeta().hasDisplayName()) {
            return;
        }

        if (!itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(MapCreator.getMapName())) {
            return;
        }

        event.getItemDrop().remove();
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (BukkitCore.getInstance().getCouponSelect().stream().noneMatch(uuid -> uuid.equals(player.getUniqueId()))) {
            return;
        }

        event.setCancelled(true);

        String message = event.getMessage();

        if (message.contains(" ")) {
            player.sendMessage(ColorUtil.RED + "Por favor, informe corretamente o cupom sem espaços.");
            return;
        }

        getInstance().getCouponSelect().remove(player.getUniqueId());

        PlayerBuyProductEvent cache = getInstance().getEventBuy().get(player.getUniqueId());

        PlayerBuyProductEvent playerBuyProductEvent = getPlayerBuyProductEvent(player, cache, message.equalsIgnoreCase("não") ? null : message);

        playerBuyProductEvent.call();
    }

    private static @NotNull PlayerBuyProductEvent getPlayerBuyProductEvent(Player player, PlayerBuyProductEvent cache, String message) {
        PlayerBuyProductEvent playerBuyProductEvent = new PlayerBuyProductEvent(player);

        playerBuyProductEvent.setStoreId(cache.getStoreId());
        playerBuyProductEvent.setCategoryId(cache.getCategoryId());
        playerBuyProductEvent.setShopId(cache.getShopId());
        playerBuyProductEvent.setProductId(cache.getProductId());
        playerBuyProductEvent.setGatewayId(cache.getGatewayId());
        playerBuyProductEvent.setAddress(cache.getAddress());
        playerBuyProductEvent.setHotbar(cache.getHotbar());

        if (message != null) {
            playerBuyProductEvent.setCoupon(message);
        } else {
            playerBuyProductEvent.setNoCoupon(true);
        }

        return playerBuyProductEvent;
    }

}
