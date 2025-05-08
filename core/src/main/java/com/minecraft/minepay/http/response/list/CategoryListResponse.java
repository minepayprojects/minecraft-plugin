package com.minecraft.minepay.http.response.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.minecraft.minepay.Core;
import com.minecraft.minepay.http.data.category.CategoryData;
import com.minecraft.minepay.http.data.product.ProductData;
import com.minecraft.minepay.http.data.shop.ShopData;
import com.minecraft.minepay.http.data.store.StoreData;
import com.minecraft.minepay.http.request.HttpRequest;
import com.minecraft.minepay.http.response.HttpResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class CategoryListResponse extends HttpResponse {

    private boolean success;

    private String message;

    private List<CategoryData> categories = new ArrayList<>();

    @Override
    public CategoryListResponse parse(HttpRequest request) {
        setSuccess(request.isSuccess());
        setMessage(!request.isSuccess() ? request.getErrorMessage() : request.getMessage());

        if (isSuccess()) {
            JsonObject object = request.getJsonObject();

            JsonArray data = object.get("data").getAsJsonArray();

            for (int i = 0; i < data.size(); i++) {
                object = data.get(i).getAsJsonObject();

                CategoryData category = Core.GSON.fromJson(object, CategoryData.class);

                category.setProducts(new HashSet<>());
                category.setShopData(Core.GSON.fromJson(object.get("shop"), ShopData.class));

                JsonArray products = object.get("products").getAsJsonArray();

                for (int y = 0; y < products.size(); y++) {
                    JsonObject product = products.get(y).getAsJsonObject();

                    ProductData productData = Core.GSON.fromJson(product, ProductData.class);

                    category.getProducts().add(productData);
                }

                getCategories().add(category);
            }

        }

        return this;
    }
}
