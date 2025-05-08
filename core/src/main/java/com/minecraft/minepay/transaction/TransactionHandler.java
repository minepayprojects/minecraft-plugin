package com.minecraft.minepay.transaction;

import com.minecraft.minepay.Core;
import com.minecraft.minepay.http.HttpHandlerBuilder;
import com.minecraft.minepay.http.body.HttpBody;
import com.minecraft.minepay.http.data.transaction.TransactionData;
import com.minecraft.minepay.http.request.HttpRequest;
import com.minecraft.minepay.http.type.HttpType;

import java.util.function.Consumer;

public class TransactionHandler {

    public static TransactionHandler getInstance() {
        return new TransactionHandler();
    }

    public void updateTransaction(TransactionData transaction, HttpBody body) {
        HttpHandlerBuilder builder = HttpHandlerBuilder.builder()
                .type(HttpType.PUT)
                .path("/transaction/" + transaction.getId())
                .body(body)
                .tokenFinder("store:" + transaction.getStoreData().getId())
                .minepay(true)
                .build();

        Core.getHttpHandler().execute(builder);
    }

    public void createTransaction(String username, int storeId, int categoryId, int shopId, int productId, int gatewayId, String uniqueId, String address, Consumer<HttpRequest> consumer) {
        createTransaction(username, storeId, categoryId, shopId, productId, gatewayId, uniqueId, address, null, consumer);
    }

    public void createTransaction(String username, int storeId, int categoryId, int shopId, int productId, int gatewayId, String uniqueId, String address, String coupon, Consumer<HttpRequest> consumer) {
        HttpBody body = HttpBody.create()
                .add("categoryId", categoryId)
                .add("shopId", shopId)
                .add("productId", productId)
                .add("gatewayId", gatewayId)
                .add("uniqueId", uniqueId)
                .add("username", username)
                .add("address", address);

        if (coupon != null) {
            body.add("centralCartCoupon", coupon);
        }

        HttpHandlerBuilder builder = HttpHandlerBuilder.builder()
                .type(HttpType.POST)
                .path("/transaction")
                .body(body)
                .tokenFinder("store:" + storeId)
                .minepay(true)
                .build();

        Core.getHttpHandler().execute(builder, consumer);
    }
}
