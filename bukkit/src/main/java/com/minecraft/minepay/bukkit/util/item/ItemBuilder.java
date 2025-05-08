package com.minecraft.minepay.bukkit.util.item;

import com.minecraft.minepay.util.color.ColorUtil;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.function.Consumer;

public class ItemBuilder {

    @Getter
    private ItemStack itemStack;

    private List<ItemFlag> itemFlagList = new ArrayList<ItemFlag>();

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    public ItemBuilder(Material material, int amount, int durability) {
        this(new ItemStack(material, amount, (short) durability));
    }

    public ItemBuilder changeItem(Consumer<ItemStack> consumer) {
        consumer.accept(itemStack);
        return this;
    }

    public ItemBuilder changeMeta(Consumer<ItemMeta> consumer) {
        var itemMeta = itemStack.getItemMeta();

        consumer.accept(itemMeta);

        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder material(Material material) {
        return changeItem(itemStack -> itemStack.setType(material));
    }

    public ItemBuilder amount(int amount) {
        return changeItem(itemStack -> itemStack.setAmount(amount));
    }

    public ItemBuilder durability(int durability) {
        return changeMeta(meta -> itemStack.setDurability((short) durability));
    }

    public ItemBuilder name(String name) {
        return changeMeta(itemMeta -> itemMeta.setDisplayName(name));
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        return changeItem(itemStack -> itemStack.addUnsafeEnchantment(enchantment, level));
    }

    public ItemBuilder addFlag(ItemFlag... itemFlags) {
        return changeMeta(itemMeta -> itemMeta.addItemFlags(itemFlags));
    }

    public ItemBuilder removeFlag(ItemFlag... itemFlags) {
        return changeMeta(itemMeta -> itemMeta.removeItemFlags(itemFlags));
    }

    public ItemBuilder glow(Boolean glowing) {
        if (glowing) {
            addEnchant(Enchantment.INFINITY, -1);
            addFlag(ItemFlag.HIDE_ENCHANTS);
        }

        return this;
    }

    public ItemBuilder lore(String lore) {
        return changeMeta(itemMeta -> itemMeta.setLore(getFormattedLore(25, lore)));
    }

    public ItemBuilder lore(String lore, Object... values) {
        return changeMeta(itemMeta -> itemMeta.setLore(getFormattedLore(25, String.format(lore, values))));
    }

    public ItemStack build() {
        return itemStack.clone();
    }

    public List<String> getFormattedLore(int limit, String oldText) {
        List<String> lore = new ArrayList<>();

        String text = ChatColor.translateAlternateColorCodes('&', oldText);

        String[] split = text.split(" ");
        String color = "";
        text = "";

        for (int i = 0; i < split.length; i++) {

            if (ChatColor.stripColor(text).length() >= limit) {
                lore.add(ColorUtil.GRAY + text);

                if (text.endsWith(".") || text.endsWith("!")) {
                    lore.add("");
                }

                text = color;
            }

            String toAdd = split[i];

            String SPACE = ChatColor.stripColor(text).isEmpty() || ChatColor.stripColor(text).isBlank() ? "" : " ";

            if (toAdd.contains("§")) {
                color = ChatColor.getLastColors(toAdd.toLowerCase());
            }

            if (toAdd.contains("\n")) {

                toAdd = toAdd.substring(0, toAdd.indexOf("\n"));

                split[i] = split[i].substring(toAdd.length() + 1);

                lore.add(ColorUtil.GRAY + text + SPACE + toAdd);

                text = color;

                i--;

            } else {

                text += SPACE + toAdd;
            }
        }

        lore.add(ColorUtil.GRAY + text);

        return lore;
    }

}