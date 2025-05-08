package com.minecraft.minepay.bukkit.event.player;

import com.minecraft.minepay.account.Account;
import com.minecraft.minepay.bukkit.event.ServerEvent;
import com.minecraft.minepay.http.data.product.ProductData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Cancellable;

@RequiredArgsConstructor
@Getter
public class PlayerRefundedProductEvent extends ServerEvent implements Cancellable {

    private final Account account;

    private final ProductData product;

    @Setter
    private boolean cancelled;

}
