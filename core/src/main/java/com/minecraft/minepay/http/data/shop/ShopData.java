package com.minecraft.minepay.http.data.shop;

import com.minecraft.minepay.http.data.store.StoreData;
import lombok.Data;

@Data
public class ShopData {

    private final int id;

    private final String name, display_in_game;

    private final boolean enabled;

    private StoreData storeData;

    private final String createdAt, deletedAt, updatedAt;

}
