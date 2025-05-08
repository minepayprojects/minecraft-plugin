package com.minecraft.minepay.bukkit.menu.list;

import com.minecraft.minepay.bukkit.event.player.PlayerBuyProductEvent;
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
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@Getter
public class ConfirmBuyMenu extends Menu {

    private final StoreData storeData;

    private final CategoryData categoryData;

    private final ShopData shopData;

    private final ProductData productData;

    private final GatewayData gatewayData;

    public ConfirmBuyMenu(StoreData storeData, CategoryData categoryData, ShopData shopData, ProductData productData, GatewayData gatewayData) {
        super("Minepay - Confirmar compra", 6 * 9);

        this.storeData = storeData;
        this.categoryData = categoryData;
        this.shopData = shopData;
        this.productData = productData;
        this.gatewayData = gatewayData;
    }

    @Override
    public void buildInventory(Player player) {

        // Product
        setItem(12, new ItemBuilder(Util.findItemStackByMaterialName(getProductData().getDisplay_ingame()))
                .name(ColorUtil.GREEN + getProductData().getName())
                .lore(getProductData().getDescription_ingame()).build());

        // Gateway
        setItem(14, new ItemBuilder(Util.findItemStackByMaterialName(getGatewayData().getName()))
                .name(ColorUtil.GREEN + getGatewayData().getName()).build());

        // Confirm
        setItem(29, new ItemBuilder(Util.findItemStackByMaterialName("35"))
                .durability(13)
                .name(ColorUtil.GREEN + "Confirmar")
                .lore("Será gerado um QR Code para pagamento para comprar este produto.").build());

        // Cancel
        setItem(33, new ItemBuilder(Util.findItemStackByMaterialName("35"))
                .durability(14)
                .name(ColorUtil.RED + "Cancelar")
                .lore("Você cancelará a compra deste produto").build());

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
            Menu.open(player, new ShopMenu(getStoreData()));
            return;
        }

        if (hasDisplayName(itemStack, ColorUtil.RED + "Cancelar")) {
            player.sendMessage(ColorUtil.GREEN + "Operação cancelada com sucesso.");

            Menu.open(player, new ProductsMenu(getStoreData(), getShopData(), getCategoryData(), 1));
            return;
        }

        if (hasDisplayName(itemStack, ColorUtil.GREEN + "Confirmar")) {

            player.closeInventory();

            PlayerBuyProductEvent playerBuyProductEvent = new PlayerBuyProductEvent(player);

            playerBuyProductEvent.setStoreId(getStoreData().getId());
            playerBuyProductEvent.setCategoryId(getCategoryData().getId());
            playerBuyProductEvent.setShopId(getShopData().getId());
            playerBuyProductEvent.setProductId(getProductData().getId());
            playerBuyProductEvent.setGatewayId(getGatewayData().getId());
            playerBuyProductEvent.setAddress(Util.getAddress(player));

            playerBuyProductEvent.call();

            return;
        }
    }
}
