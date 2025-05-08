package com.minecraft.minepay.bukkit.menu;

import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class MenuController {

    public final HashMap<UUID, Menu> menus = new HashMap<>();

}
