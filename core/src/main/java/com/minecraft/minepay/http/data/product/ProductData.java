package com.minecraft.minepay.http.data.product;

import com.minecraft.minepay.http.data.category.CategoryData;
import lombok.Data;

@Data
public class ProductData {

    private final int id;

    private CategoryData categoryData;

    private final String name, description_ingame, display_ingame;

    private final boolean enabled;

    private final float price;

    private final String[] commands, commandsIfRefunded;

    private final String createdAt, deletedAt;

}
