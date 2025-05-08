package com.minecraft.minepay.bukkit.command;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.bukkit.BukkitCore;
import com.minecraft.minepay.bukkit.config.BukkitConfiguration;
import com.minecraft.minepay.http.HttpHandler;
import com.minecraft.minepay.util.color.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class MinepayCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length < 1) {
            sender.sendMessage(ColorUtil.RED + "Uso correto: /" + label + " <configreload|storesreload>");
            return true;
        }

        switch (args[0]) {

            case "configreload": {
                BukkitCore.getInstance().setConfiguration();

                sender.sendMessage(ColorUtil.GREEN + "Configurações atualizadas com sucesso.");
                return true;
            }

            case "storesreload": {
                sender.sendMessage(ColorUtil.GREEN + "Buscando novamente as stores, shops, categorias, produtos....");

                Core.getStoreController().loadStores();

                sender.sendMessage(ColorUtil.GREEN + "Buscas feitas com sucesso!");
                return true;
            }

            default: {
                sender.sendMessage(ColorUtil.RED + "Uso correto: /" + label + " <configreload|storesreload>");
                return true;
            }

        }
    }

}
