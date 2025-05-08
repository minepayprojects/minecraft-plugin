package com.minecraft.minepay.bukkit.util.inventory;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@RequiredArgsConstructor
@Getter
@Setter
public class InventoryApi {

    private final int slot;

    private final String name;

    private HashMap<Integer, ItemStack> items = new HashMap<>();

    public void updateInventory(@NotNull Player player) {
        InventoryView inventory = player.getOpenInventory();

        getItems().forEach(inventory::setItem);

        player.updateInventory();
    }

    public void openInventory(@NotNull Player player) {
        String name = getName().length() > 32 ? getName().substring(0, 32) : getName();

        Inventory inventory = Bukkit.createInventory(player, getSlot(), name);

        getItems().forEach(inventory::setItem);

        player.setItemOnCursor(null);

        player.openInventory(inventory);

        player.updateInventory();

//        try {
//            Object craftPlayer = ReflectionUtils.toEntityHuman(player);
//            assert craftPlayer != null;
//
//            Class<?> _class = Class.forName(craftPlayer.getClass().getName().replace("Player", "Human"));
//            Object defaultContainer = _class.getField("defaultContainer").get(craftPlayer);
//            Field activeContainer = _class.getField("activeContainer");
//
//            if (activeContainer.get(craftPlayer) == defaultContainer) {
//                player.openInventory(getInventory());
//                return;
//            }
//
//            Class.forName(ReflectionUtils.getOBCPackageName(_class) + ".event.CraftEventFactory")
//                    .getMethod("handleInventoryCloseEvent", _class).invoke(null, craftPlayer);
//
//            activeContainer.set(craftPlayer, defaultContainer);
//
//            player.openInventory(getInventory());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void setItem(int slot, @NonNull ItemStack itemStack) {
        getItems().put(slot, itemStack);
    }


}
