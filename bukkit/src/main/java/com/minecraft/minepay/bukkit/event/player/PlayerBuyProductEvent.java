package com.minecraft.minepay.bukkit.event.player;

import com.minecraft.minepay.bukkit.event.ServerEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

@RequiredArgsConstructor
@Getter
@Setter
public class PlayerBuyProductEvent extends ServerEvent implements Cancellable {

    private final Player player;

    private int storeId;

    private int categoryId;

    private int shopId;

    private int productId;

    private int gatewayId;

    private String address;

    private String coupon;

    private int hotbar = -1;

    private boolean cancelled;

}
