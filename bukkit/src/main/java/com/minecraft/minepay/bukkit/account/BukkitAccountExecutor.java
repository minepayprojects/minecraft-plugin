package com.minecraft.minepay.bukkit.account;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.account.Account;
import com.minecraft.minepay.account.AccountExecutor;
import com.minecraft.minepay.bukkit.BukkitCore;
import com.minecraft.minepay.bukkit.event.player.PlayerDeliveryProductEvent;
import com.minecraft.minepay.bukkit.event.player.PlayerRefundedProductEvent;
import com.minecraft.minepay.bukkit.util.image.ImageCreator;
import com.minecraft.minepay.bukkit.util.map.MapCreator;
import com.minecraft.minepay.http.body.HttpBody;
import com.minecraft.minepay.http.data.product.ProductData;
import com.minecraft.minepay.http.data.transaction.TransactionCreateData;
import com.minecraft.minepay.http.data.transaction.TransactionData;
import com.minecraft.minepay.transaction.TransactionHandler;
import com.minecraft.minepay.util.color.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BukkitAccountExecutor extends AccountExecutor {

    public BukkitAccountExecutor(UUID uniqueId) {
        super(uniqueId);
    }

    @Override
    public void sendMessage(String message) {
        Player player = Bukkit.getPlayer(getUniqueId());
        if (player != null) {
            player.sendMessage(message);
        }
    }

    @Override
    public void sendTransactionCreated(TransactionCreateData transaction) {
        Runnable runnable = () -> {
            Player player = Bukkit.getPlayer(getUniqueId());
            if (player == null) {
                Core.getLogger().error("Ocorreu um erro ao enviar a transação recém criada para a uniqueId '%s'", getUniqueId().toString());
                return;
            }

            int hotbar = -1;

            for (int id = 0; id < 9; id++) {
                ItemStack hotbatItemStack = player.getInventory().getItem(id);

                if (hotbatItemStack == null || hotbatItemStack.getType().equals(Material.AIR)) {
                    hotbar = id;
                    break;
                }
            }

            if (hotbar == -1) {
                player.sendMessage(ColorUtil.RED + "Não há espaço em sua barra de ação (hotbar) para gerar o mapa de pagamento com o QR Code.");
                return;
            }

            String qrCode = transaction.getQrCodeBase64();
            if (qrCode == null) {
                player.sendMessage(ColorUtil.RED + "Ocorreu um erro ao gerar sua transação.");
                return;
            }

            player.sendMessage(ColorUtil.GREEN + "Você possui 15 minutos para efetuar o pagamento do produto.");

            ImageCreator imageCreator = ImageCreator.builder().data(transaction.getQrCodeBase64()).build();

            player.getInventory().setHeldItemSlot(hotbar);
            player.getInventory().setItem(hotbar, imageCreator.generate(player));
        };

        Bukkit.getScheduler().runTask(BukkitCore.getInstance(), runnable);
    }

    @Override
    public void sendTransaction(TransactionData transaction) {
        Runnable runnable = () -> {
            Account account = Account.getAccount(getUniqueId());

            ProductData product = transaction.getProductData();

            if (transaction.getStatus().equalsIgnoreCase("Cancelado")) {
                account.getExecutor().sendMessage(ColorUtil.RED + "A transação #" + transaction.getId() + " foi cancelada por falta de pagamento");
            }

            if (transaction.isPaid()) {

                Player player = Bukkit.getPlayer(account.getUniqueId());
                if (player != null) {
                    for (ItemStack content : player.getInventory().getContents()) {
                        if (content == null || content.getType().equals(Material.AIR)) {
                            continue;
                        }

                        if (content.hasItemMeta() && content.getItemMeta().hasDisplayName() && content.getItemMeta().getDisplayName().equalsIgnoreCase(MapCreator.getMapName())) {
                            player.getInventory().remove(content);
                        }
                    }
                }

                if (!transaction.isDelivered()) {

                    PlayerDeliveryProductEvent playerDeliveryProductEvent = new PlayerDeliveryProductEvent(account, product);
                    playerDeliveryProductEvent.call();

                    if (!playerDeliveryProductEvent.isCancelled()) {
                        account.getExecutor().sendMessage(ColorUtil.GREEN + "Pagamento confirmado com sucesso. Entregando seu produto...");

                        Core.getLogger().log("Enviando o produto '%s' para o jogador '%s'(%s)", product.getName(), account.getUsername(), account.getUniqueId().toString());

                        for (String command : product.getCommands()) {
                            command = command.replace("{playername}", account.getUsername());

                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                            Core.getLogger().log("Comando '%s' executado com sucesso.", command);
                        }
                    }

                    TransactionHandler.getInstance().updateTransaction(transaction, HttpBody.create().add("delivered", true));
                } else if (transaction.isRefunded() && !transaction.isProductRefunded()) {
                    PlayerRefundedProductEvent playerRefundedProductEvent = new PlayerRefundedProductEvent(account, product);
                    playerRefundedProductEvent.call();

                    if (!playerRefundedProductEvent.isCancelled()) {
                        Core.getLogger().log("Jogador '%s'(%s) pediu estorno do produto '%s'", account.getUsername(), account.getUniqueId().toString(), product.getName());

                        for (String command : product.getCommandsIfRefunded()) {
                            command = command.replace("{playername}", account.getUsername());

                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                            Core.getLogger().log("Comando '%s' executado com sucesso.", command);
                        }
                    }

                    TransactionHandler.getInstance().updateTransaction(transaction, HttpBody.create().add("productRefunded", true));
                }
            }

        };

        Bukkit.getScheduler().runTask(BukkitCore.getInstance(), runnable);
    }
}
