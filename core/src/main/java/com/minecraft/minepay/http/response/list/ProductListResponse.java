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
import java.util.List;

@Getter
@Setter
public class ProductListResponse extends HttpResponse {

    private boolean success;

    private String message;

    private int total;

    private List<ProductData> products = new ArrayList<>();

    @Override
    public ProductListResponse parse(HttpRequest request) {
        setSuccess(request.isSuccess());
        setMessage(!request.isSuccess() ? request.getErrorMessage() : request.getMessage());

        if (isSuccess()) {
            JsonObject object = request.getJsonObject();

            setTotal(object.get("total").getAsInt());

            JsonArray data = object.get("data").getAsJsonArray();

            for (int i = 0; i < data.size(); i++) {
                object = data.get(i).getAsJsonObject();

                int categoryId = object.get("categoryId").getAsInt();

                ProductData product = Core.GSON.fromJson(object, ProductData.class);

                CategoryData category = Core.getCategoryController().getCategory(categoryId);

                product.setCategoryData(category);

                getProducts().add(product);
            }

        }

        return this;
    }
}
