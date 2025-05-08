package com.minecraft.minepay.http.response.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.minecraft.minepay.Core;
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
public class ShopListResponse extends HttpResponse {

    private boolean success;

    private String message;

    private int total;

    private List<ShopData> shops = new ArrayList<>();

    @Override
    public ShopListResponse parse(HttpRequest request) {
        setSuccess(request.isSuccess());
        setMessage(!request.isSuccess() ? request.getErrorMessage() : request.getMessage());

        if (isSuccess()) {
            JsonObject object = request.getJsonObject();

            setTotal(object.get("total").getAsInt());

            JsonArray data = object.get("data").getAsJsonArray();

            for (int i = 0; i < data.size(); i++) {
                object = data.get(i).getAsJsonObject();

                ShopData shop = Core.GSON.fromJson(object, ShopData.class);

                shop.setStoreData(Core.GSON.fromJson(object.get("store"), StoreData.class));

                getShops().add(shop);
            }

        }

        return this;
    }
}
