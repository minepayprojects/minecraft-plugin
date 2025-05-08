package com.minecraft.minepay.bukkit.menu.list;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.bukkit.event.inventory.InventoryShopMenuOpenEvent;
import com.minecraft.minepay.bukkit.menu.Menu;
import com.minecraft.minepay.bukkit.util.item.ItemBuilder;
import com.minecraft.minepay.http.data.store.StoreData;
import com.minecraft.minepay.util.color.ColorUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class StoresMenu extends Menu {

    public StoresMenu() {
        super("Minepay: Stores", 3 * 9);
    }

    @Override
    public void buildInventory(Player player) {
        List<StoreData> stores = Core.getStoreController().getStores();

        int slot = 9;

        for (StoreData store : Core.getStoreController().getStores()) {
            ItemBuilder itemBuilder = new ItemBuilder(Material.PAPER)
                    .name(ColorUtil.GREEN + store.getName())
                    .lore("Clique para visualizar as categorias e os produtos da loja");

            setItem(slot += 1, itemBuilder.build());
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();

        ItemStack itemStack = getInventoryApi().getItems().get(slot);

        if (itemStack == null) {
            return;
        }

        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);

        String storeName = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());

        StoreData storeData = Core.getStoreController().getStoreByName(storeName);

        if (storeData == null) {
            player.sendMessage(ColorUtil.RED + "Ocorreu um erro ao obter a loja informada.");
            return;
        }

        InventoryShopMenuOpenEvent inventoryShopMenuOpenEvent = new InventoryShopMenuOpenEvent(player);
        inventoryShopMenuOpenEvent.call();

        if (!inventoryShopMenuOpenEvent.isCancelled()) {
            Menu.open(player, new ShopMenu(storeData));
        }
    }
}
