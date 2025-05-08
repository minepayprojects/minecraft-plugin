package com.minecraft.minepay.bukkit.menu.list;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.bukkit.event.inventory.InventoryGatewayMenuOpenEvent;
import com.minecraft.minepay.bukkit.menu.Menu;
import com.minecraft.minepay.bukkit.util.Util;
import com.minecraft.minepay.bukkit.util.item.ItemBuilder;
import com.minecraft.minepay.http.data.category.CategoryData;
import com.minecraft.minepay.http.data.product.ProductData;
import com.minecraft.minepay.http.data.shop.ShopData;
import com.minecraft.minepay.http.data.store.StoreData;
import com.minecraft.minepay.util.color.ColorUtil;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProductsMenu extends Menu {

    private final StoreData storeData;

    private final ShopData shopData;

    private final CategoryData categoryData;

    private final int page;

    public ProductsMenu(StoreData storeData, ShopData shopData, CategoryData categoryData, int page) {
        super("Minepay - Produtos (" + page + "/" + (int) Math.ceil((double) categoryData.getProducts().size() / 21) + ")", 6 * 9);

        this.storeData = storeData;
        this.shopData = shopData;
        this.categoryData = categoryData;
        this.page = page;
    }

    @Override
    public void buildInventory(Player player) {

        List<ItemStack> items = new ArrayList<>();

        for (ProductData product : getCategoryData().getProducts()) {
            ItemBuilder builder = new ItemBuilder(Util.findItemStackByMaterialName(product.getDisplay_ingame()))
                    .name(ColorUtil.GREEN + product.getName())
                    .lore(product.getDescription_ingame());

            items.add(builder.build());
        }

        if (items.isEmpty()) {
            setItem(22, new ItemBuilder(Material.BARRIER).name(ColorUtil.RED).lore("Não foi encontrado produtos cadastrados para esta categoria.").build());
        }

        setPaginate(page, items);

        setItem(45, new ItemBuilder(Material.ARROW).name(ColorUtil.RED + "<-- Voltar").build());

        items.clear();
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

        if (slot == 19) {
            Menu.open(player, new ProductsMenu(getStoreData(), getShopData(), getCategoryData(), (page - 1)));
            return;
        }

        if (slot == 25) {
            Menu.open(player, new ProductsMenu(getStoreData(), getShopData(), getCategoryData(), (page + 1)));
            return;
        }

        String itemName = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());

        ProductData productData = getCategoryData().getProductByName(itemName);

        if (productData == null) {
            return;
        }

        // Call event for open Gateway Inventory
        InventoryGatewayMenuOpenEvent inventoryGatewayMenuOpenEvent = new InventoryGatewayMenuOpenEvent(player);
        inventoryGatewayMenuOpenEvent.call();

        if (!inventoryGatewayMenuOpenEvent.isCancelled()) {
            Menu.open(player, new GatewayMenu(getStoreData(), getCategoryData(), getShopData(), productData));
        }
    }
}
