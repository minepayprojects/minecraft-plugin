package com.minecraft.minepay.http.data.category;

import com.minecraft.minepay.http.data.product.ProductData;
import com.minecraft.minepay.http.data.shop.ShopData;
import lombok.Data;

import java.util.HashSet;

@Data
public class CategoryData {

    private final int id;

    private ShopData shopData;

    private HashSet<ProductData> products;

    private final String name, description_ingame, display_ingame;

    private final boolean enabled;

    private final String createdAt, updatedAt, deletedAt;

    public ProductData getProductByName(String name) {
        return getProducts().stream().filter(productData -> productData.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
