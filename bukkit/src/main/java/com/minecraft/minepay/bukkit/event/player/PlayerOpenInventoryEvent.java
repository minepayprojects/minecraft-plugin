package com.minecraft.minepay.bukkit.event.player;

import com.minecraft.minepay.account.Account;
import com.minecraft.minepay.bukkit.event.ServerEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Cancellable;

@RequiredArgsConstructor
@Getter
public class PlayerOpenInventoryEvent extends ServerEvent implements Cancellable {

    private final Account account;

    @Setter
    private boolean cancelled;

}
