package com.minecraft.minepay.http.data.transaction;

import com.google.gson.JsonObject;
import com.minecraft.minepay.http.data.gateway.GatewayData;
import com.minecraft.minepay.http.data.product.ProductData;
import com.minecraft.minepay.http.data.store.StoreData;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class TransactionData {

    private final int id;

    private final String uniqueId, username;

    private final String transactionId;

    private final boolean delivered, paid, refunded, productRefunded;

    private String qrCode, qrCodeBase64, ticketUrl;

    private final String status, statusDetail;

    private JsonObject details;

    private StoreData storeData;

    private ProductData productData;

    private GatewayData gatewayData;

    private final String expireAt, createdAt, updatedAt, deletedAt;

}
