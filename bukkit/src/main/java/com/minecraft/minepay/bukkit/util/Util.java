package com.minecraft.minepay.bukkit.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class Util {

    public static ItemStack findItemStackByMaterialName(String material) {
        try {
            if (material == null) {
                return new ItemStack(Material.BARRIER);
            }

            if (material.equalsIgnoreCase("MercadoPago")) {
                return new ItemStack(findItemStackById("351").getType(), 1, (short) 4);
            }

            if (material.equalsIgnoreCase("PicPay")) {
                return new ItemStack(findItemStackById("351").getType(), 1, (short) 10);
            }

            if (material.equalsIgnoreCase("PagSeguro")) {
                return new ItemStack(findItemStackById("351").getType(), 1, (short) 11);
            }

            ItemStack itemStack = findItemStackById(material);
            if (itemStack != null) return itemStack;

            return new ItemStack(Objects.requireNonNull(Material.getMaterial(material)));
        } catch (Exception e) {
            e.printStackTrace();
            return new ItemStack(Material.BARRIER);
        }
    }


    private static ItemStack findItemStackById(@NotNull String id) {
        try {
            return new ItemStack(Objects.requireNonNull(Arrays.stream(Material.values()).filter(material -> material.getId() == Integer.parseInt(id)).findFirst().orElse(null)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getAddress(Player player) {
        try {
            return player.getAddress().getAddress().getHostAddress();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }

}
