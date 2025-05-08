package com.minecraft.minepay.bukkit.menu.list;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.bukkit.event.inventory.InventoryProductsMenuOpenEvent;
import com.minecraft.minepay.bukkit.menu.Menu;
import com.minecraft.minepay.bukkit.util.Util;
import com.minecraft.minepay.bukkit.util.item.ItemBuilder;
import com.minecraft.minepay.http.data.category.CategoryData;
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

import java.util.List;

@Getter
public class ShopCategoryMenu extends Menu {

    private final StoreData storeData;

    private final ShopData shopData;

    private final int shopSlot;

    public ShopCategoryMenu(StoreData storeData, ShopData shopData, int shopSlot) {
        super("Minepay - " + storeData.getName() + ": " + shopData.getName(), 6 * 9);

        this.storeData = storeData;
        this.shopData = shopData;
        this.shopSlot = shopSlot;
    }

    @Override
    public void buildInventory(Player player) {
        List<ShopData> shops = Core.getShopController().getShopsByStoreId(getStoreData().getId());

        int slot = 0;

        for (ShopData shop : shops) {
            ItemBuilder itemBuilder = new ItemBuilder(Util.findItemStackByMaterialName(shop.getDisplay_in_game()))
                    .name(ColorUtil.GREEN + shop.getName())
                    .lore("Clique para visualizar as categorias");

            setItem(slot += 1, itemBuilder.build());
        }

        setItem(7, new ItemBuilder(Material.BOOK).name(ColorUtil.GREEN + "Meus pedidos").lore(ColorUtil.YELLOW + "Clique para ver seus pedidos").build());

        for (int i = 9; i < 18; i++) {
            setItem(i, new ItemBuilder(Util.findItemStackByMaterialName("160")).durability(7).name(ColorUtil.RED).build());
        }

        setItem((getShopSlot() + 9), new ItemBuilder(Util.findItemStackByMaterialName("160")).durability(5).name(ColorUtil.RED).build());

        List<CategoryData> categories = Core.getCategoryController().getCategories(getStoreData().getId(), getShopData().getId());

        if (categories.isEmpty()) {
            setItem(31, new ItemBuilder(Material.BARRIER).name(ColorUtil.BOLD_C + "Erro!").lore("Não há nenhum produto cadastrado para a categoria").build());
        } else {

            slot = 27;
            for (CategoryData category : categories) {
                ItemBuilder itemBuilder = new ItemBuilder(Util.findItemStackByMaterialName(category.getDisplay_ingame()))
                        .name(ColorUtil.GREEN + category.getName())
                        .lore(category.getDescription_ingame());

                setItem(slot += 1, itemBuilder.build());
            }
        }

        setItem(45, new ItemBuilder(Material.ARROW).name(ColorUtil.RED + "<-- Voltar").build());

        shops.clear();
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
            Menu.open(player, new StoresMenu());
            return;
        }

        if (hasDisplayName(itemStack, ColorUtil.GREEN + "Meus pedidos")) {
            Menu.open(player, new MyTransactionsMenu(getStoreData(), 1));
            return;
        }

        String itemName = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());

        ShopData shopName = Core.getShopController().getShopByName(itemName);

        if (shopName != null) {
            Menu.open(player, new ShopCategoryMenu(getStoreData(), shopData, slot));
            return;
        }

        CategoryData categoryData = Core.getCategoryController().getCategory(itemName);

        if (categoryData == null) {
            return;
        }

        InventoryProductsMenuOpenEvent inventoryProductsMenuOpenEvent = new InventoryProductsMenuOpenEvent(player);
        inventoryProductsMenuOpenEvent.call();

        if (!inventoryProductsMenuOpenEvent.isCancelled()) {
            Menu.open(player, new ProductsMenu(getStoreData(), getShopData(), categoryData, 1));
        }
    }
}
