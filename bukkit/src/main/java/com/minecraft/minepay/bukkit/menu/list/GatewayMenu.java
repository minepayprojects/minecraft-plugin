package com.minecraft.minepay.bukkit.menu.list;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.bukkit.event.inventory.InventoryConfirmBuyMenuOpenEvent;
import com.minecraft.minepay.bukkit.event.inventory.InventoryGatewayMenuOpenEvent;
import com.minecraft.minepay.bukkit.menu.Menu;
import com.minecraft.minepay.bukkit.util.Util;
import com.minecraft.minepay.bukkit.util.item.ItemBuilder;
import com.minecraft.minepay.http.data.category.CategoryData;
import com.minecraft.minepay.http.data.gateway.GatewayData;
import com.minecraft.minepay.http.data.product.ProductData;
import com.minecraft.minepay.http.data.shop.ShopData;
import com.minecraft.minepay.http.data.store.StoreData;
import com.minecraft.minepay.util.color.ColorUtil;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.type.Gate;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GatewayMenu extends Menu {

    private final StoreData storeData;

    private final CategoryData categoryData;

    private final ShopData shopData;

    private final ProductData productData;

    public GatewayMenu(StoreData storeData, CategoryData categoryData, ShopData shopData, ProductData productData) {
        super("Minepay - Gateways", 4 * 9);

        this.storeData = storeData;
        this.categoryData = categoryData;
        this.shopData = shopData;
        this.productData = productData;
    }

    @Override
    public void buildInventory(Player player) {

        int size = getStoreData().getGateways().size();

        int slot = size == 3 ? 11 : size == 2 ? 12 : 13;

        int slotAdd = size > 1 ? 1 : 0;

        for (GatewayData gateway : getStoreData().getGateways()) {
            ItemBuilder builder = new ItemBuilder(Util.findItemStackByMaterialName(gateway.getName()))
                    .name(ColorUtil.GREEN + gateway.getName());

            setItem(slot, builder.build());

            slot += slotAdd;
        }

        setItem(27, new ItemBuilder(Material.ARROW).name(ColorUtil.RED + "<-- Voltar").build());
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();

        ItemStack itemStack = getInventoryApi().getItems().get(slot);

        if (itemStack == null) {
            return;
        }

        player.playSound(player, Sound.UI_BUTTON_CLICK, 1.0f, 2.0f);

        if (hasDisplayName(itemStack, ColorUtil.RED + "<-- Voltar")) {
            Menu.open(player, new ShopMenu(getStoreData()));
            return;
        }

        String itemName = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());

        GatewayData gatewayData = getStoreData().getGatewayByName(itemName);

        if (gatewayData == null) {
            return;
        }

        InventoryConfirmBuyMenuOpenEvent inventoryConfirmBuyMenuOpenEvent = new InventoryConfirmBuyMenuOpenEvent(player);
        inventoryConfirmBuyMenuOpenEvent.call();

        if (!inventoryConfirmBuyMenuOpenEvent.isCancelled()) {
            Menu.open(player, new ConfirmBuyMenu(getStoreData(), getCategoryData(), getShopData(), getProductData(), gatewayData));
        }
    }
}
