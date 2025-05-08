package com.minecraft.minepay.bukkit.menu.list;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.account.Account;
import com.minecraft.minepay.bukkit.event.inventory.InventoryShopCategoryMenuOpenEvent;
import com.minecraft.minepay.bukkit.event.player.PlayerBuyProductEvent;
import com.minecraft.minepay.bukkit.menu.Menu;
import com.minecraft.minepay.bukkit.util.Util;
import com.minecraft.minepay.bukkit.util.item.ItemBuilder;
import com.minecraft.minepay.http.data.category.CategoryData;
import com.minecraft.minepay.http.data.gateway.GatewayData;
import com.minecraft.minepay.http.data.product.ProductData;
import com.minecraft.minepay.http.data.shop.ShopData;
import com.minecraft.minepay.http.data.store.StoreData;
import com.minecraft.minepay.http.data.transaction.TransactionData;
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
public class MyTransactionsMenu extends Menu {

    private final StoreData storeData;

    private final int page;

    public MyTransactionsMenu(StoreData storeData, int page) {
        super("Minepay - Meus pedidos", 6 * 9);

        this.storeData = storeData;
        this.page = page;
    }

    @Override
    public void buildInventory(Player player) {
        Account account = Account.getAccount(player.getUniqueId());

        account.loadTransactions(getStoreData().getId(), acc -> {

            List<ItemStack> itemStacks = new ArrayList<>();

            for (TransactionData transaction : acc.getTransactions()) {
                ItemStack builder = new ItemBuilder(Material.PAPER)
                        .name(ColorUtil.GREEN + "Transação #" + transaction.getId())
                        .lore("Produto: " + ColorUtil.WHITE + transaction.getProductData().getName() + ColorUtil.GRAY +
                                "\nGateway: " + ColorUtil.WHITE + transaction.getGatewayData().getName() + ColorUtil.GRAY +
                                "\n\nStatus: " + ColorUtil.WHITE + transaction.getStatus())
                        .build();

                itemStacks.add(builder);
            }

            setPaginate(getPage(), itemStacks, true, player);
        });

        setItem(45, new ItemBuilder(Material.ARROW).name(ColorUtil.RED + "<-- Voltar").build());
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
    }
}
