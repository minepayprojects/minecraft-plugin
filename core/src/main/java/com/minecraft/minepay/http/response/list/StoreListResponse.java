package com.minecraft.minepay.http.response.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.minecraft.minepay.Core;
import com.minecraft.minepay.http.data.gateway.GatewayData;
import com.minecraft.minepay.http.data.store.StoreData;
import com.minecraft.minepay.http.data.transaction.TransactionData;
import com.minecraft.minepay.http.request.HttpRequest;
import com.minecraft.minepay.http.response.HttpResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class StoreListResponse extends HttpResponse {

    private boolean success;

    private String message;

    private List<StoreData> stores = new ArrayList<>();

    @Override
    public StoreListResponse parse(HttpRequest request) {
        setSuccess(request.isSuccess());
        setMessage(!request.isSuccess() ? request.getErrorMessage() : request.getMessage());

        if (isSuccess()) {
            JsonObject object = request.getJsonObject();

            JsonArray data = object.get("data").getAsJsonArray();

            for (int i = 0; i < data.size(); i++) {
                object = data.get(i).getAsJsonObject();

                StoreData store = Core.GSON.fromJson(object, StoreData.class);

                store.setGateways(new HashSet<>());

                JsonArray gateways = object.get("gateways").getAsJsonArray();

                for (int y = 0; y < gateways.size(); y++) {
                    object = gateways.get(y).getAsJsonObject();

                    GatewayData gatewayData = Core.GSON.fromJson(object.get("gateway").getAsJsonObject(), GatewayData.class);

                    store.getGateways().add(gatewayData);
                }

                getStores().add(store);
            }

        }

        return this;
    }
}
