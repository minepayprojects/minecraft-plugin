package com.minecraft.minepay.bukkit.event.inventory;

import com.minecraft.minepay.bukkit.event.ServerEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@RequiredArgsConstructor
@Getter
@Setter
public class InventoryProductsMenuOpenEvent extends ServerEvent implements Cancellable {

    private boolean cancelled;

    private final Player player;

}
