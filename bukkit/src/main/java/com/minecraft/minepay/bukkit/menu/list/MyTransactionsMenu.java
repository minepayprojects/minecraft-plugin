package com.minecraft.minepay.bukkit.menu.list;

import com.minecraft.minepay.account.Account;
import com.minecraft.minepay.bukkit.menu.Menu;
import com.minecraft.minepay.bukkit.util.item.ItemBuilder;
import com.minecraft.minepay.http.data.store.StoreData;
import com.minecraft.minepay.http.data.transaction.TransactionCreatedData;
import com.minecraft.minepay.http.data.transaction.TransactionData;
import com.minecraft.minepay.util.color.ColorUtil;
import com.minecraft.minepay.util.time.TimeUtils;
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
                StringBuilder stringBuilder = new StringBuilder();

                stringBuilder.append("Produto: ").append(ColorUtil.WHITE).append(transaction.getProductData().getName()).append(ColorUtil.GRAY).append("\n");
                stringBuilder.append("Gateway: ").append(ColorUtil.WHITE).append(transaction.getGatewayData().getName()).append(ColorUtil.GRAY).append("\n");
                stringBuilder.append("Criado em: ").append(ColorUtil.WHITE).append(TimeUtils.formatAPI(transaction.getCreatedAt())).append(ColorUtil.GRAY).append("\n");

                if (transaction.getStatus().equalsIgnoreCase("Cancelado")) {
                    stringBuilder.append("Expirado em: ").append(ColorUtil.WHITE).append(TimeUtils.formatAPI(transaction.getExpireAt())).append(ColorUtil.GRAY).append("\n");
                }

                stringBuilder.append("\n").append("Status: ").append(ColorUtil.WHITE).append(transaction.getStatus()).append(ColorUtil.GRAY).append("\n");

                stringBuilder.append("Status detalhado: ").append(ColorUtil.WHITE).append(transaction.getStatusDetail()).append(ColorUtil.GRAY);

                ItemStack builder = new ItemBuilder(Material.PAPER)
                        .name(ColorUtil.GREEN + "Transação #" + transaction.getId())
                        .lore(stringBuilder.toString())
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

        Account account = Account.getAccount(player.getUniqueId());

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

        int id = Integer.parseInt(itemName.split("#")[1]);

        TransactionData transaction = account.getTransactions().stream().filter(transactionData -> transactionData.getId() == id).findFirst().orElse(null);

        if (transaction == null) {
            return;
        }

        if (transaction.getStatus().equalsIgnoreCase("Aguardando pagamento")) {
            player.closeInventory();

            account.getExecutor().sendTransactionCreated(new TransactionCreatedData(transaction.getQrCode(), transaction.getQrCodeBase64(), transaction.getTicketUrl()));
        }
    }
}
