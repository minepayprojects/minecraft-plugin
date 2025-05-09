package com.minecraft.minepay.bukkit.menu;

import com.minecraft.minepay.account.Account;
import com.minecraft.minepay.bukkit.BukkitCore;
import com.minecraft.minepay.bukkit.util.Util;
import com.minecraft.minepay.bukkit.util.inventory.InventoryApi;
import com.minecraft.minepay.bukkit.util.item.ItemBuilder;
import com.minecraft.minepay.http.data.product.ProductData;
import com.minecraft.minepay.util.color.ColorUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@RequiredArgsConstructor
@Getter
public abstract class Menu {

    private final BukkitCore instance = BukkitCore.getInstance();

    private final String title;

    private final int slot;

    private final InventoryApi inventoryApi;

    public Menu(String title, int slot) {
        this.title = title;
        this.slot = slot;
        this.inventoryApi = new InventoryApi(getSlot(), getTitle());
    }

    public abstract void buildInventory(Player player);

    public abstract void onInventoryClick(InventoryClickEvent event);

    public void setItem(int slot, ItemStack itemStack) {
        getInventoryApi().setItem(slot, itemStack);
    }

    public boolean hasDisplayName(ItemStack itemStack, String name) {
        return itemStack.getItemMeta() != null && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(name);
    }

    public void setPaginate(int page, List<ItemStack> items) {
        setPaginate(page, items, false, null);
    }

    public ItemBuilder getProductItem(ProductData product) {
        String stringBuilder = product.getDisplay_ingame() + "\n\n" +
                ColorUtil.GRAY + "Preço: " + ColorUtil.WHITE + "R$" + product.getPrice();

        return new ItemBuilder(Util.findItemStackByMaterialName(product.getDisplay_ingame()))
                .name(ColorUtil.GREEN + product.getName())
                .lore(stringBuilder);
    }

    public void setPaginate(int page, List<ItemStack> items, boolean inventoryOpened, Player player) {
        int perPage = 21, maxPage = (int) Math.ceil((double) items.size() / perPage);
        int start = 0, end = perPage;

        if (page > 1) {
            start = ((page - 1) * perPage);
            end = (page * perPage);
        }

        if (end > items.size()) {
            end = items.size();
        }

        int slot = 11;

        for (int i = start; i < end; i++) {
            ItemStack itemStack = items.get(i);

            setItem(slot, itemStack);

            if (slot % 9 == 6) {
                slot += 5;
                continue;
            }

            slot += 1;
        }

        if (page != 1) {
            setItem(19, new ItemBuilder(Material.ARROW).name(ColorUtil.GREEN + "Página " + (page - 1)).build());
        }

        if ((page + 1) <= maxPage) {
            setItem(25, new ItemBuilder(Material.ARROW).name(ColorUtil.GREEN + "Página " + (page + 1)).build());
        }

        if (inventoryOpened && player != null) {
            getInventoryApi().updateInventory(player);
        }
    }

    public static void open(Player player, Menu menu) {
        menu.buildInventory(player);
        menu.getInventoryApi().openInventory(player);

        BukkitCore.getInstance().getMenuController().getMenus().put(player.getUniqueId(), menu);
    }

}
